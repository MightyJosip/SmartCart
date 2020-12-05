import os

from django.core.management.base import BaseCommand


from smartCart.models import Trgovina, Artikl, TrgovinaArtikli, Vrsta, OpisArtikla, BaseUserModel, Zemlja_porijekla

class Command(BaseCommand):
    help = 'Adds list of opisArtikla to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "opisartikla_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            opisiArtikala = [line for line in file[1:]]
            for opisArtikla in opisiArtikala:
                podaci_o_opisu = opisArtikla.split(';')
                podaci_o_opisu[-1] = podaci_o_opisu[-1].rstrip()
                #print(podaci_o_opisu[0])
                op = OpisArtikla(
                    autor_opisa= BaseUserModel.objects.get(email=podaci_o_opisu[0]),
                    artikl= Artikl.objects.get(barkod_artikla=podaci_o_opisu[1]),

                    vrsta= Vrsta.objects.get(sif_vrsta=podaci_o_opisu[2]),
                    zemlja_porijekla= Zemlja_porijekla.objects.get(naziv=podaci_o_opisu[3]),
                    trgovina= Trgovina.objects.get(sif_trgovina=podaci_o_opisu[4]),
                    trgovina_artikl= TrgovinaArtikli.objects.get(trgovina=podaci_o_opisu[4], artikl=podaci_o_opisu[1]),

                    naziv_artikla=podaci_o_opisu[6],
                    opis_artikla=podaci_o_opisu[7],
                    broj_glasova=podaci_o_opisu[8],
                    masa=podaci_o_opisu[9]

                    )
                op.save()
                print(f"Dodan opis artikla u trgovinu")