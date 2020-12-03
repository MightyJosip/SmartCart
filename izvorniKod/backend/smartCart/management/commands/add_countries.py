import os

from django.core.management.base import BaseCommand

from smartCart.models import Zemlja_porijekla


class Command(BaseCommand):
    help = 'Adds list of world countries to the database'

    def handle(self, *args, **kwargs):
        with open(os.path.join(os.path.dirname(__file__), "countries_list.txt"), 'r', encoding='utf-8') as file:
            countries = [line.rstrip()[3:] for line in file]
            for country in countries:
                zp = Zemlja_porijekla(naziv=country)
                zp.save()
                print(f"Dodana država {country}")
