import os

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
import django

django.setup()
from django.core.management import call_command

call_command('makemigrations')
migrations = [f for f in os.listdir(os.path.join(os.path.dirname(__file__), 'smartCart/migrations')) if
              os.path.isfile(os.path.join(os.path.join(os.path.dirname(__file__), 'smartCart/migrations'), f))]
call_command('sqlmigrate', 'smartCart', '0001')
call_command('migrate')
