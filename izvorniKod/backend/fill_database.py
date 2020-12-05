import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
# os.system("cmd.exe /c chcp 1250")  <--Ovo pokrenuti na windowsu

import django
django.setup()

from django.core.management import call_command


call_command('add_countries')
call_command('add_proizvodaci')
call_command('add_products')
call_command('add_kategorije')
call_command('add_potkategorije')
call_command('add_vrsta')
call_command('add_uloge')
call_command('add_trgovci')
call_command('add_trgovine')
call_command('add_trgovinaartikli')
call_command('add_opisartikla')


print("Done")
