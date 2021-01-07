package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Odabir_popisa extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odabir_popisa);
        final String naziv;
        final Intent intent = getIntent();
        final int siftrgovina = Integer.parseInt(intent.getStringExtra("sif_trgovina"));
        final String barkod = intent.getStringExtra("barkod");
        if(intent.hasExtra("naziv")){
             naziv = intent.getStringExtra("naziv");
        }
        else{
            naziv = null;
        }



        ArrayList<String> array = new ArrayList<>();

        ListView listapopisa = (ListView) findViewById(R.id.popislistview);

        listapopisa.setOnItemClickListener((parent, v, position, id) -> {

            Intent intent1 = new Intent(getBaseContext(), PrikazArtikla.class);

            String s = array.get(position);
            String[] s1 = s.split(" ");
            final int sifpopisa = Integer.parseInt(s1[0]);

            SmartCartDatabase db = SmartCartDatabase.getInstance(this);
            StavkaDao dao = db.stavkaDao();
            Stavka novaStavka;

            if(naziv == null){
                novaStavka = new Stavka(sifpopisa, barkod, siftrgovina);
            }
            else{
                novaStavka = new Stavka(sifpopisa, barkod, siftrgovina, naziv);
            }

            //Stavka novaStavka = new Stavka(sifpopisa, barkod, siftrgovina);
            //Stavka novaStavka = new Stavka(sifpopisa, barkod);
            dao.dodajStavke(novaStavka);

            Toast.makeText(this, "Artikl dodan u popis", Toast.LENGTH_SHORT).show();

            //startActivity(intent1);
            finish();
        });

        List<Popis> svi = SmartCartDatabase.getInstance(this).popisDao().dohvatiSvePopise();
        for (Popis p : svi) {
            TextView text = new TextView(this);
            text.setText(p.toString());
            array.add((String) text.getText());
        }

        ArrayAdapter adapter = new ArrayAdapter(Odabir_popisa.this, android.R.layout.simple_list_item_1, array);

        listapopisa.setAdapter(adapter);


    }
}