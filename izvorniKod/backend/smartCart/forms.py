from django import forms
from .models import Artikl


class LoginForm(forms.Form):
    username = forms.CharField(label='User name', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class DodajTrgovinu(forms.Form):
    nazTrgovina = forms.CharField(label='Naziv trgovine', max_length=100)


class DodajArtikl(forms.ModelForm):
    class Meta:
        model = Artikl
        fields = ['barkod_artikla', 'naziv_artikla', 'opis_artikla', 'proizvođač', 'zemlja_porijekla', 'vegan']

