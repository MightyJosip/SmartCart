from django import forms

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

class SignUpAdminForm(forms.Form):
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
        fields = ['naz_trgovina', 'adresa_trgovina', 'radno_vrijeme_pocetak', 'radno_vrijeme_kraj', 'latitude',
                  'longitude']
        labels = {
            'naz_trgovina': 'Naziv trgovine',
            'adresa_trgovina': 'Adresa trgovine',
            'radno_vrijeme_pocetak': 'Radno vrijeme početak',
            'latitude': 'Latitude',
            'longitude': 'Longitude'
        }
        widgets = {
            'radno_vrijeme_pocetak': forms.TimeInput(attrs={'type': 'time'}, format='%H:%M'),
            'radno_vrijeme_kraj': forms.TimeInput(attrs={'type': 'time'}, format='%H:%M')
        }


class DodajArtikl(forms.ModelForm):
    class Meta:
        model = Artikl
        fields = ['barkod_artikla']



class DodajProizvodaca(forms.ModelForm):
    class Meta:
        model = Proizvodac
        fields = ['naziv']


class DodajArtiklUTrgovinu(forms.Form):
    artikl = forms.ModelChoiceField(queryset=Artikl.objects.all())
    cijena = forms.DecimalField(max_digits=8, decimal_places=2)
    akcija = forms.BooleanField(required=False)
    dostupan = forms.BooleanField(required=False)


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
        widgets = {
            'radno_vrijeme_pocetak': forms.TimeInput(attrs={'type': 'time'}, format='%H:%M'),
            'radno_vrijeme_kraj': forms.TimeInput(attrs={'type': 'time'}, format='%H:%M')
        }


class PromijeniLongLat(forms.ModelForm):
    class Meta:
        model = Trgovina
        fields = ['longitude', 'latitude']


# dodaj id da se zna tko je tko
class PromijeniPrioritet(forms.Form):
    prioritiziran = forms.BooleanField(required=False)

class UploadFileForm(forms.Form):
    title = forms.CharField(max_length=100)
    file = forms.FileField()

class NovaLozinkaForm(forms.Form):
    email = forms.CharField(label='E-mail', max_length=100)
    password = forms.CharField(max_length=32, widget=forms.PasswordInput)
    confirm_password = forms.CharField(max_length=32, widget=forms.PasswordInput)
