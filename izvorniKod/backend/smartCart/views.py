from django.shortcuts import render
from django.http import HttpResponse

from django.contrib.auth.hashers import *
from django.contrib.auth.models import User

from django.contrib.auth import authenticate, login as logging_in, logout as logging_out

def index(request):
    return render(request, 'smartCart/index.html', {})

def login(request):

    if(request.method == 'GET'):
        return render(request, 'smartCart/login.html', {})

    
    username = request.POST['username']
    password = request.POST['password']

    user = authenticate(request, username=username, password=password)

    if (user is not None):
        logging_in(request, user)
        return render(request, 'smartCart/trgovac.html', {})
    else:
        return render(request, 'smartCart/login.html', {})
    """
    users = User.objects.values()
    for user in users:
        if (user['username'] == username and check_password(password, user['password'])):
                print(request.session)
                request.session.username = username
                print(request.session.username)
                return render(request, 'smartCart/trgovac.html', {'username': request.session.username})
    """

def trgovac(request):
    if(request.user.is_authenticated):
        return render(request, 'smartCart/trgovac.html', {})
    else:
        return render(request, 'smartCart/index.html', {})

def logout(request):
    logging_out(request)
    return render(request, 'smartCart/index.html', {})