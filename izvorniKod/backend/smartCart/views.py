from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render, get_object_or_404
from django.contrib.auth import authenticate, login as logging_in, logout as logging_out
from django.contrib.auth.decorators import login_required, user_passes_test
from django.shortcuts import redirect
from .models import Trgovina, Artikl, SecretCode, Proizvodac, Zemlja_porijekla, TrgovinaArtikli
from .forms import LoginForm, DodajTrgovinu, DodajArtikl, SignUpTrgovacForm, SignUpKupacForm, DodajProizvodaca, \
    DodajArtiklUTrgovinu, UrediArtiklUTrgovini, PromijeniRadnoVrijeme, EditLogin
from django.contrib.auth import get_user_model
from django.core import serializers
from django.http import HttpResponse, JsonResponse, HttpResponseRedirect
import json

User = get_user_model()


def trgovac_login_required(user):
    return user.is_trgovac if user.is_authenticated else False


def kupac_login_required(user):
    return user.is_kupac if user.is_authenticated else False


# Android
# ------------------------------------------------------------------------------------------
# funkcija kojom se vraća json određenih artikala
def android_artikli(request):
    try:
        naziv_artikla = request.POST['naziv_artikla']
    except:
        naziv_artikla = ''

    artikli = Artikl.objects.filter(naziv_artikla__contains='%s' % naziv_artikla)
    artikli_json = serializers.serialize('json', artikli)

    response = HttpResponse(artikli_json, content_type='application/json')
    response.status_code = 200
    return response


# funkcija koja vraća artikle
# prima barkodove traženih artikala
# TODO: provjeriti rad ove "loads" funkcije
def android_popis(request):
    barkodovi = json.loads(request.POST['barkodovi'])
    artikli = {}
    for barkod in barkodovi:
        artikli += Artikl.objects.filter(barkod_artikla=barkod)
    artikli_json = serializers.serialize('json', artikli)
    artikli_json = serializers.serialize('json', artikli)

    response = HttpResponse(artikli_json, content_type='application/json')
    response.status_code = 200
    return response


# funkcija kojom se vraća json određenih trgovina
# žao mi je što izgleda ružno :(
def android_trgovine(request):
    try:
        naz_trgovina = request.POST['naz_trgovina']
    except:
        naz_trgovina = ''

    try:
        sif_trgovina = request.POST['sif_trgovina']
    except:
        sif_trgovina = None

    trgovine = Trgovina.objects.filter(naz_trgovina__contains='%s' % naz_trgovina)

    if sif_trgovina is not None:
        trgovine = trgovine.filter(sif_trgovina=trgovina)

    trgovine_json = serializers.serialize('json', trgovine)

    response = HttpResponse(trgovine_json, content_type='application/json')
    response.status_code = 200
    return response

# funkcija za ulogiravanje s android uređaja
# vraća http odgovor
def android_login(request):
    email = json.loads(request.body)['email']
    password = json.loads(request.body)['password']

    if(email == "" or password == ""):
        json_response = JsonResponse({'err': 'Fill out all fields'})
        json_response.status_code = 401
        return json_response

    user = authenticate(request, username=email, password=password)

    if user is not None:
        logging_in(request=request, user=user)
        response = HttpResponse()
        response.status_code = 200
        return response
    else:
        json_response = JsonResponse({'err': 'Wrong email or password'})
        json_response.status_code = 401
        return json_response


# funkcija za izlogiravanje s android uređaja
# vraća http odgovor
def android_logout(request):
    logging_out(request=request)
    response = HttpResponse()
    response.status_code = 200
    return response


