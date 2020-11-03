from django.contrib import admin

# Register your models here.
from django.contrib.auth.admin import UserAdmin

from .models import Artikl, Trgovina, SecretCode, BaseUserModel


admin.site.register(Artikl)
admin.site.register(Trgovina)
admin.site.register(SecretCode)
admin.site.register(BaseUserModel, UserAdmin)
