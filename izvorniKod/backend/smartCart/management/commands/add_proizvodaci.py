import os

from django.core.management.base import BaseCommand

from smartCart.models import Proizvodac


class Command(BaseCommand):
    help = 'Adds list of proizvođači to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "proizvodac_list.txt"), 'r', encoding='utf-8') as file:
            proizvodaci = [line for line in file]
            for proizvodac in proizvodaci:
                proizvodac = proizvodac.rstrip()
                pr = Proizvodac(naziv=proizvodac)
                pr.save()
                print(f"Dodan proizvođač {proizvodac}")
