import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")

import django
django.setup()

from django.core.management import call_command

call_command('generate_secret_code_admin')