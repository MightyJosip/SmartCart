import os

from django.core.management.base import BaseCommand


from smartCart.models import Trgovina, BaseUserModel

class Command(BaseCommand):
    help = 'Adds list of trgovine to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "trgovine_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            trgovine = [line for line in file[1:]]
            for trgovina in trgovine:
                podaci_o_trgovini = trgovina.split(';')
                podaci_o_trgovini[-1] = podaci_o_trgovini[-1].rstrip()
                tr = Trgovina(
                    sif_trgovina=podaci_o_trgovini[0],
                    naz_trgovina=podaci_o_trgovini[1],
                    adresa_trgovina=podaci_o_trgovini[2],
                    radno_vrijeme_pocetak=podaci_o_trgovini[3],
                    radno_vrijeme_kraj=podaci_o_trgovini[4],
                    latitude=podaci_o_trgovini[5],
                    longitude=podaci_o_trgovini[6],
                    vlasnik= BaseUserModel.objects.get(email=podaci_o_trgovini[7])
                    )
                tr.save()
                print(f"Dodana trgovina {podaci_o_trgovini[1]}")