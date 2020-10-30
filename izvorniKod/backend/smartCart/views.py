from django.shortcuts import render
from django.http import HttpResponse
#from django.contrib.auth.hashers import *
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login as logging_in, logout as logging_out
from django.shortcuts import redirect
from django.urls import reverse

from smartCart.models import Trgovina

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
            #------
            return render(request, 'smartCart/trgovac.html', {'trgovine': trgovine})
        else:
            return render(request, 'smartCart/index.html', {})

    #return the same page and avoid js on client side..

    sifTrgovina = request.POST['sifTrgovina']
    nazTrgovina = request.POST['nazTrgovina']

    print(sifTrgovina + ' ' + nazTrgovina)

    trgovina = Trgovina(sifTrgovina=sifTrgovina, nazTrgovina=nazTrgovina)
    trgovina.save()

    return redirect(request.META['HTTP_REFERER'])



# logout page, cannot be rendered
def logout(request):
    print("u logoutu")
    logging_out(request)
    return render(request, 'smartCart/index.html', {})