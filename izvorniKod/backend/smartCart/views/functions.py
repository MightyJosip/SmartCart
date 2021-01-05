import json

from django.contrib.auth import get_user_model, login
from django.contrib.auth.decorators import user_passes_test, login_required
from django.contrib.sessions.models import Session
from django.http import JsonResponse, HttpResponse
from django.shortcuts import render, redirect
from django.utils.decorators import method_decorator
from django.contrib.auth import logout

from ..models import TrgovinaArtikli, UserSession, Trgovina, Uloga

User = get_user_model()


def user_enabled_required(user):
    return user.omogucen
must_be_enabled = method_decorator(user_passes_test(user_enabled_required, login_url='/login', redirect_field_name='index'), name='dispatch')


def trgovac_login_required(user):
    return user.get_auth_level() == 'Trgovac' if user.is_authenticated else False


must_be_trgovac = method_decorator(
    user_passes_test(trgovac_login_required, login_url='login', redirect_field_name='index'),
    name='dispatch')

must_be_logged = method_decorator(login_required(login_url='login'), name='dispatch')


def render_template(view, request, **kwargs):
    return render(request, view.template_name, kwargs)


def render_form(view, request, **kwargs):
    if isinstance(view.form, dict):
        kwargs.update(view.form)
        return render_template(view, request, **kwargs)
    kwargs['form'] = view.form
    return render_template(view, request, **kwargs)


def redirect_to_home_page(request):
    if request.user.get_auth_level() == 'Trgovac':
        return redirect('trgovac')
    else:
        return redirect('index')


def root_dispatch(view, request, *args, **kwargs):
    return super(type(view), view).dispatch(request, *args, **kwargs)


def read_form(view, request, name=None, files=False):
    if isinstance(view.form, dict):
        view.form[name] = create_form(view.form[name].__class__, request, files)
        return view.form[name].is_valid()
    view.form = create_form(view.form_class, request, files)
    return view.form.is_valid()


def create_form(cls, request, files):
    # Helper method, shouldn't be used outside of this file
    if files:
        return cls(request.POST, request.FILES)
    return cls(request.POST)


def get_artikli_from_trgovina(sifTrgovina):
    return TrgovinaArtikli.objects.filter(trgovina=sifTrgovina)


def get_vlasnik_trgovine(sifTrgovine):
    return Trgovina.objects.get(sif_trgovina=sifTrgovine).vlasnik


def stay_on_page(request):
    return redirect(request.META['HTTP_REFERER'])


def get_object_or_none(model, **kwargs):
    try:
        return model.objects.get(**kwargs)
    except model.DoesNotExist:
        return None


def get_user_from_session(session_key):
    return UserSession.objects.get(session_id=session_key).user


def get_authorization_level(user):
    if not user.is_authenticated:
        return 'gost'
    elif user.get_auth_level() == 'Kupac':
        return 'kupac'
    elif user.get_auth_level() == 'Trgovac':
        return 'trgovac'
    else:
        return 'admin'


def android_login_function(request, user):
    login(request=request, user=user)
    Session.objects.filter(usersession__user=user).delete()
    request.session.save()
    UserSession.objects.get_or_create(user=user,
                                      session=Session.objects.get(pk=request.session.session_key))


def create_json_response(code, data=None, **kwargs):
    if data is not None:
        json_response = HttpResponse(data, content_type='application/json')
    else:
        json_response = HttpResponse(json.dumps(kwargs), content_type='application/json')
    json_response.status_code = code
    return json_response

