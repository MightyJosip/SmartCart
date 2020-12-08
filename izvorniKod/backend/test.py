import requests

url = 'http://localhost:8000/android/write_description'

x = requests.post(url, 
    json={'email': 'trgovac@fer.hr', 
        'barkod': 3850139435757,
        'sif_vrsta': '1',
        'zemlja_porijekla' : 'Bolivija',
        'sif_trgovina' : '1',
        'sif_trgovina_artikl' : 2,
        'naziv_artikla' : 'Chevosi',
        'opis_artikla' : 'Ide dobro uz pivu'
        }
        
        , timeout=2.5)


try:
    print(x.json())
except:
    print(x)