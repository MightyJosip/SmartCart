import requests

url = 'http://localhost:8000/android/artikltrgovina'
myobj = {'somekey': 'somevalue'}

x = requests.post(url, json={'sif_trgovina': 2, 'barkod': 3850116106052}, timeout=2.5)


try:
    print(x.json())
except:
    print(x)