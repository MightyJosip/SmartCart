from django.contrib import admin

# Register your models here.
from .models import *



admin.site.register(Uloga)
admin.site.register(SecretCode)
admin.site.register(BaseUserModel, MyUserAdmin)
admin.site.register(UserSession)
admin.site.register(OnemoguceniRacun)

admin.site.register(Kategorija)
admin.site.register(Potkategorija)
admin.site.register(Vrsta)
admin.site.register(Artikl)
admin.site.register(Trgovina)
admin.site.register(TrgovinaArtikli)
admin.site.register(OpisArtikla)
admin.site.register(Zemlja_porijekla)
admin.site.register(Proizvodac)