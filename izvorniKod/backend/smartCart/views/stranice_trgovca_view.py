from django.shortcuts import get_object_or_404, redirect, render
from django.views.generic import View

from .functions import render_form, must_be_trgovac, read_form, stay_on_page, get_artikli_from_trgovina, root_dispatch, \
    redirect_to_home_page, User, get_object_or_none, get_vlasnik_trgovine, render_template
from ..forms import DodajTrgovinu, DodajArtikl, DodajProizvodaca, DodajArtiklUTrgovinu, PromijeniRadnoVrijeme, \
    UrediArtiklUTrgovini, PromijeniLongLat
from ..models import Trgovina, Artikl, TrgovinaArtikli, Proizvodac, Zemlja_porijekla, OpisArtikla


@must_be_trgovac
class TrgovacView(View):
    template_name = 'smartCart/trgovac.html'

    def __init__(self):
        super(TrgovacView, self).__init__()
        self.form = {'trg_form': DodajTrgovinu(), 'art_form': DodajArtikl(), 'pro_form': DodajProizvodaca()}

    def get(self, request, *args, **kwargs):
        trgovine = list(Trgovina.objects.filter(vlasnik__id=request.user.id))
        artikli = list(Artikl.objects.all())
        return render_form(self, request, trgovine=trgovine, artikli=artikli)


@must_be_trgovac
class TrgovinaView(View):
    template_name = 'smartCart/trgovina.html'

    def __init__(self):
        super(TrgovinaView, self).__init__()
        self.form = {
            'artikl_form': DodajArtiklUTrgovinu(), 
            'vrijeme_form': PromijeniRadnoVrijeme(),
            'longlat_form': PromijeniLongLat()
            }

    def dispatch(self, request, *args, **kwargs):
        t = Trgovina.objects.get(sif_trgovina=self.kwargs['sif_trgovina'])
        if request.user.id != t.vlasnik.id:
            return redirect_to_home_page(request)
        return root_dispatch(self, request, *args, **kwargs)

    def get(self, request, *args, **kwargs):
        return render_form(self, request, trgovina=Trgovina.objects.get(sif_trgovina=self.kwargs['sif_trgovina']),
                           artikli=get_artikli_from_trgovina(self.kwargs['sif_trgovina']))

    def post(self, request, *args, **kwargs):
        t = Trgovina.objects.get(sif_trgovina=self.kwargs['sif_trgovina'])
        if read_form(self, request, 'artikl_form'):
            bar_k = request.POST['artikl']
            cijena = request.POST['cijena']
            akcija = True if 'akcija' in request.POST else False
            dostupan = True if 'dostupan' in request.POST else False
            a = Artikl.objects.get(barkod_artikla=bar_k)
            try:
                old_trg_art = TrgovinaArtikli.objects.get(artikl__barkod_artikla=bar_k)
                old_trg_art.cijena = cijena
                old_trg_art.akcija = akcija
                old_trg_art.dostupan = dostupan
                old_trg_art.save()
            except TrgovinaArtikli.DoesNotExist:
                trg_art = TrgovinaArtikli(trgovina=t,
                                          artikl=a,
                                          cijena=cijena,
                                          akcija=akcija,
                                          dostupan=dostupan)
                trg_art.save()
        if read_form(self, request, 'vrijeme_form'):
            t.radno_vrijeme_pocetak = request.POST['radno_vrijeme_pocetak']
            t.radno_vrijeme_kraj = request.POST['radno_vrijeme_kraj']
            t.save()

        if read_form(self, request, 'longlat_form'):
            t.longitude = request.POST['longitude']
            t.latitude = request.POST['latitude']
            t.save()
        return stay_on_page(request)


@must_be_trgovac
class UrediArtiklView(View):
    form_class = UrediArtiklUTrgovini

    def dispatch(self, request, *args, **kwargs):
        self.t_id = TrgovinaArtikli.objects.get(id=self.kwargs['artikl_trgovina']).trgovina.sif_trgovina
        self.old_art = TrgovinaArtikli.objects.get(id=self.kwargs['artikl_trgovina'])
        self.form = UrediArtiklUTrgovini(initial={
            'cijena': self.old_art.cijena,
            'akcija': self.old_art.akcija,
            'dostupan': self.old_art.dostupan
        })
        t = Trgovina.objects.get(sif_trgovina=self.t_id)
        if request.user.id != t.vlasnik.id:  # Stop "hacking" into trgovina website
            return redirect_to_home_page(request)
        return super(UrediArtiklView, self).dispatch(request, *args, **kwargs)

    #TODO: dohvati opise i ispiši ih
    def get(self, request, *args, **kwargs):
        #print(self.kwargs['artikl_trgovina'])
        #print(TrgovinaArtikli.objects.get(id = 1))
        
        opisi = OpisArtikla.objects.all().filter(trgovina_artikl=TrgovinaArtikli.objects.get(id = self.kwargs['artikl_trgovina']))
        if len(opisi) == 0:
            opisi = ''
        return render(request, 'smartCart/artikl_u_trgovini.html',
                      {'form': self.form, 
                      'trgovina': Trgovina.objects.get(sif_trgovina=self.t_id),
                       'artikl': self.old_art.artikl.naziv_artikla,
                       'opisi' : opisi
                       })

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            old_art = TrgovinaArtikli.objects.get(id=self.kwargs['artikl_trgovina'])
            old_art.cijena = request.POST['cijena']
            old_art.akcija = True if 'akcija' in request.POST else False
            old_art.dostupan = True if 'dostupan' in request.POST else False
            old_art.save()
        return redirect(f'/trgovina/{self.t_id}')


