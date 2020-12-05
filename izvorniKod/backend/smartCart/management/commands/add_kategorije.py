import os

from django.core.management.base import BaseCommand

from smartCart.models import Kategorija


class Command(BaseCommand):
    help = 'Adds list of kategorije to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "kategorije_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            kategorije = [line for line in file[1:]]
            for kategorija in kategorije:
                podaci_o_kategoriji = kategorija.split(';')
                podaci_o_kategoriji[-1] = podaci_o_kategoriji[-1].rstrip()
                kat = Kategorija(sif_kategorija=podaci_o_kategoriji[0], naz_kategorija=podaci_o_kategoriji[1])
                kat.save()
                print(f"Dodana kategorija {podaci_o_kategoriji[1]}")