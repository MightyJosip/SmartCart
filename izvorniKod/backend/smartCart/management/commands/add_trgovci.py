import os

from django.core.management.base import BaseCommand


from smartCart.models import BaseUserModel, Uloga

class Command(BaseCommand):
    help = 'Adds list of trgovci to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "trgovci_list.txt"), 'r', encoding='utf-8') as file:
            file = file.readlines()
            trgovci = [line for line in file[1:]]
            for trgovac in trgovci:
                podaci_o_trgovcu = trgovac.split(';')
                podaci_o_trgovcu[-1] = podaci_o_trgovcu[-1].rstrip()
                BaseUserModel.objects.create_user(
                    email= podaci_o_trgovcu[0],
                    password= podaci_o_trgovcu[1],
                    uloga= Uloga.objects.get(auth_level=podaci_o_trgovcu[2])
                    )
                print(f"Dodan trgovac {podaci_o_trgovcu[0]}")