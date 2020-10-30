from django.urls import path

from . import views

urlpatterns = [
    path('', views.index, name = 'index'),
    path('login', views.login, name = 'login'),
    path('trgovac', views.trgovac, name='trgovac'), ##
    path('logout', views.logout, name='logout'),
    path('trgovac/dodaj-trgovine', views.dodaj_trgovine, name='dodaj_trgovine'),
    path('trgovac/dodaj-artikle', views.dodaj_artikle, name='dodaj_artikle')
]
