from django.contrib.auth import logout, login, authenticate
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render, redirect
from django.views import View
from django.core.mail import send_mail

from .functions import render_template, render_form, read_form, User, redirect_to_home_page, root_dispatch, \
    must_be_logged, must_be_enabled
from ..forms import SignUpTrgovacForm, SignUpKupacForm, LoginForm, EditLogin, NovaLozinkaForm, SignUpAdminForm
from ..models import SecretCode, Uloga, BaseUserModel, PrivremenaLozinka, AbstractUser
from django.contrib.auth.hashers import *
from django.core.mail import EmailMultiAlternatives
from django.core import mail
from django.template.loader import render_to_string
from django.utils.html import strip_tags
from ..templates.smartCart import *

from django.http import HttpResponse

import random
import json

#from allauth.account.views import *


def error_404(request, exception):
    return redirect('index')


class IndexView(View):
    template_name = "smartCart/index.html"

    def get(self, request, *args, **kwargs):
        return render_template(self, request)


class SignUpTrgovacView(View):
    template_name = 'smartCart/signup_trgovac.html'
    form_class = SignUpTrgovacForm

    def __init__(self):
        super(SignUpTrgovacView, self).__init__()
        self.form = SignUpTrgovacView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            return self.check_user(request)

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def check_user(self, request):
        email = request.POST['email']
        password = request.POST['password']
        confirm_password = request.POST['confirm_password']
        secret_code = request.POST['secret_code']
        try:
            validate_email(email)
        except ValidationError:
            return render_form(self, request, message='Wrong mail format\n')
        if password != confirm_password:
            return render_form(self, request, message='Passwords don\'t match\n')
        secret_code = SecretCode.objects.filter(value=secret_code)
        if User.objects.filter(email=email).exists():
            return render_form(self, request, message='Mail already exists\n')
        if (secret_code[0].uloga.auth_level != "Trgovac"):
            return render_form(self, request, message='Wrong authorisation level\n')
        if not secret_code.exists():
            return render_form(self, request, message='Wrong secret code\n')
        else:
            secret_code.delete()
        User.objects.create_user(email, password, uloga = Uloga.objects.get(auth_level='Trgovac'))
        return render(request, 'smartCart/index.html', {})


class SignUpKupacView(View):
    template_name = 'smartCart/signup_kupac.html'
    form_class = SignUpKupacForm

    def __init__(self):
        super(SignUpKupacView, self).__init__()
        self.form = SignUpKupacView.form_class()

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def post(self, request, *args, **kwargs):
        read_form(self, request)
        if self.form.is_valid():
            return self.check_user(request)
    
    def check_user(self, request, *args, **kwargs):
        email = request.POST['email']
        password = request.POST['password']
        confirm_password = request.POST['confirm_password']
        try:
            validate_email(email)
        except ValidationError:
            return render_form(self, request, message='Wrong mail format\n')
        if password != confirm_password:
            return render_form(self, request, message='Passwords don\'t match\n')
        if User.objects.filter(email=email).exists():
            return render_form(self, request, message='Mail already exists\n')
        User.objects.create_user(email, password, uloga = Uloga.objects.get(auth_level='Kupac'))
        return render(request, 'smartCart/index.html', {})

class SignUpAdminView(View):
    template_name = 'smartCart/signup_admin.html'
    form_class = SignUpAdminForm

    def __init__(self):
        super(SignUpAdminView, self).__init__()
        self.form = SignUpAdminView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            return self.check_user(request)

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def check_user(self, request):
        email = request.POST['email']
        password = request.POST['password']
        confirm_password = request.POST['confirm_password']
        secret_code = request.POST['secret_code']
        try:
            validate_email(email)
        except ValidationError:
            return render_form(self, request, message='Wrong mail format\n')
        if password != confirm_password:
            return render_form(self, request, message='Passwords don\'t match\n')
        secret_code = SecretCode.objects.filter(value=secret_code)
        if User.objects.filter(email=email).exists():
            return render_form(self, request, message='Mail already exists\n')
        if (secret_code[0].uloga.auth_level != "Admin"):
            return render_form(self, request, message='Wrong authorisation level\n')
        if not secret_code.exists():
            return render_form(self, request, message='Wrong secret code\n')
        else:
            secret_code.delete()
        #User.objects.create_user(email, password, uloga = Uloga.objects.get(auth_level='Trgovac'))
        u = User(email=email)
        u.set_password(password)
        u.is_superuser = True
        u.is_staff = True
        u.uloga = Uloga.objects.get(auth_level='Admin')
        u.save()
        return render(request, 'smartCart/index.html', {})


