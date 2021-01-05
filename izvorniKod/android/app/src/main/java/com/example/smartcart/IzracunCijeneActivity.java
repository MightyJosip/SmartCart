package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;

import java.util.List;

public class IzracunCijeneActivity extends AppCompatActivity {

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
        List<Stavka> stavcice = dao.dohvatiStavkeZaPopis(id_popisa);



        System.out.println(stavcice);



    }

    public int izracunajCijenu(Popis popis, List<Stavka> stavke) {
        int result = 0;
        if (popis.getNacinIzracuna() == 1) {

        }
        else {

        }
        return result;
    }
}