# funkcija za stvaranje računa s android uređaja
# vraća http odgovor
def android_sign_up(request):
    email = json.loads(request.body)['email']
    password = json.loads(request.body)['password']
    secret_code = json.loads(request.body)['secret_code']

    if secret_code:
        authorisation_level = 'trgovac'
    else:
        authorisation_level = 'kupac'

    if(email == "" or password == ""):
        json_response = JsonResponse({'err': 'Fill out all fields'})
        json_response.status_code = 401
        return json_response

    if (User.objects.filter(email=email).exists()):
        json_response = JsonResponse({'err': 'User already exists'})
        json_response.status_code = 401
        return json_response

    if (authorisation_level == 'kupac'):
        User.objects.create_user(email, password, is_kupac=True)
        response = HttpResponse()
        response.status_code = 200
        return response

    if (authorisation_level == 'trgovac'):
        secret_code = SecretCode.objects.filter(value=secret_code)
        if not secret_code.exists():
            json_response = JsonResponse({'err': 'Wrong secret code'})
            json_response.status_code = 401
            return json_response
        secret_code.delete()
        User.objects.create_user(email, password, is_trgovac=True)

        response = HttpResponse()
        response.status_code = 200
        return response

    json_response = JsonResponse({'err': 'User already exists'})
    json_response.status_code = 401
    return json_response


# ------------------------------------------------------------------------------------------

def index(request):
    """
    kad se zatraži "/", funkcija vraća početnu stranicu
    koja se zove index.html
    """
    return render(request, 'smartCart/index.html', {'user': request.user})


def sign_up_trgovac(request):
    if request.method == 'POST':
        form = SignUpTrgovacForm(request.POST)
        if form.is_valid():
            email = request.POST['email']
            password = request.POST['password']
            confirm_password = request.POST['confirm_password']
            secret_code = request.POST['secret_code']
            try:
                validate_email(email)
            except ValidationError:
                return render(request, 'smartCart/signup_trgovac.html',
                              {'message': 'Wrong mail format\n', 'form': form})
            if password != confirm_password:
                return render(request, 'smartCart/signup_trgovac.html',
                              {'message': 'Passwords don\'t match\n', 'form': form})
            secret_code = SecretCode.objects.filter(value=secret_code)
            if not secret_code.exists():
                return render(request, 'smartCart/signup_trgovac.html',
                              {'message': 'Wrong secret code\n', 'form': form})
            else:
                secret_code.delete()
            User.objects.create_user(email, password, is_trgovac=True)
            return render(request, 'smartCart/index.html', {})
    else:
        form = SignUpTrgovacForm()
        return render(request, 'smartCart/signup_trgovac.html', {'form': form})


def sign_up_kupac(request):
    if request.method == 'POST':
        form = SignUpKupacForm(request.POST)
        if form.is_valid():
            email = request.POST['email']
            password = request.POST['password']
            confirm_password = request.POST['confirm_password']
            try:
                validate_email(email)
            except ValidationError:
                return render(request, 'smartCart/signup_kupac.html',
                              {'message': 'Wrong mail format\n', 'form': form})
            if password != confirm_password:
                return render(request, 'smartCart/signup_kupac.html',
                              {'message': 'Passwords don\'t match\n', 'form': form})
            User.objects.create_user(email, password, is_kupac=True)
            return render(request, 'smartCart/index.html', {})
    else:
        form = SignUpKupacForm()
        return render(request, 'smartCart/signup_kupac.html', {'form': form})


def login(request):
    """
    stranica za ulogiravanje korisnika
    ako joj se pristupa metodi GET, vraća se stranica za ulogriavanje: login.html
    ako joj se pristupa metodi POST, podaci se validiraju
    ako se uspješno validiraju, šalje se HTML GET upit na "/trgovac"
    ako se neuspješno validiraju, stranica login.html se ponovno učitava
    """
    if request.user.is_authenticated:
        if trgovac_login_required(request.user):
            return redirect('trgovac')
        else:
            return redirect('index')
    if request.method == 'POST':
        form = LoginForm(request.POST)
        if form.is_valid():
            username = request.POST['username']
            password = request.POST['password']
            user = authenticate(request, username=username, password=password)
            if user is not None:
                logging_in(request, user)
                return redirect('trgovac')
            else:
                return render(request, 'smartCart/login.html',
                              {'message': 'Invalid username or password\n', 'form': form})
    else:
        form = LoginForm()
        return render(request, 'smartCart/login.html', {'form': form})


