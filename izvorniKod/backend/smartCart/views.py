from django import forms
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login as logging_in, logout as logging_out
from django.contrib.auth.decorators import login_required
from django.shortcuts import redirect
from django.urls import reverse
from .models import Trgovina, Artikl, SecretCode, Proizvođač, Zemlja_porijekla


def index(request):
    """
    kad se zatraži "/", funkcija vraća početnu stranicu
    koja se zove index.html
    """
    return render(request, 'smartCart/index.html', {})


def sign_up(request):
    if request.method == 'GET':
        return render(request, 'smartCart/signup.html', {})

    email = request.POST['email']
    password = request.POST['password']
    confirm_password = request.POST['confirm_password']
    secret_code = request.POST['secret_code']

    try:
        validate_email(email)
    except ValidationError:
        return render(request, 'smartCart/signup.html', {'message': 'Wrong mail format\n'})
    if password != confirm_password:
        return render(request, 'smartCart/signup.html', {'message': 'Passwords don\'t match\n'})
    if not SecretCode.objects.filter(value=secret_code).exists():
        return render(request, 'smartCart/signup.html', {'message': 'Wrong secret code\n'})
    User.objects.create_user(email, email, password)
    return render(request, 'smartCart/index.html', {})

    # if secret_code not in


def login(request):
    """
    stranica za ulogiravanje korisnika
    ako joj se pristupa metodi GET, vraća se stranica za ulogriavanje: login.html
    ako joj se pristupa metodi POST, podaci se validiraju
    ako se uspješno validiraju, šalje se HTML GET upit na "/trgovac"
    ako se neuspješno validiraju, stranica login.html se ponovno učitava
    """
    if request.user.is_authenticated:
        return redirect('trgovac')
    if request.method == 'GET':
        return render(request, 'smartCart/login.html', {})

    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(request, username=username, password=password)

    if user is not None:
        logging_in(request, user)
        return redirect('trgovac')
    else:
        return render(request, 'smartCart/login.html', {'message': 'Invalid username or password\n'})


@login_required
def trgovac(request):
    """
    glavna stranica za trgovca, vraća se stranica "trgovac.html"
    služi za pregled svih namirnica i svih trgovina
    postoje i formulari preko kojih se može dodati nova trgovina i novi proizvod
    može joj se pristupiti metodi GET
    """
    if request.method == 'GET':
        if request.user.is_authenticated:
            # ------
            trgovine = list(Trgovina.objects.all())
            artikli = list(Artikl.objects.all())
            # ------
            return render(request, 'smartCart/trgovac.html', {'trgovine': trgovine, 'artikli': artikli})
        else:
            return render(request, 'smartCart/index.html', {})

    # ako metoda nije post ova grana se izvršava, trebalo bi baciti err
    # sljedeća funkcija vraća prethodnu stranicu, ostatak od prošlih kemijanja
    return redirect(request.META['HTTP_REFERER'])


def dodaj_trgovine(request):
    """
    pomoćni view za dodavanje trgovine
    kad se dodaju trgovine, šalje se HTTP POST na "trgovac/dodaj-trgovine"
    trgovina se dodaje u bazu podatka i zatim
    se šalje HTTP GET zahtjev za "/trgovac" da se sve lijepo refresha i pokaže promjena
    """
    if request.method == 'GET':
        return redirect(request.META['HTTP_REFERER'])

    sifTrgovina = request.POST['sifTrgovina']
    nazTrgovina = request.POST['nazTrgovina']

    trgovina = Trgovina(sifTrgovina=sifTrgovina, nazTrgovina=nazTrgovina)
    trgovina.save()

    return redirect(request.META['HTTP_REFERER'])


