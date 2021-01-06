import os

os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
import django

django.setup()
from django.core.management import call_command

try:
    call_command('makemigrations')
    migrations = sorted([f for f in os.listdir(os.path.join(os.path.dirname(__file__), 'smartCart/migrations')) if
                  os.path.isfile(os.path.join(os.path.join(os.path.dirname(__file__), 'smartCart/migrations'), f)) and f.startswith('0')])
    call_command('sqlmigrate', 'smartCart', migrations[-1][:4])
except FileNotFoundError:
    call_command('makemigrations', 'smartCart')
    migrations = sorted([f for f in os.listdir(os.path.join(os.path.dirname(__file__), 'smartCart/migrations')) if
                         os.path.isfile(os.path.join(os.path.join(os.path.dirname(__file__), 'smartCart/migrations'),
                                                     f)) and f.startswith('0')])
    call_command('sqlmigrate', 'smartCart', migrations[-1][:4])
call_command('migrate')
