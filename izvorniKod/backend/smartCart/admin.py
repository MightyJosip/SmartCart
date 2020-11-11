from django.contrib import admin

# Register your models here.
from .models import Artikl, Trgovina, SecretCode, BaseUserModel, MyUserAdmin


admin.site.register(Artikl)
admin.site.register(Trgovina)
admin.site.register(SecretCode)
admin.site.register(BaseUserModel, MyUserAdmin)
