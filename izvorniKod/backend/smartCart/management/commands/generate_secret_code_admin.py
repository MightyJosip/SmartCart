import os
import random

from django.core.management.base import BaseCommand

from smartCart.models import SecretCode, Uloga


class Command(BaseCommand):
    help = 'Create new secret code used for signing in of admin'

    def handle(self, *args, **kwargs):
        code = random.randint(100000, 999999)
        while SecretCode.objects.filter(value=code).exists():
            code = random.randint(100000, 999999)
        new_secret = SecretCode(
            value=code,
            uloga=Uloga.objects.get(auth_level='Admin')
            )
        new_secret.save()
        print(f"Generated: {code}")
