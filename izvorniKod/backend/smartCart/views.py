from django.shortcuts import render
from django.http import HttpResponse
#from django.contrib.auth.hashers import *
from django.contrib.auth.models import User
from django.contrib.auth import authenticate, login as logging_in, logout as logging_out
from django.shortcuts import redirect

# entry point
def index(request):
    return render(request, 'smartCart/index.html', {})

# login page
def login(request):
    if(request.method == 'GET'):
        return render(request, 'smartCart/login.html', {})

    username = request.POST['username']
    password = request.POST['password']
    user = authenticate(request, username=username, password=password)

    if (user is not None):
        logging_in(request, user)
        return render(request, 'smartCart/trgovac.html', {'username' : username})
    else:
        return render(request, 'smartCart/login.html', {})

# trgovac page
def trgovac(request):
    if(request.user.is_authenticated):
        return render(request, 'smartCart/trgovac.html', {})
    else:
        return render(request, 'smartCart/index.html', {})

# logout page, cannot be rendered
def logout(request):
    logging_out(request)
    return render(request, 'smartCart/index.html', {})