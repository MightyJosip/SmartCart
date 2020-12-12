import requests

url = 'http://localhost:8000/potvrdi_lozinku'


x = requests.post(url, data={'token': 200245, 'email': 'ante@fer.hr'}, timeout=2.5)


try:
    print(x.json())
except:
    print(x)