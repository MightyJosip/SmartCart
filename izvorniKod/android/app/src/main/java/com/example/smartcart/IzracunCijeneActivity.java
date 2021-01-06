package com.example.smartcart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;

import java.util.ArrayList;
import java.util.List;

public class IzracunCijeneActivity extends AppCompatActivity {

    private List<Stavka> stavke;

    private double cijena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izracun_cijene);

        final Intent intent = getIntent();
        final int id_popisa = intent.getIntExtra("id", -1);

        SmartCartDatabase db = SmartCartDatabase.getInstance(this);
        StavkaDao dao = db.stavkaDao();
        PopisDao pao = db.popisDao();
        Popis popis_za_izracun = pao.dohvatiId(id_popisa);
        stavke = dao.dohvatiStavkeZaPopis(id_popisa);
        izracunajCijenu(popis_za_izracun);
    }

    private void izracunajCijenu(Popis popis) {
        if (popis.getNacinIzracuna() == 1) {
            if (checkLocationPermission()) {
                cijenaNajblizih();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
            }
        } else {
            System.out.println("test 2");
        }
    }

    private void cijenaNajblizih() {
        Connector connector = Connector.getInstance(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Double[] myLocation = getLocation(lm);
        List<String> barkodovi = new ArrayList<>();
        for (Stavka stavka : stavke) {
            barkodovi.add(stavka.getBarkod());
        }
        connector.calculatePrice(myLocation[0], myLocation[1], 15, barkodovi, response -> {
            TextView prikazCijene = findViewById(R.id.najniza_cijena);
            prikazCijene.setText(prikazCijene.getText() + String.format("%.2f", Double.valueOf(response)) + "kn");
        }, error -> {
            Toast.makeText(IzracunCijeneActivity.this, "Unable to calculate price", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1337 && checkLocationPermission()) {
            cijenaNajblizih();
        }
    }

    private boolean checkLocationPermission() {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission((Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private Double[] getLocation(LocationManager lm) {
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        return new Double[] {latitude, longitude};
    }

}