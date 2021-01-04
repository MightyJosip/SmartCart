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
from django.urls import path
from django.conf import settings
from django.conf.urls.static import static
from smartCart.views import *
from smartCart.views.android_views import *
from smartCart.views.web_account_views import *
from django.views.generic import TemplateView
from django.urls import path, include

urlpatterns = [
    path('admin', admin.site.urls),
    path('', IndexView.as_view(), name='index'),
    path('signup/kupac', SignUpKupacView.as_view(), name='signup_kupac'),
    path('signup/trgovac', SignUpTrgovacView.as_view(), name='signup_trgovac'),
    path('signup/admin', SignUpAdminView.as_view(), name='signup_admin'),
    path('login', LoginView.as_view(), name='login'),
    path('logout', LogoutView.as_view(), name='logout'),
    path('trgovac', TrgovacView.as_view(), name='trgovac'),
    path('edit_profile', EditProfileView.as_view(), name='edit_profile'),
    path('delete_account', DeleteAccountView.as_view(), name='delete_account'),
    path('trgovac/dodaj-trgovine', DodajTrgovineView.as_view(), name='dodaj_trgovine'),
    path('trgovac/dodaj-artikle', DodajArtikleView.as_view(), name='dodaj_artikle'),
    path('trgovac/dodaj-proizvodace', DodajProizvodaceView.as_view(), name='dodaj_proizvodace'),
    path('trgovina/<int:sif_trgovina>', TrgovinaView.as_view(), name='trgovina'),
    path('trgovina/delete/<int:sif_trgovina>', DeleteTrgovinaView.as_view(), name='delete_trgovina'),
    path('artikl/<int:barkod_artikla>', ArtiklView.as_view(), name='artikl'),
    path('uredi_artikl/<int:artikl_trgovina>', UrediArtiklView.as_view(), name='uredi_artikl_u_trgovini'),
    path('obrisi_artikl/<int:artikl_trgovina>', ObrisiArtiklView.as_view(), name='obrisi_artikl_u_trgovini'),
    path('nova_lozinka', NovaLozinkaView.as_view(), name='nova_lozinka'),
    path('potvrdi_lozinku', PotvrdiLozinkuView.as_view(), name='potvrdi_lozinku'),
    # android dio
    path('android/signup', AndroidSignUpView.as_view(), name='android_sign_up'),
    path('android/login', AndroidLogInView.as_view(), name='android_login'),
    path('android/logout', AndroidLogoutView.as_view(), name='android_logout'),
    path('android/edit_profile', AndroidEditProfileView.as_view(), name='android_edit_profile'),
    path('android/artikli', AndroidArtikliView.as_view(), name='android_artikli'),
    path('android/trgovine', AndroidTrgovineView.as_view(), name='android_trgovine'),
    path('android/popis', AndroidPopisView.as_view(), name='android_popis'),
    # dobro paziti na sljedeća dva!
    path('android/artikltrgovina', AndroidArtiklTrgovina.as_view(), name='artikltrgovina'),
    path('android/artiklitrgovina', AndroidSviArtikliUTrgovini.as_view(), name="artiklitrgovina"),
    #
    path('android/opisi', AndroidOpisiView.as_view(), name='opisi'),
    path('android/downvote', AndroidDownvoteView.as_view(), name='downvote'),
    path('android/upvote', AndroidUpvoteView.as_view(), name='upvote'),
    path('android/write_description', AndroidWriteProductDescription.as_view(), name='write_description'),
    path('android/skenirajbarkod', SkenirajBarkodView.as_view(), name='skenirajbarkod'),

    path('accounts/', include('allauth.urls')),
    path('', TemplateView.as_view(template_name='social_app/index.html'))
]

urlpatterns += static(settings.STATIC_URL, document_root=settings.STATIC_ROOT)
