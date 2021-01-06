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

import com.example.smartcart.database.Popis;
import com.example.smartcart.database.SmartCartDatabase;
import com.example.smartcart.database.Stavka;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.List;

public class PrikazTrgovine extends AppCompatActivity {

    private static final int REQUEST_BARCODE = 9901;
    PopupWindow window;
    String scannedBarcode;

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
            name = (String)  intent.getSerializableExtra("name");
            TextView textView = findViewById(R.id.textView6);
            textView.setText(name);
        }

        String[] arr = name.split(" ");
        String id = arr[arr.length -1];

        Connector conn = Connector.getInstance(this);
        conn.fetch_artikli_u_trgovini(id, jsonArray -> {
            //Log.d("msg", jsonArray.toString());
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    //Log.d("trgovina", jsonArray.getJSONObject(i).toString());
                    JSONObject fields = new JSONObject(jsonArray.getJSONObject(i).get("fields").toString());
                    TextView textView = new TextView(this);
                    textView.setText(fields.toString());
                    ((LinearLayout) findViewById(R.id.trgovine_dynamic_list)).addView(textView);





                    textView.setOnClickListener(v -> {
                        Intent intent1 = new Intent(PrikazTrgovine.this, PrikazArtikla.class);
                        try {
                            Log.d("ovi", fields.getString("trgovina") + " " + fields.getString("artikl"));
                            intent1.putExtra("sif_trgovina", (Serializable) fields.getString("trgovina"));
                            intent1.putExtra("barkod", (Serializable) fields.getString("artikl"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        startActivity(intent1);
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
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