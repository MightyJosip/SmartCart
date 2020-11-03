from django.db import models


# predstavlja proizvođača u bazi podataka
# trenutno ima samo jedan atribut - naziv
# TODO: dodati još atributa
class Proizvođač(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'NAZIV: {self.naziv}'


# predstavlja zemlju porijekla
# trenutno ima jedan atribut - naziv
# TODO: dodaj više atributa
# ovu bi tablicu mi trebali napuniti
class Zemlja_porijekla(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'NAZIV: {self.naziv}'


# klasa artikl
# atribute barkod_artikla, naziv_artikla, opis_artikla, proizvođač, zemlja_porijekla i vegan dodaje korisnik
# autor atributa bi se trebao sam ispuniti
# vote count bi trebao biti postavljen na nulu
# TODO: srediti vote count atribude iz NULL prebaciti u DEFAULT = 0
class Artikl(models.Model):
    barkod_artikla = models.IntegerField(primary_key=True)

    naziv_artikla = models.CharField(max_length=100, null=False)
    autor_naziva = models.CharField(max_length=100, null=True)
    vote_count_naziva = models.IntegerField(null=True)

    opis_artikla = models.CharField(max_length=5000, null=True)
    autor_opisa = models.CharField(max_length=100, null=True)
    vote_count_opisa = models.IntegerField(null=True)
    
    proizvođač = models.ForeignKey(Proizvođač, on_delete=models.SET_NULL, null=True)       #uh, može i set default ali brate ima tu posla
    autor_proizvođača = models.CharField(max_length=100, null=True)
    vote_count_proizvođača = models.IntegerField(null=True)

    zemlja_porijekla = models.ForeignKey(Zemlja_porijekla, on_delete=models.SET_NULL,
                                         null=True)  # krivo jer može biti točnije. Npr. uzmite bilo koji med i pisat će "Zemlja porijeka: 5% iz hrvatske, 99.9% iz kine"
    autor_zemlje_porijekla = models.CharField(max_length=100, null=True)
    vote_count_zemlje_porijekla = models.IntegerField(null=True)

    vegan = models.CharField(max_length=2, null=True)

    def __str__(self):
        return f'BARKOD: {self.barkod_artikla}, NAZIV: {self.naziv_artikla}'

    # def uvjet(self):
    #     return test_nad_uvjetom


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
    sifTrgovina = models.IntegerField(primary_key=True)
    nazTrgovina = models.CharField(max_length=100)
    artikli = models.ManyToManyField(Artikl)

    def __str__(self):
        return f'SIFRA TRGOVINE: {self.sifTrgovina}, NAZIV: {self.nazTrgovina}'


class SecretCode(models.Model):
    value = models.IntegerField(primary_key=True)

    def __str__(self):
        return f'SECRET CODE: {self.value}'
