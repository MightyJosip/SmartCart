import os

from django.core.management.base import BaseCommand

from smartCart.models import Potkategorija, Vrsta


class Command(BaseCommand):
    help = 'Adds list of vrsta to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "vrsta_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            vrste = [line for line in file[1:]]
            for vrsta in vrste:
                podaci_o_vrsti = vrsta.split(';')
                podaci_o_vrsti[-1] = podaci_o_vrsti[-1].rstrip()
                vr = Vrsta(
                    sif_vrsta=podaci_o_vrsti[0],
                    sif_potkategorija=Potkategorija.objects.get(sif_potkategorija=podaci_o_vrsti[1]),
                    naz_vrsta=podaci_o_vrsti[2]
                    )
                vr.save()
                print(f"Dodana vrsta {podaci_o_vrsti[2]}")