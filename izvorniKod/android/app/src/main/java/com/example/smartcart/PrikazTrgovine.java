package com.example.smartcart;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

public class PrikazTrgovine extends AppCompatActivity {

    private static final int REQUEST_BARCODE = 9901;
    PopupWindow window;
    String scannedBarcode;

    RecyclerView recyclerView;
    ArtiklAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prikaz_trgovine);
        if (savedInstanceState != null) {
            String barcode = savedInstanceState.getString("barcode");
            if (barcode != null) {
                getStavka(barcode);
            }
        }

        Intent intent = getIntent();
        String name = "";
        if (intent.hasExtra("name")) {
            name = (String) intent.getSerializableExtra("name");
        }

        String[] arr = name.split(" ");
        String id = arr[arr.length - 1];

        Connector conn = Connector.getInstance(this);
        List<JSONObject> artikli = new LinkedList<JSONObject>();
        conn.fetch_artikli_u_trgovini(id, jsonArray -> {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject fields = new JSONObject(jsonArray.getJSONObject(i).get("fields").toString());
                    artikli.add(fields);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            recyclerView = findViewById(R.id.artikli_list);
            draw_artikli(artikli);
        }, obj -> Log.e("err", obj.toString()));

        FloatingActionButton fab = findViewById(R.id.barcode_fab);
        fab.setBackgroundColor(getColor(R.color.white));
        fab.setOnClickListener(v -> {
            Intent i = new Intent(this, BarcodeScannerActivity.class);
            startActivityForResult(i, REQUEST_BARCODE);
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE && resultCode == RESULT_OK) {
            assert data != null;
            Toast.makeText(this, data.getStringExtra("barcode"), Toast.LENGTH_SHORT).show();
            getStavka(data.getStringExtra("barcode"));
        }
    }

    private void getStavka(String barcode) {
        this.scannedBarcode = barcode;
        Intent intent = getIntent();
        String[] arr = ((String) intent.getSerializableExtra("name")).split(" ");
        String id = arr[arr.length -1];

        Connector connector = Connector.getInstance(this);
        connector.fetch_artikl_u_trgovini(id, barcode, jsonArray -> {
            Log.d("artikl", jsonArray.toString());
            if (jsonArray.length() > 1) {
                JSONObject opis = null;
                JSONObject trg = null;
                try {
                    opis = new JSONObject(jsonArray.getJSONObject(1).get("fields").toString());
                    trg = new JSONObject(jsonArray.getJSONObject(0).get("fields").toString());
                    Stavka stavka = new Stavka(
                            Integer.parseInt(id),
                            opis.getString("naziv_artikla")
                    );
                    stavka.setCijena(Double.parseDouble(trg.getString("cijena")));
                    stavka.setUKosarici(true);
                    chooseListAndAddStavka(stavka);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                chooseListAndAddStavka(new Stavka(Integer.parseInt(id), barcode));
            }
        }, err -> Toast.makeText(this, R.string.no_barcode_in_store, Toast.LENGTH_LONG).show());

    }

    private void chooseListAndAddStavka(Stavka st) {
        SmartCartDatabase db = SmartCartDatabase.getInstance(this);
        List<Popis> svi = db.popisDao().dohvatiSvePopise();


        LinearLayout parent = new LinearLayout(this);
        parent.setPadding(16, 8, 16, 8);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setBackground(ContextCompat.getDrawable(this, R.drawable.rectangle_background));
        ListView lista = new ListView(this);

        window = new PopupWindow(parent, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lista.setOnItemClickListener((par, v, position, id) -> {
            st.setSifPopis(svi.get(position).getSifPopis());
            db.stavkaDao().dodajStavke(st);
            Toast.makeText(this, R.string.successfully_added, Toast.LENGTH_LONG).show();
            window.dismiss();
        });
        TextView naslov = new TextView(this);
        naslov.setTypeface(naslov.getTypeface(), Typeface.BOLD);
        naslov.setAllCaps(true);
        naslov.setText(st.getNaziv() + ": " + st.getCijena() + "kn");
        parent.addView(naslov);
        parent.addView(lista);

        Popis[] popisi = svi.toArray(new Popis[svi.size() - 1]);
        ArrayAdapter<Popis> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, popisi);

        lista.setAdapter(adapter);
        window.showAtLocation(findViewById(R.id.trgovine_div).getRootView(), Gravity.CENTER, 0, 0);
    }
    private void draw_artikli(List<JSONObject> artikli) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new ArtiklAdapter(getApplicationContext(), artikli, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("barcode", scannedBarcode);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (window != null) {
            window.dismiss();
            window = null;
        }
    }
}