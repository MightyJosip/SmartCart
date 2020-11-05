from django import forms
from django.forms import TimeInput

from .models import Artikl, Trgovina, TrgovinaArtikli, Proizvođač


class LoginForm(forms.Form):
    username = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class EditLogin(forms.Form):
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class SignUpTrgovacForm(forms.Form):
    email = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    secret_code = forms.IntegerField(label='Secret code')


class SignUpKupacForm(forms.Form):
    email = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)


class DodajTrgovinu(forms.ModelForm):
    class Meta:
        model = Trgovina
        fields = ['nazTrgovina', 'adresaTrgovina', 'radno_vrijeme_početak', 'radno_vrijeme_kraj']
        labels = {
            'nazTrgovina': 'Naziv trgovine',
            'adresaTrgovina': 'Adresa trgovine'
        }
        widgets = {
            'radno_vrijeme_početak': TimeInput(format='%H:%M'),
            'radno_vrijeme_kraj': TimeInput(format='%H:%M')
        }


class DodajArtikl(forms.ModelForm):
    class Meta:
        model = Artikl
        fields = ['barkod_artikla', 'naziv_artikla', 'opis_artikla', 'proizvođač', 'zemlja_porijekla', 'vegan']
        labels = {
            'vegan': 'Veganski proizvod',
        }


class DodajProizvođača(forms.ModelForm):
    class Meta:
        model = Proizvođač
        fields = ['naziv']


class DodajArtiklUTrgovinu(forms.ModelForm):
    class Meta:
        model = TrgovinaArtikli
        fields = ['artikl', 'cijena', 'akcija', 'dostupan']


class UrediArtiklUTrgovini(forms.ModelForm):
    class Meta:
        model = TrgovinaArtikli
        fields = ['cijena', 'akcija', 'dostupan']


class PromijeniRadnoVrijeme(forms.ModelForm):
    class Meta:
        model = Trgovina
        fields = ['radno_vrijeme_početak', 'radno_vrijeme_kraj']