@must_be_trgovac
class ObrisiArtiklView(View):
    def dispatch(self, request, *args, **kwargs):
        self.t_id = TrgovinaArtikli.objects.get(id=self.kwargs['artikl_trgovina']).trgovina.sif_trgovina
        t = Trgovina.objects.get(sif_trgovina=self.t_id)
        if request.user.id != t.vlasnik.id:  # Stop "hacking" into trgovina website
            return redirect_to_home_page(request)
        return super(ObrisiArtiklView, self).dispatch(request, *args, **kwargs)

    def get(self, request, *args, **kwargs):
        old_art = TrgovinaArtikli.objects.get(id=self.kwargs['artikl_trgovina'])
        old_art.delete()
        return stay_on_page(request)

    def post(self, request, *args, **kwargs):
        return stay_on_page(request)


@must_be_trgovac
class DodajTrgovineView(View):
    form_class = DodajTrgovinu

    def __init__(self):
        super(DodajTrgovineView, self).__init__()
        self.form = DodajTrgovineView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            trgovina = Trgovina(naz_trgovina=request.POST['naz_trgovina'],
                                adresa_trgovina=request.POST['adresa_trgovina'],
                                vlasnik=get_object_or_404(User, pk=request.user.id),
                                radno_vrijeme_pocetak=request.POST['radno_vrijeme_pocetak'],
                                radno_vrijeme_kraj=request.POST['radno_vrijeme_kraj'],
                                latitude=request.POST['latitude'],
                                longitude=request.POST['longitude'])
            trgovina.save()
        return stay_on_page(request)

    def get(self, request, *args, **kwargs):
        return stay_on_page(request)


@must_be_trgovac
class DodajProizvodaceView(View):
    form_class = DodajProizvodaca

    def __init__(self):
        super(DodajProizvodaceView, self).__init__()
        self.form = DodajProizvodaceView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            proizvodac_za_dodati = Proizvodac(naziv=request.POST['naziv'])
            proizvodac_za_dodati.save()
        return stay_on_page(request)

    def get(self, request, *args, **kwargs):
        return stay_on_page(request)


@must_be_trgovac
class DodajArtikleView(View):
    form_class = DodajArtikl

    def __init__(self):
        super(DodajArtikleView, self).__init__()
        self.form = DodajArtikleView.form_class()

    def post(self, request, *args, **kwargs):
        if read_form(self, request):
            artikl_za_dodati = Artikl(
                barkod_artikla=request.POST['barkod_artikla'],
                naziv_artikla=request.POST['naziv_artikla'],
                opis_artikla=request.POST['opis_artikla'],
                proizvodac=get_object_or_none(Proizvodac, naziv=request.POST['proizvodac']),
                zemlja_porijekla=get_object_or_none(Zemlja_porijekla, naziv=request.POST['zemlja_porijekla']),
                vegan=True if 'vegan' in request.POST else False
            )
            artikl_za_dodati.save()
        return stay_on_page(request)

    def get(self, request, *args, **kwargs):
        return stay_on_page(request)


@must_be_trgovac
class DeleteTrgovinaView(View):
    def post(self, request, *args, **kwargs):
        if not request.user.is_authenticated and request.user != get_vlasnik_trgovine(self.kwargs['sif_trgovina']):
            return redirect('trgovac')
        Trgovina.objects.filter(sif_trgovina=self.kwargs['sif_trgovina']).delete()
        return redirect('trgovac')


@must_be_trgovac
class ArtiklView(View):
    template_name = 'smartCart/artikl.html'

    def get(self, request, *args, **kwargs):
        return render_template(self, request, artikl=Artikl.objects.get(barkod_artikla=self.kwargs["barkod_artikla"]))
