import os
import random

from django.core.management.base import BaseCommand

from smartCart.models import SecretCode


class Command(BaseCommand):
    help = 'Create new secret code used for signing in of trgovac'

    def handle(self, *args, **kwargs):
        code = random.randint(100000, 999999)
        while SecretCode.objects.filter(value=code).exists():
            code = random.randint(100000, 999999)
        new_secret = SecretCode(value=code)
        new_secret.save()
        print(f"Generated: {code}")
