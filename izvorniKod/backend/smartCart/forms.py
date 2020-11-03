from django import forms
from .models import Artikl


class LoginForm(forms.Form):
    username = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class SignUpTrgovacForm(forms.Form):
    email = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    secret_code = forms.IntegerField(label='Secret code')


class SignUpKupacForm(forms.Form):
    email = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class DodajTrgovinu(forms.Form):
    nazTrgovina = forms.CharField(label='Naziv trgovine', max_length=100)
    adresaTrgovina = forms.CharField(label='Adresa trgovine', max_length=200)


class DodajArtikl(forms.ModelForm):
    class Meta:
        model = Artikl
        fields = ['barkod_artikla', 'naziv_artikla', 'opis_artikla', 'proizvođač', 'zemlja_porijekla', 'vegan']

