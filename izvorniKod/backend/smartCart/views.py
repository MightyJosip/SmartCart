from django.shortcuts import render
from django.http import HttpResponse

from django.contrib.auth.hashers import *
from django.contrib.auth.models import User

def index(request):
    return render(request, 'smartCart/index.html', {})

def login(request):
    username = request.POST['username']
    password = request.POST['password']
    print(request.POST['username'])
    print(request.POST['password'])

    users = User.objects.values()

    print(users)

    for user in users:
        print('-----------------------------')
        print(user)
        print('-----------------------------')

        print(user['username'])
        if (user['username'] == username):
            if(check_password(request.POST['password'], user['password'])):
                print(user['username'])
                return render(request, 'smartCart/trgovac.html', {})
    
  
    #print(check_password(request.POST['password'], users[2]['password']))
    

    return render(request, 'smartCart/index.html', {})

def trgovac(request):
    return render(request, 'smartCart/trgovac.html', {})