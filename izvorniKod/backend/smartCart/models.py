from django.db import models


class Artikl(models.Model):
    barkod_artikla = models.IntegerField(primary_key=True)
    naziv_artikla = models.CharField(max_length=100)

    def __str__(self):
        return f'BARKOD: {self.barkod_artikla}, NAZIV: {self.naziv_artikla}'

    # def uvjet(self):
    #     return test_nad_uvjetom
