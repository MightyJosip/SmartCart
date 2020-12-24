from django.contrib.admin import ModelAdmin
from django.contrib.auth.base_user import BaseUserManager
from django.db import models
from django.contrib.auth.models import AbstractUser
from django.conf import settings
from django.contrib.sessions.models import Session
from django.db.models import ManyToManyField


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
        user = self.model(email=email)
        user.is_superuser = True
        user.is_staff = True
        user.uloga = Uloga(sif_uloga=4)
        user.omogucen = True
        user.set_password(password)
        user.save()
        return user


class Uloga(models.Model):
    sif_uloga = models.IntegerField(primary_key=True)

    AUTH_LEVEL_CHOICES = [
        ('G', 'Gost'),
        ('K', 'Kupac'),
        ('T', 'Trgovac'),
        ('A', 'Administrator')
    ]

    auth_level = models.CharField(max_length=7, choices=AUTH_LEVEL_CHOICES, default='Gost', null=False)

    def __str__(self):
        return f'{self.sif_uloga}, {self.auth_level}'


class SecretCode(models.Model):
    value = models.IntegerField(primary_key=True)
    uloga = models.ForeignKey(Uloga, on_delete=models.CASCADE, null=True)

    class Meta():
        managed = True

    def __str__(self):
        return f'{self.value}'



class BaseUserModel(AbstractUser):

    # TODO: implementiraj da korisnik može odabrati username i prikazuj username (ili izbaci)
    username = None
    first_name = None
    last_name = None

    email = models.EmailField(unique=True)
    uloga = models.ForeignKey(Uloga, on_delete=models.CASCADE, null=True)
    onemogucio = models.ForeignKey('self', on_delete=models.CASCADE, null=True)
    omogucen = models.BooleanField(default=True)

    USERNAME_FIELD = 'email'
    REQUIRED_FIELDS = []

    objects = AccountManager()

    def get_auth_level(self):
        return self.uloga.auth_level

    def __str__(self):
        return self.email


# nepotrebno
class OnemoguceniRacun(models.Model):
    emailAdmin = models.ForeignKey(BaseUserModel, on_delete=models.CASCADE, null=True)
    datum = models.CharField(max_length=100, null=True)  # placeholder


# TODO: ovo valja urediti tako da se prikazuje auth_level
class MyUserAdmin(ModelAdmin):
    model = BaseUserModel
    list_display = ('email', 'is_staff', 'omogucen')
    list_filter = ()
    search_fields = ('email',)
    ordering = ('email',)
    filter_horizontal = ()
    actions = ['onemoguci_racune']

    def onemoguci_racune(self, request, queryset):
        queryset.update(omogucen=False)
        queryset.update(onemogucio=request.user)

    onemoguci_racune.short_description = "Onemoguci oznacene racune"


class UserSession(models.Model):
    user = models.ForeignKey(settings.AUTH_USER_MODEL, on_delete=models.CASCADE)
    session = models.OneToOneField(Session, on_delete=models.CASCADE)


