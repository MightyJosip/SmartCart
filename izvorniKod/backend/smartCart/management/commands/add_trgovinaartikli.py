import os

from django.core.management.base import BaseCommand


from smartCart.models import Trgovina, Artikl, TrgovinaArtikli

class Command(BaseCommand):
    help = 'Adds list of trgovinaArtikli to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "trgovinaartikli_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            trgovineartikli = [line for line in file[1:]]
            for trgovinaartikl in trgovineartikli:
                podaci_o_trgoviniartiklu = trgovinaartikl.split(';')
                podaci_o_trgoviniartiklu[-1] = podaci_o_trgoviniartiklu[-1].rstrip()

                tr = TrgovinaArtikli(
                    trgovina = Trgovina.objects.get(sif_trgovina=podaci_o_trgoviniartiklu[0]),
                    artikl = Artikl.objects.get(barkod_artikla=podaci_o_trgoviniartiklu[1]),
                    cijena = podaci_o_trgoviniartiklu[2]
                    )
                tr.save()
                print(f"Dodan artikl u trgovinu")