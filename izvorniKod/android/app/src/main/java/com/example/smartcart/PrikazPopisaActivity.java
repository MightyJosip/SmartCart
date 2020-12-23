package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.PopisDao;
import com.example.smartcart.database.SmartCartDatabase;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class PrikazPopisaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_popisa);


        ArrayList<String> array = new ArrayList<>();
        /*TextView text = new TextView(this);
        text.setText("tusam");*/
        //Toast toast = Toast.makeText(this, "Uspjeh. Poruka: ", Toast.LENGTH_LONG);

        ListView lista = (ListView) findViewById(R.id.listview);
        lista.setOnItemClickListener((parent, v, position, id) -> {
            //Toast.makeText(PrikazPopisaActivity.this, array.get(position), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getBaseContext(), PrikazStavkiActivity.class);
            String s = array.get(position);
            String[] s1 = s.split(" ");
            int sif = Integer.parseInt(s1[0]);
            intent.putExtra("id", sif);
            startActivity(intent);

        });

        List<Popis> svi = SmartCartDatabase.getInstance(this).popisDao().dohvatiSvePopise();
        for (Popis p : svi) {
            TextView text = new TextView(this);
            text.setText(p.toString());
            array.add((String) text.getText());
        }

        ArrayAdapter adapter = new ArrayAdapter(PrikazPopisaActivity.this, android.R.layout.simple_list_item_1, array);

        lista.setAdapter(adapter);


    }
}