from django import forms
from django.forms import TimeInput

from .models import Artikl, Trgovina, TrgovinaArtikli, Proizvodac


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


# TODO: proširiti formu za latitude i logitude
class DodajTrgovinu(forms.ModelForm):
    class Meta:
        model = Trgovina
        fields = ['naz_trgovina', 'adresa_trgovina', 'radno_vrijeme_pocetak', 'radno_vrijeme_kraj', 'latitude', 'longitude']
        labels = {
            'naz_trgovina': 'Naziv trgovine',
            'adresa_trgovina': 'Adresa trgovine',
            'radno_vrijeme_pocetak': 'Radno vrijeme početak',
            'latitude' : 'Latitude',
            'longitude' : 'Longitude'
        }
        widgets = {
            'radno_vrijeme_pocetak': TimeInput(format='%H:%M'),
            'radno_vrijeme_kraj': TimeInput(format='%H:%M')
        }


class DodajArtikl(forms.ModelForm):
    class Meta:
        model = Artikl
        fields = ['barkod_artikla', 'naziv_artikla', 'opis_artikla', 'proizvodac', 'zemlja_porijekla', 'vegan']
        labels = {
            'proizvodac': 'Proizvođač',
            'vegan': 'Veganski proizvod'
        }


class DodajProizvodaca(forms.ModelForm):
    class Meta:
        model = Proizvodac
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
        fields = ['radno_vrijeme_pocetak', 'radno_vrijeme_kraj']
        labels = {
            'radno_vrijeme_pocetak': 'Radno vrijeme početak',
        }

class PromijeniLongLat(forms.ModelForm):
    class Meta:
        model = Trgovina
        fields = ['longitude', 'latitude']
