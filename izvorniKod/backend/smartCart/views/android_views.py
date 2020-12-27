import json

from django.contrib.auth import authenticate, logout
from django.contrib.sessions.models import Session
from django.core import serializers
from django.http import HttpResponse

from .functions import create_json_response, android_login_function, get_user_from_session, User, get_object_or_none
from ..models import Artikl, SecretCode, Trgovina, TrgovinaArtikli, OpisArtikla, Vrsta, Zemlja_porijekla, \
    BaseUserModel, Glasovi, Uloga

from allauth.account.views import *



# TODO: u svim funkcijama porokati ove dekoratore koji provjeravaju login i lvl autorizacije
class AndroidArtikliView(View):
    def post(self, request, *args, **kwargs):
        try:
            naziv_artikla = json.loads(request.body)['naziv_artikla']
        except KeyError:
            naziv_artikla = ''
        artikli = Artikl.objects.filter(naziv_artikla__contains='%s' % naziv_artikla)
        return create_json_response(200, data=serializers.serialize('json', artikli))


class AndroidArtiklTrgovina(View):
    def post(self, request, *args, **kwargs):
        data = json.loads(request.body)
        sif_trgovina = data['sif_trgovina']
        barkod = data['barkod']

        if (sif_trgovina != ''):
            artikl_trgovina = TrgovinaArtikli.objects.get(
                trgovina=Trgovina.objects.get(sif_trgovina=sif_trgovina),
                artikl=Artikl.objects.get(barkod_artikla=barkod))
        else:
            artikl_trgovina = TrgovinaArtikli.objects.all().filter(artikl=Artikl.objects.get(barkod_artikla=barkod))


        najbolji_opis = OpisArtikla.objects.all().filter(artikl_id=barkod).order_by('broj_glasova').reverse()
        if len(najbolji_opis) > 0:
            najbolji_opis = najbolji_opis[0]
            data = serializers.serialize('json', [artikl_trgovina] + [najbolji_opis])
        else:
            data = serializers.serialize('json', [artikl_trgovina])

        return create_json_response(200, data=data)


class AndroidOpisiView(View):
    def post(self, request, *args, **kwargs):
        data = json.loads(request.body)
        id_artikltrgovina = data['id']

        opisi = OpisArtikla.objects.filter(trgovina_artikl_id=id_artikltrgovina)
        return create_json_response(200, data=serializers.serialize('json', opisi))


class AndroidDownvoteView(View):
    def post(self, request, *args, **kwargs):
        id = json.loads(request.body)['id']
        session_key = json.loads(request.body)['session_id']
        opis = OpisArtikla.objects.get(id=id)
        korisnik = BaseUserModel.objects.get(pk=get_user_from_session(session_key).id)
        old_vote = get_object_or_none(Glasovi, user_id=korisnik, sif_opis=opis)
        if old_vote is None:
            Glasovi.objects.create(user=korisnik, sif_opis=opis, vrijednost_glasa='G')
            opis.broj_glasova -= 1
            opis.save()
            return create_json_response(200, msg='Successfully voted')
        else:
            if old_vote.vrijednost_glasa == 'D':
                return create_json_response(200, msg='Already voted')
            else:
                old_vote.vrijednost_glasa = 'D'
                opis.broj_glasova -= 2
                old_vote.save()
                opis.save()
                return create_json_response(200, msg='Changed vote from downvote to upvote')


class AndroidUpvoteView(View):
    def post(self, request, *args, **kwargs):
        id = json.loads(request.body)['id']
        session_key = json.loads(request.body)['session_id']
        opis = OpisArtikla.objects.get(id=id)
        korisnik = BaseUserModel.objects.get(pk=get_user_from_session(session_key).id)
        old_vote = get_object_or_none(Glasovi, user_id=korisnik, sif_opis=opis)
        if old_vote is None:
            Glasovi.objects.create(user=korisnik, sif_opis=opis, vrijednost_glasa='G')
            opis.broj_glasova += 1
            opis.save()
            return create_json_response(200, msg='Successfully voted')
        else:
            if old_vote.vrijednost_glasa == 'G':
                return create_json_response(200, msg='Already voted')
            else:
                old_vote.vrijednost_glasa = 'G'
                opis.broj_glasova += 2
                old_vote.save()
                opis.save()
                return create_json_response(200, msg='Changed vote from downvote to upvote')


