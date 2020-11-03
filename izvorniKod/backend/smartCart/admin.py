from django.contrib import admin

# Register your models here.
from .models import Artikl
from .models import Trgovina
from .models import SecretCode

admin.site.register(Artikl)
admin.site.register(Trgovina)
admin.site.register(SecretCode)