@login_required(login_url='login/')
def edit_profile(request):
    if request.method == 'POST':
        form = EditLogin(request.POST)
        if form.is_valid():
            password = request.POST['password']
            confirm_password = request.POST['confirm_password']
            if password == confirm_password:
                request.user.set_password(password)
                request.user.save()
                return redirect('index')
            else:
                return render(request, 'smartCart/edit_profile.html',
                              {'message': 'Passwords don\'t match\n', 'form': form})
    else:
        form = EditLogin()
        return render(request, 'smartCart/edit_profile.html', {'form': form})


@user_passes_test(trgovac_login_required, login_url='login/')
def trgovac(request):
    """
    glavna stranica za trgovca, vraća se stranica "trgovac.html"
    služi za pregled svih namirnica i svih trgovina
    postoje i formulari preko kojih se može dodati nova trgovina i novi proizvod
    može joj se pristupiti metodi GET
    """
    if request.method == 'GET':
        trgovine = list(Trgovina.objects.filter(vlasnik__id=request.user.id))
        artikli = list(Artikl.objects.all())
        nova_trgovina = DodajTrgovinu()
        novi_artikl = DodajArtikl()
        novi_proizvodac = DodajProizvodaca()
        return render(request, 'smartCart/trgovac.html', {'trgovine': trgovine, 'artikli': artikli,
                                                          'trg_form': nova_trgovina, 'art_form': novi_artikl,
                                                          'pro_form': novi_proizvodac})

    # ako metoda nije post ova grana se izvršava, trebalo bi baciti err
    # sljedeća funkcija vraća prethodnu stranicu, ostatak od prošlih kemijanja
    return redirect(request.META['HTTP_REFERER'])


@user_passes_test(trgovac_login_required, login_url='login/')
def dodaj_trgovine(request):
    """
    pomoćni view za dodavanje trgovine
    kad se dodaju trgovine, šalje se HTTP POST na "trgovac/dodaj-trgovine"
    trgovina se dodaje u bazu podatka i zatim
    se šalje HTTP GET zahtjev za "/trgovac" da se sve lijepo refresha i pokaže promjena
    """

    if request.method == 'GET':
        return redirect(request.META['HTTP_REFERER'])
    form = DodajTrgovinu(request.POST)
    print(form)
    print(form.is_valid())
    if form.is_valid():
        print("tu sam-----------------------------------------------------------------------------")
        naz_trgovina = request.POST['naz_trgovina']
        adr_trgovina = request.POST['adresa_trgovina']
        rad_vri_pocetak = request.POST['radno_vrijeme_pocetak']
        rad_vri_kraj = request.POST['radno_vrijeme_kraj']
        trgovina = Trgovina(naz_trgovina=naz_trgovina,
                            adresa_trgovina=adr_trgovina,
                            vlasnik=get_object_or_404(User, pk=request.user.id),
                            radno_vrijeme_pocetak=rad_vri_pocetak,
                            radno_vrijeme_kraj=rad_vri_kraj)
        print(trgovina)
        trgovina.save()

    return redirect(request.META['HTTP_REFERER'])


@user_passes_test(trgovac_login_required, login_url='login/')
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
    form = DodajArtikl(request.POST)
    if form.is_valid():
        barkod_artikla = request.POST['barkod_artikla']
        naziv_artikla = request.POST['naziv_artikla']
        opis_artikla = request.POST['opis_artikla']
        proizvodac = add_proizvodac(request.POST['proizvodac'])
        zemlja_porijekla = add_zemlja_porijekla(request.POST['zemlja_porijekla'])
        vegan = True if 'vegan' in request.POST else False
        artikl_za_dodati = Artikl(
            barkod_artikla=barkod_artikla,
            naziv_artikla=naziv_artikla,
            opis_artikla=opis_artikla,
            proizvodac=proizvodac,
            zemlja_porijekla=zemlja_porijekla,
            vegan=vegan
        )
        artikl_za_dodati.save()

    return redirect(request.META['HTTP_REFERER'])


