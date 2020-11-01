from django.db import models


class Proizvođač(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)

    def __str__(self):
        return f'NAZIV: {self.naziv}'

class Zemlja_porijekla(models.Model):
    naziv = models.CharField(primary_key=True, max_length=100)
    def __str__(self):
        return f'NAZIV: {self.naziv}'

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

    zemlja_porijekla = models.ForeignKey(Zemlja_porijekla, on_delete=models.SET_NULL, null=True)   #krivo jer može biti točnije. Npr. uzmite bilo koji med i pisat će "Zemlja porijeka: 5% iz hrvatske, 99.9% iz kine"
    autor_zemlje_porijekla = models.CharField(max_length=100, null=True)
    vote_count_zemlje_porijekla = models.IntegerField(null=True)

    vegan = models.CharField(max_length=2, null=True)
    
    def __str__(self):
        return f'BARKOD: {self.barkod_artikla}, NAZIV: {self.naziv_artikla}'

    # def uvjet(self):
    #     return test_nad_uvjetom

class Trgovina(models.Model):
    sifTrgovina = models.IntegerField(primary_key=True)
    nazTrgovina = models.CharField(max_length=100)
    artikli = models.ManyToManyField(Artikl)

    def __str__(self):
        return f'SIFRA TRGOVINE: {self.sifTrgovina}, NAZIV: {self.nazTrgovina}'

