import os

from django.core.management.base import BaseCommand


from smartCart.models import Uloga

class Command(BaseCommand):
    help = 'Adds list of uloge to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "uloge_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            uloge = [line for line in file[1:]]
            for uloga in uloge:
                podaci_o_ulozi = uloga.split(';')
                podaci_o_ulozi[-1] = podaci_o_ulozi[-1].rstrip()
                ul = Uloga(
                    sif_uloga=podaci_o_ulozi[0],
                    auth_level=podaci_o_ulozi[1]
                    )
                ul.save()
                print(f"Dodana uloga {podaci_o_ulozi[1]}")