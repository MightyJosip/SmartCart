package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.example.smartcart.database.StavkaDao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        List<Stavka> stavcice = dao.dohvatiStavkeZaPopis(myExtra);

        StringBuilder sb = new StringBuilder();
        for (Stavka s : stavcice) {
            sb.append(s).append("\n");
        }

        ArrayList<String> array = new ArrayList<>();
        ListView listastavki = (ListView) findViewById(R.id.stavkalistview);

        List<Stavka> stavke = SmartCartDatabase.getInstance(PrikazStavkiActivity.this).stavkaDao().dohvatiStavkeZaPopis(myExtra);
        for (Stavka s : stavke) {
            TextView text = new TextView(PrikazStavkiActivity.this);
            text.setText(s.toString());
            array.add((String) text.getText());
        }

        Button btnIzracunCijene = findViewById(R.id.btn_izracun_cijene);
        btnIzracunCijene.setOnClickListener(l -> {
            Intent nextIntent = new Intent(PrikazStavkiActivity.this, IzracunCijeneActivity.class);
            EditText radius = (EditText) findViewById(R.id.fld_radius);

            nextIntent.putExtra("id", myExtra);
            nextIntent.putExtra("distance", Integer.parseInt(radius.getText().toString()));
            startActivity(nextIntent);
        });

        ArrayAdapter adapter = new ArrayAdapter(PrikazStavkiActivity.this, android.R.layout.simple_list_item_1, array);

        listastavki.setAdapter(adapter);
    }



}
