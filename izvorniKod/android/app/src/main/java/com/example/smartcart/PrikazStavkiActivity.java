package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PrikazStavkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_stavki);

        final Intent intent = getIntent();
        final int myExtra = intent.getIntExtra("id", -1);

        SmartCartDatabase db = SmartCartDatabase.getInstance(this);
        StavkaDao dao = db.stavkaDao();
        renderList(myExtra);

        EditText et = findViewById(R.id.ime_stavke);
        Button btn_dodaj = findViewById(R.id.nova_stavka);
        btn_dodaj.setOnClickListener(l -> {
            String input = et.getText().toString().trim();
            if (input.isEmpty())
                return;

            Stavka novaStavka = new Stavka(myExtra, input);

            dao.dodajStavke(novaStavka);
            renderList(myExtra);
        });

        Button btn_izracunaj = findViewById(R.id.izracunaj_cijenu);
        btn_izracunaj.setOnClickListener(l -> {
            Intent izracun_cijene_intent = new Intent(getBaseContext(), IzracunCijeneActivity.class);
            izracun_cijene_intent.putExtra("id", myExtra);
            startActivity(izracun_cijene_intent);
        });

    }

    private void renderList(int sifraPopisa) {
        ArrayList<String> array = new ArrayList<>();
        ListView listastavki = (ListView) findViewById(R.id.stavkalistview);

        List<Stavka> stavke = SmartCartDatabase.getInstance(PrikazStavkiActivity.this).stavkaDao().dohvatiStavkeZaPopis(sifraPopisa);
        for (Stavka s : stavke) {
            TextView text = new TextView(PrikazStavkiActivity.this);
            text.setText(s.toString());
            array.add((String) text.getText());
        }

        ArrayAdapter adapter = new ArrayAdapter(PrikazStavkiActivity.this, android.R.layout.simple_list_item_1, array);

        listastavki.setAdapter(adapter);
    }
}