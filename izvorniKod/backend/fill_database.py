import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
# os.system("cmd.exe /c chcp 1250")  <--Ovo pokrenuti na windowsu

import django
django.setup()

from django.core.management import call_command

call_command('add_countries')
call_command('add_proizvodaci')
call_command('add_products')

print("Done")