class LoginView(View):
    template_name = 'smartCart/login.html'
    form_class = LoginForm

    def __init__(self):
        super(LoginView, self).__init__()
        self.form = LoginView.form_class()

    def post(self, request, *args, **kwargs):
        read_form(self, request)
        if self.form.is_valid():
            return self.check_user(request)
        return render_form(self, request, message='Invalid username or password\n')

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated:
            return redirect_to_home_page(request)
        return root_dispatch(self, request, *args, **kwargs)

    def check_user(self, request):
        email = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=email, password=password)
        if user is not None:
            if (user.omogucen == True):
                login(request, user)
                return render(request, 'smartCart/index.html', {})
            else:
                return render_form(self, request, message='You have been banned\n')
        else:
            return render_form(self, request, message='Invalid username or password\n')


class LogoutView(View):
    def dispatch(self, request, *args, **kwargs):
        logout(request)
        return redirect('index')


@must_be_logged
class EditProfileView(View):
    template_name = 'smartCart/edit_profile.html'
    form_class = EditLogin

    def __init__(self):
        super(EditProfileView, self).__init__()
        self.form = EditProfileView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            password = request.POST['password']
            confirm_password = request.POST['confirm_password']
            if password != confirm_password:
                return render_form(self, request, message='Passwords don\'t match\n')
            else:
                request.user.set_password(password)
                request.user.save()
                return redirect('index')

    def get(self, request, *args, **kwargs):
        return render_form(self, request)


@must_be_logged
class DeleteAccountView(View):
    def post(self, request, *args, **kwargs):
        request.user.delete()
        return redirect('index')

class NovaLozinkaView(View):
    template_name = 'smartCart/nova_lozinka.html'
    form_class = NovaLozinkaForm

    def __init__(self):
        super(NovaLozinkaView, self).__init__()
        self.form = NovaLozinkaForm()

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def post(self, request, *args, **kwargs):
        if(self, read_form):
            email = request.POST['email']
            password = request.POST['password']
            confirm_password = request.POST['confirm_password']
            try:
                validate_email(email)
            except ValidationError:
                return render_form(self, request, message='Wrong mail format\n')
            
            if len(BaseUserModel.objects.all().filter(email=email)) == 0:
                return render_form(self, request, message='User does not exist\n') 

            if password != confirm_password:
                return render_form(self, request, message='Passwords don\'t match\n')

            user = BaseUserModel.objects.get(email=email)

            if user.check_password(password) == True:
                return render_form(self, request, message='New password must be different than the old\n')

            #TODO: create tmp password in db
            #TODO: jedan na jedan veza? ovako nema smisla i neće raditi s tokenima
            #TODO: popravi i tokene...
            temporary_password = PrivremenaLozinka(
                user=user,
                password= make_password(password=password, hasher='default'),
                #TODO: popravi ovo
                token= random.randint(100000, 999999)
            )

            temporary_password.save()

            subject = 'Subject'
            html_message = render_to_string('smartCart/confirm_password.html', {'token': temporary_password.token, 'email': temporary_password.user.email})
            plain_message = strip_tags(html_message)
            from_email = 'smartCart app'
            to = 'smartestcart@gmail.com'

            mail.send_mail(subject, plain_message, from_email, [to], html_message=html_message)
            
        #TODO: dodati neku poruku greške, uspjeha o slanju maila?            
        return redirect('index')

class PotvrdiLozinkuView(View):
    def post(self, request, *args, **kwargs):
        token = request.POST['token']
        email = request.POST['email']
        privremena = PrivremenaLozinka.objects.get(token=token)
        hashed = privremena.password
        privremena.delete()

        b = BaseUserModel.objects.get(email=email)
        b.password = hashed
        b.save()

        #TODO: dodati malo opširniji opis?
        # ovo je primitivno ali radi
        return HttpResponse("<p>Vaša je lozinka uspješno promijenjena!</p>")



