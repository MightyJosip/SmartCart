from django.shortcuts import render
from django.http import HttpResponse

from django.contrib.auth.hashers import *
from django.contrib.auth.models import User

def index(request):
    return render(request, 'smartCart/index.html', {})

def login(request):
    print(request.POST['username'])
    print(request.POST['password'])

    users = User.objects.values()
  
    print(check_password(request.POST['password'], users[2]['password']))
    

    return render(request, 'smartCart/index.html', {})