@user_passes_test(trgovac_login_required, login_url='login/')
def dodaj_proizvodace(request):
    if request.method == 'GET':
        return redirect(request.META['HTTP_REFERER'])
    form = DodajProizvodaca(request.POST)
    if form.is_valid():
        naziv_proizvodaca = request.POST['naziv']
        proizvodac_za_dodati = Proizvodac(naziv=naziv_proizvodaca)
        proizvodac_za_dodati.save()
    return redirect(request.META['HTTP_REFERER'])


@user_passes_test(trgovac_login_required, login_url='login/')
def trgovina(request, sif_trgovina):
    """
    stranica za prikaz trgovine, informacije o trgovini i proizvoda dostupnim u tim trgovinama
    slanjem HTTP GET upita na "/trgovina/<int>" gdje je <int> cijeli broj koji predstavlja unikatnu šifru baš te trgovine
    vraća se "trgovina.html" s podacije o toj trgovini
    slanjem HTTP POST upita na "/trgovina" sa informacijama o artiklu (konkretnike: barkoda artikla), artikl se može dodati u trgovinu
    """
    t = Trgovina.objects.get(sif_trgovina=sif_trgovina)
    if request.user.id != t.vlasnik.id:  # Stop "hacking" into trgovina website
        return redirect('trgovac')
    if request.method == 'GET':
        dodaj_artikl_form = DodajArtiklUTrgovinu(request.POST)
        radno_vrijeme_form = PromijeniRadnoVrijeme(request.POST)
        return render(request, 'smartCart/trgovina.html',
                      {'trgovina': t, 'artikli': get_artikli_from_trgovina(sif_trgovina),
                       'artikl_form': dodaj_artikl_form, 'vrijeme_form': radno_vrijeme_form})
    dodaj_artikl_form = DodajArtiklUTrgovinu(request.POST)
    radno_vrijeme_form = PromijeniRadnoVrijeme(request.POST)
    if dodaj_artikl_form.is_valid():
        bar_k = request.POST['artikl']
        cijena = request.POST['cijena']
        akcija = True if 'akcija' in request.POST else False
        dostupan = True if 'dostupan' in request.POST else False
        a = Artikl.objects.get(barkod_artikla=bar_k)
        try:
            old_trg_art = TrgovinaArtikli.objects.get(artikl__barkod_artikla=bar_k)
            old_trg_art.cijena = cijena
            old_trg_art.akcija = akcija
            old_trg_art.dostupan = dostupan
            old_trg_art.save()
        except TrgovinaArtikli.DoesNotExist:
            trg_art = TrgovinaArtikli(trgovina=t,
                                      artikl=a,
                                      cijena=cijena,
                                      akcija=akcija,
                                      dostupan=dostupan)
            trg_art.save()
    if radno_vrijeme_form.is_valid():
        pocetak = request.POST['radno_vrijeme_pocetak']
        kraj = request.POST['radno_vrijeme_kraj']
        t.radno_vrijeme_pocetak = pocetak
        t.radno_vrijeme_kraj = kraj
        t.save()

    return redirect(request.META['HTTP_REFERER'])


@user_passes_test(trgovac_login_required, login_url='login/')
def artikl(request, barkod_artikla):
    """
    stranica za prikaz artikla i informacija o artiklu
    slanjem HTTP GET upita na "/artikl/<int>" gdje je <int> cijeli broj koji predstavlja unikatnu šifru tog artikla
    vraća se "artikl.html" koji sadrži podatke o baš tom artiklu
    """
    a = Artikl.objects.get(barkod_artikla=barkod_artikla)
    return render(request, 'smartCart/artikl.html', {'barkod_artikla': barkod_artikla, 'artikl': a})


