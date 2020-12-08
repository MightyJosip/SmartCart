import requests

url = 'http://localhost:8000/android/opisi'
myobj = {'somekey': 'somevalue'}

x = requests.post(url, json={'id': 1}, timeout=2.5)


try:
    print(x.json())
except:
    print(x)