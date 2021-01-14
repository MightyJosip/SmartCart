import requests


# url = 'http://localhost:8000/potvrdi_lozinku'
# x = requests.post(url, data={'token': 202406, 'email': 'ante@fer.hr'}, timeout=2.5)

# url = 'http://localhost:8000/android/login'
# x = requests.post(url, json={'email': 'trgovac@fer.hr', 'password': 'pwd'}, timeout=2.5)

# url = 'http://localhost:8000/android/logout'
# x = requests.post(url, json={'sessionId': 'neki session id'}, timeout=2.5)
#################

"""
url = 'http://localhost:8000/android/login'
x = requests.post(url, json={'email': 'trgovac@fer.hr', 'password': 'pwd'}, timeout=2.5)

print(x.json())
sessionId = x.json()['session_id']

url = 'http://localhost:8000/android/edit_profile'
x = requests.post(url, json={'session_id': sessionId, 'old_password': 'abc', 'new_password': 'pwd'})


url = 'http://localhost:8000/android/closeststores'
x = requests.post(url, json={'latitude': 45.690050, 'longitude': 16.025267, 'distance': 15}, timeout=2.5)



url = 'http://localhost:8000/android/artiklitrgovina'
x = requests.post(url, json={'sif_trgovina': '1'}, timeout=2.5)

url = 'http://localhost:8000/android/artikltrgovina'
x = requests.post(url, json={'sif_trgovina': '1', 'barkod': '3850104008597'}, timeout=2.5)

url = 'http://localhost:8000/android/opisi'
x = requests.post(url, json={'sif_trgovina': '1', 'barkod': '3850104008597'}, timeout=2.5)


try:
    print(x.json())
    print(len(x.json()))
    print("dan je json")
except:
    print(x)
    print("dan je x")

"""

import base64
import codecs

s = ''
with open("favicon.png", "rb") as imageFile:
    s = base64.b64encode(imageFile.read())

print(s)


fh = open("imageToSave.png", "wb")
fh.write(codecs.decode(s, 'base64'))
fh.close()
