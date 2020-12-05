import os
import random

from django.core.management.base import BaseCommand

from smartCart.models import SecretCode, Uloga


class Command(BaseCommand):
    help = 'Create new secret code used for signing in of trgovac'

    def handle(self, *args, **kwargs):
        print(f"Nije još implementirano :(")