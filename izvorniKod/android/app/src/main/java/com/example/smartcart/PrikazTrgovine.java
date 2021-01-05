package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

public class PrikazTrgovine extends AppCompatActivity {

    private static final int REQUEST_BARCODE = 9901;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_prikaz_trgovine);

        LinearLayout linearLayout = findViewById(R.id.trgovine_div);

        Intent intent = getIntent();
        String name = "";
        if (intent.hasExtra("name")) {
            name = (String)  intent.getSerializableExtra("name");
            TextView textView = findViewById(R.id.textView6);
            textView.setText(name);
        }

        String[] arr = name.split(" ");
        String id = arr[arr.length -1];

        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();

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
            Toast.makeText(this, data.getStringExtra("barcode"), Toast.LENGTH_LONG).show();
        }
    }

}