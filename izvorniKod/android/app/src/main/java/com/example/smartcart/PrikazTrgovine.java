package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;
import java.io.Serializable;

import kotlin.reflect.KVariance;

public class PrikazTrgovine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // koristi se ovaj programski layout, valja prebaciti na xml
        //setContentView(R.layout.activity_prikaz_trgovine);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Intent intent = getIntent();
        String name = "";
        if (intent.hasExtra("name")) {
            name = (String)  intent.getSerializableExtra("name");
            TextView textView = new TextView(this);
            textView.setText(name);
            linearLayout.addView(textView);
        }



        String id = name.split(" ")[2];
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
                    linearLayout.addView(textView);






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



        setContentView(linearLayout);
    }
}