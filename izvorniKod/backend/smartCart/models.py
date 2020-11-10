from django.contrib.admin import ModelAdmin
from django.contrib.auth.base_user import BaseUserManager
from django.db import models
from django.contrib.auth.models import AbstractUser
from django.conf import settings
from django.contrib.sessions.models import Session

class AccountManager(BaseUserManager):
    use_in_migrations = True

    def create_user(self, email, password, **extra_fields):
        if not email:
            raise ValueError('The Email must be set')
        email = self.normalize_email(email)
        user = self.model(email=email, **extra_fields)
        user.set_password(password)
        user.save()
        return user

    def create_superuser(self, email, password, **extra_fields):
        extra_fields.setdefault('is_staff', True)
        extra_fields.setdefault('is_superuser', True)
        extra_fields.setdefault('is_active', True)
        extra_fields.setdefault('is_kupac', True)
        extra_fields.setdefault('is_trgovac', True)

        return self.create_user(email, password, **extra_fields)


class BaseUserModel(AbstractUser):
    username = None
    first_name = None
    last_name = None
    is_kupac = models.BooleanField(default=False)
    is_trgovac = models.BooleanField(default=False)
    email = models.EmailField(unique=True)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    objects = AccountManager()

    def __str__(self):
        return self.email


class MyUserAdmin(ModelAdmin):
    model = BaseUserModel
    list_display = ('email', 'is_staff', 'is_kupac', 'is_trgovac')
    list_filter = ()
    search_fields = ('email',)
    ordering = ('email', )
    filter_horizontal = ()


# predstavlja proizvođača u bazi podataka
# trenutno ima samo jedan atribut - naziv
# TODO: dodati još atributa
class Proizvodac(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'{self.naziv}'


# predstavlja zemlju porijekla
# trenutno ima jedan atribut - naziv
# TODO: dodaj više atributa
# ovu bi tablicu mi trebali napuniti
class Zemlja_porijekla(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'{self.naziv}'


# klasa artikl
# atribute barkod_artikla, naziv_artikla, opis_artikla, proizvođač, zemlja_porijekla i vegan dodaje korisnik
# autor atributa bi se trebao sam ispuniti
# vote count bi trebao biti postavljen na nulu
# TODO: srediti vote count atribude iz NULL prebaciti u DEFAULT = 0
class Artikl(models.Model):
    barkod_artikla = models.CharField(max_length=13, primary_key=True)

    naziv_artikla = models.CharField(max_length=100, null=False)
    autor_naziva = models.CharField(max_length=100, null=True)
    vote_count_naziva = models.IntegerField(null=True)

    opis_artikla = models.CharField(max_length=5000, null=True)
    autor_opisa = models.CharField(max_length=100, null=True)
    vote_count_opisa = models.IntegerField(null=True)
    
    proizvodac = models.ForeignKey(Proizvodac, on_delete=models.SET_NULL, null=True)       #uh, može i set default ali brate ima tu posla
    autor_proizvodaca = models.CharField(max_length=100, null=True)
    vote_count_proizvodaca = models.IntegerField(null=True)

    zemlja_porijekla = models.ForeignKey(Zemlja_porijekla, on_delete=models.SET_NULL,
                                         null=True)  # krivo jer može biti točnije. Npr. uzmite bilo koji med i pisat će "Zemlja porijeka: 5% iz hrvatske, 99.9% iz kine"
    autor_zemlje_porijekla = models.CharField(max_length=100, null=True)
    vote_count_zemlje_porijekla = models.IntegerField(null=True)

    vegan = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.naziv_artikla}'


# klasa trgovina
# sve jasno
# TODO: dodaj atribute tipa lokacija, adresa itd..
# TODO: rastaviti ovaj "many to many fields" u zasebnu klasu, dodati "dostupnost" i "cijena" kao atribute
# stvar je u tome da je N:N veza ne isparava pretvaranjem iz ER u Relacijski model
# ovdje je pak stvar da N:N veza može biti dio neke druge veze sve dok....
# nema svoje atribute.
# E al mi znamo da svaka trgovina određuje svoju cijenu za artikle koji se razlikuju od trgovine do trgovine
# Na istu foru dodaj "dostupnost"
# udari u google py django many to many relationship i otvori službenu stranicu i šamaraj
class Trgovina(models.Model):
    sif_trgovina = models.AutoField(primary_key=True)
    naz_trgovina = models.CharField(max_length=100)
    adresa_trgovina = models.CharField(max_length=200)
    radno_vrijeme_pocetak = models.TimeField()
    radno_vrijeme_kraj = models.TimeField()
    latitude = models.CharField(max_length=100, null=True)
    longitude = models.CharField(max_length=100, null=True)
    vlasnik = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)

    def __str__(self):
        return f'{self.naz_trgovina}, {self.adresa_trgovina}'


class TrgovinaArtikli(models.Model):
    trgovina = models.ForeignKey(Trgovina, on_delete=models.CASCADE)
    artikl = models.ForeignKey(Artikl, on_delete=models.CASCADE)
    cijena = models.DecimalField(max_digits=8, decimal_places=2)
    akcija = models.BooleanField(default=False)
    dostupan = models.BooleanField(default=False)


class SecretCode(models.Model):
    value = models.IntegerField(primary_key=True)

    def __str__(self):
        return f'{self.value}'


class UserSession(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    session = models.ForeignKey(Session, on_delete=models.CASCADE)  