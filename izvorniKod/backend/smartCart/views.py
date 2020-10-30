from django.shortcuts import render
from django.http import HttpResponse

from django.contrib.auth.hashers import *
from django.contrib.auth.models import User

def index(request):
    return render(request, 'smartCart/index.html', {})

def login(request):
    username = request.POST['username']
    password = request.POST['password']

    users = User.objects.values()

    for user in users:
        if (user['username'] == username and check_password(password, user['password'])):
                print(request.session)
                print("safjkshajs")
                request.session.username = username
                print(request.session.username)
                return render(request, 'smartCart/trgovac.html', {'username': request.session.username})
        

    return render(request, 'smartCart/index.html', {})

def trgovac(request):
    return render(request, 'smartCart/trgovac.html', {})