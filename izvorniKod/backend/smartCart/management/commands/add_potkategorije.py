import os

from django.core.management.base import BaseCommand

from smartCart.models import Potkategorija, Kategorija


class Command(BaseCommand):
    help = 'Adds list of artikls to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "potkategorije_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            potkategorije = [line for line in file[1:]]
            for potkategorija in potkategorije:
                podaci_o_potkategoriji = potkategorija.split(';')
                podaci_o_potkategoriji[-1] = podaci_o_potkategoriji[-1].rstrip()
                potkat = Potkategorija(
                    sif_potkategorija=podaci_o_potkategoriji[0],
                    kategorija=Kategorija.objects.get(sif_kategorija=podaci_o_potkategoriji[1]),
                    naz_potkategorija=podaci_o_potkategoriji[2]
                    )
                potkat.save()
                print(f"Dodana potkategorija {podaci_o_potkategoriji[2]}")