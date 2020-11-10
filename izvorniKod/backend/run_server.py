import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
# os.system("cmd.exe /c chcp 1250")  <--Ovo pokrenuti na windowsu

import django
django.setup()

from django.core.management import call_command

SERVER_IP = ''
SERVER_PORT = ''

if not SERVER_IP:
    SERVER_IP = '127.0.0.1'
if not SERVER_PORT:
    SERVER_PORT = '8000'

call_command('makemigrations')
call_command('runserver', f"{SERVER_IP}:{SERVER_PORT}")