class AndroidWriteProductDescription(View):
    def post(self, request, *args, **kwargs):
        data = json.loads(request.body)

        email_autor_opisa = data['email']
        barkod = data['barkod']

        sif_vrsta = data['sif_vrsta']
        zemlja_porijekla = data['zemlja_porijekla']
        sif_trgovina = data['sif_trgovina']
        sif_trgovina_artikl = data['sif_trgovina_artikl']

        naziv_artikla = data['naziv_artikla']
        opis_artikla = data['opis_artikla']

        try:
            opis = OpisArtikla(autor_opisa=BaseUserModel.objects.get(email=email_autor_opisa),
                               artikl=Artikl.objects.get(barkod_artikla=barkod),
                               vrsta=Vrsta.objects.get(sif_vrsta=sif_vrsta),
                               zemlja_porijekla=Zemlja_porijekla.objects.get(naziv=zemlja_porijekla),
                               trgovina=Trgovina.objects.get(sif_trgovina=sif_trgovina),
                               trgovina_artikl=TrgovinaArtikli.objects.get(id=sif_trgovina_artikl),
                               naziv_artikla=naziv_artikla,
                               opis_artikla=opis_artikla)
            opis.save()
        except Exception as e:
            data = json.dumps({'err': str(e)})
            return create_json_response(403, data=data)

        return HttpResponse()


class AndroidPopisView(View):
    def post(self, request, *args, **kwargs):
        barkodovi = json.loads(request.body)['barkod']
        artikli = []
        for barkod in barkodovi:
            artikli += Artikl.objects.filter(barkod_artikla=barkod)
        return create_json_response(200, data=serializers.serialize('json', artikli))


class AndroidLogInView(View):
    def post(self, request, *args, **kwargs):
        data = json.loads(request.body)
        email = data['email']
        password = data['password']
        if email == "" or password == "":
            return create_json_response(401, err='Fill out all fields')
        user = authenticate(request, username=email, password=password)
        if user is not None:
            android_login_function(request, user)
            return create_json_response(200, session_id=request.session.session_key,
                                        authorisation_level=get_user_from_session(
                                            request.session.session_key).get_auth_level())
        else:
            return create_json_response(401, err='Wrong email or password')


class AndroidLogoutView(View):
    def post(self, request, *args, **kwargs):
        user = get_user_from_session(json.loads(request.body)['sessionId'])
        Session.objects.filter(usersession__user=user).delete()
        logout(request=request)
        return create_json_response(200, success='done')


class AndroidSignUpView(View):
    def post(self, request, *args, **kwargs):
        email = json.loads(request.body)['email']
        password = json.loads(request.body)['password']
        secret_code = json.loads(request.body)['secret_code']

        if secret_code:
            authorisation_level = 'trgovac'
        else:
            authorisation_level = 'kupac'

        if email == "" or password == "":
            return create_json_response(401, err='Fill out all fields')

        if User.objects.filter(email=email).exists():
            return create_json_response(401, err='User already exists')

        if authorisation_level == 'kupac':
            User.objects.create_user(email, password, is_kupac=True, authorisation_level=Uloga.objects.get(auth_level='Kupac'))
            return create_json_response(200, success='done')

        if authorisation_level == 'trgovac':
            secret_code = SecretCode.objects.filter(value=secret_code)
            if not secret_code.exists():
                return create_json_response(401, err='Wrong secret code')
            secret_code.delete()
            User.objects.create_user(email, password, is_trgovac=True, authorisation_level=Uloga.objects.get(auth_level='Trgovac'))
            return create_json_response(200, success='done')
        return create_json_response(401, err='Weird error :(')


class AndroidTrgovineView(View):
    def post(self, request, *args, **kwargs):
        try:
            naz_trgovina = json.loads(request.body)['naz_trgovina']
        except KeyError:
            naz_trgovina = ''
        try:
            sif_trgovina = json.loads(request.body)['sif_trgovina']
        except KeyError:
            sif_trgovina = None
        trgovine = Trgovina.objects.filter(naz_trgovina__contains=f'{naz_trgovina}')
        if sif_trgovina is not None:
            trgovine = trgovine.filter(sif_trgovina=sif_trgovina)
        return create_json_response(200, data=serializers.serialize('json', trgovine))
