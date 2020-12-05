from django.contrib.auth import logout, login, authenticate
from django.core.exceptions import ValidationError
from django.core.validators import validate_email
from django.shortcuts import render, redirect
from django.views import View

from .functions import render_template, render_form, read_form, User, redirect_to_home_page, root_dispatch, \
    must_be_logged
from ..forms import SignUpTrgovacForm, SignUpKupacForm, LoginForm, EditLogin
from ..models import SecretCode


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
        if not secret_code.exists():
            return render_form(self, request, message='Wrong secret code\n')
        else:
            secret_code.delete()
        User.objects.create_user(email, password, is_trgovac=True)
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
        User.objects.create_user(email, password, is_kupac=True)
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

    def get(self, request, *args, **kwargs):
        return render_form(self, request)

    def dispatch(self, request, *args, **kwargs):
        if request.user.is_authenticated:
            return redirect_to_home_page(request)
        return root_dispatch(self, request, *args, **kwargs)

    def check_user(self, request):
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(request, username=username, password=password)
        if user is not None:
            login(request, user)
            return redirect_to_home_page(request)
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
