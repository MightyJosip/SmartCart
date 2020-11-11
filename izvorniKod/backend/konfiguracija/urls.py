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
    # path('smartcart/', include('smartCart.urls')),
    path('admin', admin.site.urls),
    path('', views.index, name='index'),
    path('signup/trgovac', views.sign_up_trgovac, name='signup_trgovac'),
    path('signup/kupac', views.sign_up_kupac, name='signup_kupac'),
    path('login', views.login, name='login'), #bilo je login/, sad je login
    path('edit_profile', views.edit_profile, name='edit_profile'),
    path('delete_account', views.delete_account, name='delete_account'),
    path('trgovac', views.trgovac, name='trgovac'),  ############<-------------------------------------------------
    path('logout', views.logout, name='logout'),
    path('trgovac/dodaj-trgovine', views.dodaj_trgovine, name='dodaj_trgovine'),
    path('trgovac/dodaj-artikle', views.dodaj_artikle, name='dodaj_artikle'),
    path('trgovac/dodaj-proizvodace', views.dodaj_proizvodace, name='dodaj_proizvodace'),
    path('trgovina/<int:sif_trgovina>', views.trgovina, name='trgovina'),  # int kinda sus
    path('trgovina/delete/<int:sif_trgovina>', views.delete_trgovina, name='delete_trgovina'),  # int kinda sus
    path('artikl/<int:barkod_artikla>', views.artikl, name='artikl'),
    path('uredi_artikl/<int:artikl_trgovina>', views.uredi_artikl_u_trgovini, name='uredi_artikl_u_trgovini'),
    path('obrisi_artikl/<int:artikl_trgovina>', views.obrisi_artikl_u_trgovini, name='obrisi_artikl_u_trgovini'),
    # android dio
    path('android/signup', views.android_sign_up, name='android_sign_up'),
    path('android/login', views.android_login, name='android_login'),
    path('android/logout', views.android_logout, name='android_logout'),
    path('android/artikli', views.android_artikli, name='android_artikli'),
    path('android/trgovine', views.android_trgovine, name='android_trgovine'),
    path('android/popis', views.android_popis, name='android_popis')
]
