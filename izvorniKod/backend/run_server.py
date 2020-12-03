import os
os.environ.setdefault("DJANGO_SETTINGS_MODULE", "konfiguracija.settings")
# os.system("cmd.exe /c chcp 1250")  <--Ovo pokrenuti na windowsu

import django
django.setup()

from django.core.management import call_command

CONST = {}
CONST_PATH = os.path.join(os.path.dirname(__file__), "constants.txt")
if os.path.exists(CONST_PATH):
    with open(CONST_PATH, 'r', encoding='utf-8') as file:
        file = file.readlines()
        for line in file:
            line = line.rstrip().split("=")
            CONST[line[0]] = line[1]
else:
    CONST['SERVER_IP'] = os.environ['SERVER_IP']
    CONST['SERVER_PORT'] = os.environ['SERVER_PORT']

SERVER_IP = CONST['SERVER_IP']
SERVER_PORT = CONST['SERVER_PORT']

if not SERVER_IP:
    SERVER_IP = '127.0.0.1'
if not SERVER_PORT:
    SERVER_PORT = '8000'

call_command('makemigrations')
call_command('runserver', f"{SERVER_IP}:{SERVER_PORT}")
