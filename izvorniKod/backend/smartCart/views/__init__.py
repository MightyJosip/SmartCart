from .web_account_views import IndexView, SignUpKupacView, SignUpTrgovacView, LoginView, LogoutView, EditProfileView, \
                               DeleteAccountView, error_404

from .stranice_trgovca_view import TrgovacView, TrgovinaView, DodajTrgovineView, DeleteTrgovinaView, ArtiklView, \
                                   DodajArtikleView, UrediArtiklView, ObrisiArtiklView, DodajProizvodaceView

from .android_views import AndroidSignUpView, AndroidLogInView, AndroidLogoutView, AndroidArtikliView, \
                           AndroidTrgovineView, AndroidPopisView, AndroidEditProfileView