@user_passes_test(trgovac_login_required, login_url='login/')
def uredi_artikl_u_trgovini(request, artikl_trgovina):
    t_id = TrgovinaArtikli.objects.get(id=artikl_trgovina).trgovina.sif_trgovina
    t = Trgovina.objects.get(sif_trgovina=t_id)
    if request.user.id != t.vlasnik.id:  # Stop "hacking" into trgovina website
        return redirect('index')
    if request.method == 'GET':
        old_art = TrgovinaArtikli.objects.get(id=artikl_trgovina)
        form = UrediArtiklUTrgovini(initial={
            'cijena': old_art.cijena,
            'akcija': old_art.akcija,
            'dostupan': old_art.dostupan
        })
        return render(request, 'smartCart/artikl_u_trgovini.html',
                      {'form': form, 'trgovina': t, 'artikl': old_art.artikl.naziv_artikla})
    form = UrediArtiklUTrgovini(request.POST)
    if form.is_valid():
        cijena = request.POST['cijena']
        akcija = True if 'akcija' in request.POST else False
        dostupan = True if 'dostupan' in request.POST else False
        old_art = TrgovinaArtikli.objects.get(id=artikl_trgovina)
        old_art.cijena = cijena
        old_art.akcija = akcija
        old_art.dostupan = dostupan
        old_art.save()
    return redirect(f'/trgovina/{t_id}')


@user_passes_test(trgovac_login_required, login_url='login/')
def obrisi_artikl_u_trgovini(request, artikl_trgovina):
    t_id = TrgovinaArtikli.objects.get(id=artikl_trgovina).trgovina.sif_trgovina
    t = Trgovina.objects.get(sif_trgovina=t_id)
    if request.user.id != t.vlasnik.id:  # Stop "hacking" into trgovina website
        return redirect('index')
    if request.method == 'GET':
        old_art = TrgovinaArtikli.objects.get(id=artikl_trgovina)
        old_art.delete()
        return redirect(request.META['HTTP_REFERER'])
    return redirect(request.META['HTTP_REFERER'])


@login_required(login_url='login/')
def logout(request):
    """
    logout stranica, ne može se prikazati
    ovo je potrebno doraditi, zajedno s prikazom ostalih stranica. Treba provjeriti tko je ulogiran i tko radi što
    request.user.is_authenticated je funkcija korištena pri logiranju
    """
    logging_out(request)
    return redirect('index')


@login_required(login_url='login/')
def delete_account(request):
    if request.method == 'POST':
        request.user.delete()
        return redirect('index')


@user_passes_test(trgovac_login_required, login_url='login/')
def delete_trgovina(request, sif_trgovina):
    """
    logout stranica, ne može se prikazati
    ovo je potrebno doraditi, zajedno s prikazom ostalih stranica. Treba provjeriti tko je ulogiran i tko radi što
    request.user.is_authenticated je funkcija korištena pri logiranju
    """
    if not request.user.is_authenticated and request.user != get_vlasnik_trgovine(sif_trgovina):
        return redirect('trgovac')

    Trgovina.objects.filter(sif_trgovina=sif_trgovina).delete()

    return redirect('trgovac')


def add_proizvodac(name):
    try:
        return Proizvodac.objects.get(naziv=name)
    except Proizvodac.DoesNotExist:
        return None


def add_zemlja_porijekla(name):
    try:
        return Zemlja_porijekla.objects.get(naziv=name)
    except Zemlja_porijekla.DoesNotExist:
        return None


def get_vlasnik_trgovine(sifTrgovine):
    return Trgovina.objects.get(sif_trgovina=sifTrgovine).vlasnik


def get_artikli_from_trgovina(sifTrgovina):
    return TrgovinaArtikli.objects.filter(trgovina=sifTrgovina)
