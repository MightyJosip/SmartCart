package com.example.smartcart;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class IzracunCijeneActivity extends AppCompatActivity {

    private List<Stavka> stavke;

    private double cijena;

    private int distance;

    private int permission;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izracun_cijene);

        intent = getIntent();
        final int id_popisa = intent.getIntExtra("id", -1);
        distance = intent.getIntExtra("distance", 10);

        SmartCartDatabase db = SmartCartDatabase.getInstance(this);
        StavkaDao dao = db.stavkaDao();
        PopisDao pao = db.popisDao();
        Popis popis_za_izracun = pao.dohvatiId(id_popisa);
        stavke = dao.dohvatiStavkeZaPopis(id_popisa);
        izracunajCijenu(popis_za_izracun);
    }

    @Override
    protected void onResume() {
        super.onResume();
        permission = 0;
        SmartCartDatabase db = SmartCartDatabase.getInstance(this);
        final int id_popisa = intent.getIntExtra("id", -1);
        PopisDao pao = db.popisDao();
        Popis popis_za_izracun = pao.dohvatiId(id_popisa);
        izracunajCijenu(popis_za_izracun);
    }

    private void izracunajCijenu(Popis popis) {
        if (popis.getNacinIzracuna() == 1) {
            if (checkLocationPermission()) {
                permission++;
            }
            if (checkLocationPermission2()) {
                permission++;
            }
            if (permission == 2) {
                cijenaNajblizih();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1337);
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1338);
            }
        } else {
            System.out.println("test 2");
        }
    }

    private void cijenaNajblizih() {
        System.out.println("HERE");
        Connector connector = Connector.getInstance(this);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Double[] myLocation = getLocation(lm);
        List<String> barkodovi = new ArrayList<>();
        for (Stavka stavka : stavke) {
            barkodovi.add(stavka.getBarkod());
        }
        connector.calculatePrice(myLocation[0], myLocation[1], distance, barkodovi, response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String sum = jsonObject.get("sum").toString();
                TextView prikazCijene = findViewById(R.id.najniza_cijena);
                Double sumDouble = Double.valueOf(sum);
                if ((sumDouble > 0)) {
                    prikazCijene.setText("Najniža cijena je " + String.format("%.2f", sumDouble) + "kn");
                } else {
                    prikazCijene.setText("Nemoguće izračunati cijenu");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast.makeText(IzracunCijeneActivity.this, "Unable to calculate price", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1337 && checkLocationPermission()) {
            permission++;
        }
        if (requestCode == 1338 && checkLocationPermission2()) {
            permission++;
        }
        if (permission == 2) {
            cijenaNajblizih();
        }
    }

    private boolean checkLocationPermission() {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission((Manifest.permission.ACCESS_FINE_LOCATION));
    }

    private boolean checkLocationPermission2() {
        return PackageManager.PERMISSION_GRANTED == checkSelfPermission((Manifest.permission.ACCESS_COARSE_LOCATION));
    }

    private Double[] getLocation(LocationManager lm) {
        System.out.println(checkLocationPermission());
        System.out.println(checkLocationPermission2());

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                return new Double[]{latitude, longitude};
            }
        }
        return null;
    }
}