from django.shortcuts import render
from django.http import HttpResponse




def index(request):
    return render(request, 'smartCart/index.html', {})

def login(request):

    return