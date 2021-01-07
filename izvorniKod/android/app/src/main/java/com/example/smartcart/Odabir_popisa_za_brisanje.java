package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;

import java.util.ArrayList;
import java.util.List;

public class Odabir_popisa_za_brisanje extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odabir_popisa_za_brisanje);


        ArrayList<String> array = new ArrayList<>();

        ListView listapopisazabrisanje = (ListView) findViewById(R.id.popisi_za_brisanje);

        listapopisazabrisanje.setOnItemClickListener((parent, v, position, id) -> {

            Intent intent1 = new Intent(getBaseContext(), PrikazPopisaActivity.class);

            String s = array.get(position);
            String[] s1 = s.split(" ");
            final String nazivpopisa = s1[1];

            SmartCartDatabase db = SmartCartDatabase.getInstance(Odabir_popisa_za_brisanje.this);
            PopisDao dao = db.popisDao();
            Popis popis = new Popis(nazivpopisa);
            popis.setSifPopis( Integer.parseInt(s1[0]));

            dao.obrisiPopis(popis);

            Toast.makeText(this, "Popis obrisan", Toast.LENGTH_SHORT).show();

            //startActivity(intent1);
            finish();
        });

        List<Popis> svi = SmartCartDatabase.getInstance(this).popisDao().dohvatiSvePopise();
        for (Popis p : svi) {
            TextView text = new TextView(this);
            text.setText(p.toString());
            array.add((String) text.getText());
        }

        ArrayAdapter adapter = new ArrayAdapter(Odabir_popisa_za_brisanje.this, android.R.layout.simple_list_item_1, array);

        listapopisazabrisanje.setAdapter(adapter);

    }


}