def dodaj_artikle(request):
    """
    pomoćno view za dodavanje artikala
    kad se dodaju artikli, šalje se HTTP POST na "/trgovac/dodaj-artikle"
    artikl se dodaje i bazu podataka
    zatim se šalje HTTP GET zahtjev za "/trgovac"
    KOMENTAR: ima masu ovih "if then var = None" jer se to translatira u NULL vrijednosti u bazi podataka
    Nisam postavio default vrijednosti pa sam išao nullovima
    ako se objekt stvori, i pospremi u bazu, one varijable koje njemu nismo napunili idu odmah u NULL ili default vrijednost
    npr. u artiklu nisam nigdje napucao niti jedan vote_count!!!!, spremaju se NULL vrijednosti
    P.S. Ako pod "Zemlja porijekla" probaš upisati "ikaguhskadghaskfhasfkhasf" dobijaš err
    Stvar je u tome da se radi o 1..1 : N..0 vezi tj. iz zemlje dolazi 0..N proizvoda, a proizvod ima točno jednu zemlju porijekla!
    Zemlja porijekla je foreign key u artiklu
    Uh... Kako 1..1 pretvoriti u 0..1? Staviti null? Probao sam i ne ide.
    ovo bih trebao pisati u models.py, zar ne?
    """
    if request.method == 'GET':
        return redirect(request.META['HTTP_REFERER'])
    barkod_artikla = request.POST['barkod_artikla']
    naziv_artikla = request.POST['naziv_artikla']
    opis_artikla = request.POST['opis_artikla']
    proizvođač = request.POST['proizvođač']
    zemlja_porijekla = request.POST['zemlja_porijekla']
    vegan = request.POST['vegan']

    proizvođač = add_proizvođač(proizvođač)
    zemlja_porijekla = add_zemlja_porijekla(zemlja_porijekla)

    if opis_artikla == '':
        opis_artikla = None

    if vegan == '':
        vegan = None

    artikl_za_dodati = Artikl(
        naziv_artikla=naziv_artikla,
        barkod_artikla=barkod_artikla,
        opis_artikla=opis_artikla,
        proizvođač=proizvođač,
        zemlja_porijekla=zemlja_porijekla,
        vegan=vegan
    )
    artikl_za_dodati.save()

    return redirect(request.META['HTTP_REFERER'])


def trgovina(request, sifTrgovina):
    """
    stranica za prikaz trgovine, informacije o trgovini i proizvoda dostupnim u tim trgovinama
    slanjem HTTP GET upita na "/trgovina/<int>" gdje je <int> cijeli broj koji predstavlja unikatnu šifru baš te trgovine
    vraća se "trgovina.html" s podacije o toj trgovini
    slanjem HTTP POST upita na "/trgovina" sa informacijama o artiklu (konkretnike: barkoda artikla), artikl se može dodati u trgovinu
    """

    if request.method == 'POST':
        bar_k = request.POST['barkod']
        sif_t = request.POST['sifTrgovina']
        t = Trgovina.objects.get(sifTrgovina=sif_t)
        a = Artikl.objects.get(barkod_artikla=bar_k)
        t.artikli.add(a)
        t.save()
        return redirect(request.META['HTTP_REFERER'])

    t = Trgovina.objects.get(sifTrgovina=sifTrgovina)
    return render(request, 'smartCart/trgovina.html',
                  {'sifTrgovina': sifTrgovina, 'nazTrgovina': t.nazTrgovina, 'artikli': t.artikli.all()})


def artikl(request, barkod_artikla):
    """
    stranica za prikaz artikla i informacija o artiklu
    slanjem HTTP GET upita na "/artikl/<int>" gdje je <int> cijeli broj koji predstavlja unikatnu šifru tog artikla
    vraća se "artikl.html" koji sadrži podatke o baš tom artiklu
    """
    a = Artikl.objects.get(barkod_artikla=barkod_artikla)
    return render(request, 'smartCart/artikl.html', {'barkod_artikla': barkod_artikla, 'artikl': a})


# TODO: u slučaju da je korisnik izlogiran i pokuša se izlogirati treba vratiti grešku
def logout(request):
    """
    logout stranica, ne može se prikazati
    ovo je potrebno doraditi, zajedno s prikazom ostalih stranica. Treba provjeriti tko je ulogiran i tko radi što
    request.user.is_authenticated je funkcija korištena pri logiranju
    """
    if not request.user.is_authenticated:
        # TODO: Vrati grešku
        pass

    logging_out(request)
    return render(request, 'smartCart/index.html', {})


def add_proizvođač(name):
    try:
        return Proizvođač.objects.get(naziv=name)
    except Proizvođač.DoesNotExist:
        return None


def add_zemlja_porijekla(name):
    try:
        return Zemlja_porijekla.objects.get(naziv=name)
    except Zemlja_porijekla.DoesNotExist:
        return None
