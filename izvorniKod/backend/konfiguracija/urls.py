"""konfiguracija URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""

from django.contrib import admin
from django.urls import include, path
from smartCart import views

urlpatterns = [
    path('smartcart/', include('smartCart.urls')),
    path('admin', admin.site.urls),
    path('', views.index, name = 'index'),
    path('login/', views.login, name = 'login'),
    path('trgovac', views.trgovac, name='trgovac'), ############<-------------------------------------------------
    path('logout', views.logout, name='logout'),
    path('trgovac/dodaj-trgovine', views.dodaj_trgovine, name='dodaj_trgovine'),
    path('trgovac/dodaj-artikle', views.dodaj_artikle, name='dodaj_artikle'),
    path('trgovina/<int:sifTrgovina>', views.trgovina, name='trgovina'), #int kinda sus
    path('artikl/<int:barkod_artikla>', views.artikl, name='artikl')
]
