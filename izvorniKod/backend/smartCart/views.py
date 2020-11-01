from django.shortcuts import render
from django.http import HttpResponse
#from django.contrib.auth.hashers import *
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login as logging_in, logout as logging_out
from django.shortcuts import redirect
from django.urls import reverse

from smartCart.models import Trgovina, Artikl

# entry point
def index(request):
    print("u indexu")
    return render(request, 'smartCart/index.html', {})

# login page
def login(request):
    print("u loginu")
    if(request.method == 'GET'):
        return render(request, 'smartCart/login.html', {})

    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(request, username=username, password=password)

    if (user is not None):
        logging_in(request, user)
        return redirect('trgovac')
        #return render(request, 'smartCart/trgovac.html', {'username' : username})
    else:
        return render(request, 'smartCart/login.html', {})

# trgovac page
def trgovac(request):
    print("u trgovcu")
    if (request.method == 'GET'):
        if(request.user.is_authenticated):
            #------
            trgovine = list(Trgovina.objects.all())
            artikli = list(Artikl.objects.all())
            #------
            return render(request, 'smartCart/trgovac.html', {'trgovine': trgovine, 'artikli': artikli})
        else:
            return render(request, 'smartCart/index.html', {})

    #return the same page and avoid js on client side..

    sifTrgovina = request.POST['sifTrgovina']   #bespotrebno, sad postoji dodaj_trgovine!
    nazTrgovina = request.POST['nazTrgovina']

    print(sifTrgovina + ' ' + nazTrgovina)

    trgovina = Trgovina(sifTrgovina=sifTrgovina, nazTrgovina=nazTrgovina)
    trgovina.save()

    return redirect(request.META['HTTP_REFERER'])

# adding trgovine
def dodaj_trgovine(request):
    if (request.method == 'GET'):
        return redirect(request.META['HTTP_REFERER'])

    sifTrgovina = request.POST['sifTrgovina']
    nazTrgovina = request.POST['nazTrgovina']

    print(sifTrgovina + ' ' + nazTrgovina)

    trgovina = Trgovina(sifTrgovina=sifTrgovina, nazTrgovina=nazTrgovina)
    trgovina.save()

    return redirect(request.META['HTTP_REFERER'])

# adding artikli
def dodaj_artikle(request):
    if (request.method == 'GET'):
        return redirect(request.META['HTTP_REFERER'])

    barkod_artikla = request.POST['barkod_artikla']
    naziv_artikla = request.POST['naziv_artikla']
    opis_artikla = request.POST['opis_artikla']
    proizvođač = request.POST['proizvođač']
    zemlja_porijekla = request.POST['zemlja_porijekla']
    vegan = request.POST['vegan']

    if proizvođač == '':    #potrebno je nabaciti masovnu validaciju ulaznih podataka,
        proizvođač = None
    
    if zemlja_porijekla == '':
        zemlja_porijekla = None

    if opis_artikla == '':
        opis_artikla = None

    if vegan == '':
        vegan = None

    artikl = Artikl(
        naziv_artikla=naziv_artikla, 
        barkod_artikla=barkod_artikla,
        opis_artikla=opis_artikla,
        proizvođač=proizvođač,
        zemlja_porijekla=zemlja_porijekla,
        vegan=vegan
    )
    artikl.save()

    return redirect(request.META['HTTP_REFERER'])

# webpage for each trgovina
def trgovina(request, sifTrgovina):
    if (request.method == 'POST'):
        print("uuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuuu")
        bar_k = request.POST['barkod']
        sif_t = request.POST['sifTrgovina']
        print(bar_k + ' ' + sif_t)
        t = Trgovina.objects.get(sifTrgovina=sifTrgovina)
        a = Artikl.objects.get(barkod_artikla=bar_k)

        t.artikli.add(a) #odbija poslušnost
        t.save()
        print(t)       
        print(a)
        print("--------")
        print(t.artikli.all())
        return redirect(request.META['HTTP_REFERER'])

    t = Trgovina.objects.get(sifTrgovina=sifTrgovina)
    print(t)
    print(t.artikli.all())
    #print(t.artikli.all()[0].barkod_artikla)
    return render(request, 'smartCart/trgovina.html', {'sifTrgovina': sifTrgovina, 'nazTrgovina': t.nazTrgovina, 'artikli': t.artikli.all()})

def artikl(request, barkod_artikla):
    a = Artikl.objects.get(barkod_artikla=barkod_artikla)
    return render(request, 'smartCart/artikl.html', {'barkod_artikla': barkod_artikla, 'artikl': a})

# logout page, cannot be rendered
def logout(request):
    print("u logoutu")
    logging_out(request)
    return render(request, 'smartCart/index.html', {})