class Proizvodac(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'{self.naziv}'


class Zemlja_porijekla(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'{self.naziv}'


class Artikl(models.Model):
    barkod_artikla = models.CharField(max_length=13, primary_key=True)

    #########################################
    #                                       #
    #              LEGACY SUPPORT           #
    #                                       #
    #########################################
    naziv_artikla = models.CharField(max_length=100, null=False)
    autor_naziva = models.CharField(max_length=100, null=True)
    vote_count_naziva = models.IntegerField(null=True)

    opis_artikla = models.CharField(max_length=5000, null=True)
    autor_opisa = models.CharField(max_length=100, null=True)
    vote_count_opisa = models.IntegerField(null=True)

    proizvodac = models.ForeignKey(Proizvodac, on_delete=models.SET_NULL, null=True)
    autor_proizvodaca = models.CharField(max_length=100, null=True)
    vote_count_proizvodaca = models.IntegerField(null=True)

    zemlja_porijekla = models.ForeignKey(Zemlja_porijekla, on_delete=models.SET_NULL, null=True)
    autor_zemlje_porijekla = models.CharField(max_length=100, null=True)
    vote_count_zemlje_porijekla = models.IntegerField(null=True)

    vegan = models.BooleanField(default=False)

    def __str__(self):
        return f'{self.barkod_artikla}'


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

    def __str__(self):
        return f'{self.trgovina}, {self.artikl}'


class Kategorija(models.Model):
    sif_kategorija = models.AutoField(primary_key=True)
    naz_kategorija = models.CharField(max_length=100)

    def __str__(self):
        return f'{self.sif_kategorija}, {self.naz_kategorija}'


class Potkategorija(models.Model):
    sif_potkategorija = models.AutoField(primary_key=True)
    kategorija = models.ForeignKey(Kategorija, on_delete=models.SET_NULL, null=True)
    naz_potkategorija = models.CharField(max_length=100)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['kategorija', 'sif_potkategorija'], name="constraint_1")]

    def __str__(self):
        return f'{self.sif_potkategorija}, {self.naz_potkategorija}'


class Vrsta(models.Model):
    sif_vrsta = models.AutoField(primary_key=True)
    potkategorija = models.ForeignKey(Potkategorija, on_delete=models.SET_NULL, null=True)
    naz_vrsta = models.CharField(max_length=100)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['potkategorija', 'sif_vrsta'], name="constraint_2")]

    def __str__(self):
        return f'{self.sif_vrsta}, {self.naz_vrsta}'


class OpisArtikla(models.Model):
    autor_opisa = models.ForeignKey(BaseUserModel, on_delete=models.CASCADE, null=True)
    artikl = models.ForeignKey(Artikl, on_delete=models.CASCADE, null=True)

    vrsta = models.ForeignKey(Vrsta, on_delete=models.CASCADE, null=True)
    zemlja_porijekla = models.ForeignKey(Zemlja_porijekla, on_delete=models.CASCADE, null=True)
    trgovina = models.ForeignKey(Trgovina, on_delete=models.CASCADE, null=True)
    trgovina_artikl = models.ForeignKey(TrgovinaArtikli, on_delete=models.CASCADE, null=True)

    naziv_artikla = models.CharField(max_length=100, null=False)
    opis_artikla = models.CharField(max_length=5000, null=True)
    popis_glasova = ManyToManyField(BaseUserModel, through='Glasovi', related_name='glasaci')
    broj_glasova = models.IntegerField(null=False, default=0)
    masa = models.IntegerField(null=True)

    prioritiziran = models.BooleanField(default=False)

    class Meta:
        constraints = [models.UniqueConstraint(fields=['autor_opisa', 'artikl'], name='constraint_3')]

    def __str__(self):
        return f'{self.autor_opisa}, {self.artikl}'


class Glasovi(models.Model):
    VRSTE_GLASOVA = [
        ('N', 'Nije glasao'),
        ('G', 'Goreglas'),
        ('D', 'Doljeglas'),
    ]
    user = models.ForeignKey(BaseUserModel, on_delete=models.CASCADE)
    opis_artikla = models.ForeignKey(OpisArtikla, on_delete=models.CASCADE)
    vrijednost_glasa = models.CharField(max_length=11, choices=VRSTE_GLASOVA, default='Nije glasao', null=False)

#TODO: implementiraj vremensko ograničenje
class PrivremenaLozinka(models.Model):
    user = models.ForeignKey(BaseUserModel, on_delete=models.CASCADE, null=True)
    password = models.CharField(max_length=128, null=True) 
    token = models.CharField(max_length=50 ,null=True)
    istice = models.CharField(max_length=100, null=True)  # placeholder

class DBFile(models.Model):
    name = models.CharField(max_length=500, null=False)
    data = models.BinaryField()
    uploaded_by = models.ForeignKey(BaseUserModel, on_delete=models.CASCADE, null=False)
    date = models.DateTimeField()