import requests


# url = 'http://localhost:8000/potvrdi_lozinku'
# x = requests.post(url, data={'token': 202406, 'email': 'ante@fer.hr'}, timeout=2.5)

# url = 'http://localhost:8000/android/login'
# x = requests.post(url, json={'email': 'trgovac@fer.hr', 'password': 'pwd'}, timeout=2.5)

# url = 'http://localhost:8000/android/logout'
# x = requests.post(url, json={'sessionId': 'neki session id'}, timeout=2.5)

url = 'http://localhost:8000/android/artiklitrgovina'
x = requests.post(url, json={'sif_trgovina': '1'}, timeout=2.5)

try:
    print(x.json())
    print("dan je json")
except:
    print(x)
    print("dan je x")