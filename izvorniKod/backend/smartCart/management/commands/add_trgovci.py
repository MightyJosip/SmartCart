import os

from django.core.management.base import BaseCommand


from smartCart.models import BaseUserModel

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
                    podaci_o_trgovcu[0],
                    podaci_o_trgovcu[1], 
                    is_trgovac=True
                    )
                print(f"Dodan trgovac {podaci_o_trgovcu[0]}")