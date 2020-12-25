import os

from django.core.management.base import BaseCommand

from smartCart.models import Artikl, Proizvodac, Zemlja_porijekla


class Command(BaseCommand):
    help = 'Adds list of artikls to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "product_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            artikli = [line for line in file[1:]]
            for artikl in artikli:
                podaci_o_artiklu = artikl.split(';')
                podaci_o_artiklu[-1] = podaci_o_artiklu[-1].rstrip()
                art = Artikl(barkod_artikla=podaci_o_artiklu[0],
                             naziv_artikla=podaci_o_artiklu[1])
                art.save()
                print(f"Dodan artikl {podaci_o_artiklu[1]}")
