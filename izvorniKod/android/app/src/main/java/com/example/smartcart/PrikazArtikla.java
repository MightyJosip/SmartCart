package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class PrikazArtikla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ovo valja pomoću xml-a, a ne programski
        // setContentView(R.layout.activity_prikaz_artikla);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Intent intent = getIntent();
        Log.d("ovi", "Tu sam");
        if (intent.hasExtra("sif_trgovina") && intent.hasExtra("barkod")) {
            Log.d("ovi", "imaaaa");
            String sif_trgovina = (String) intent.getSerializableExtra("sif_trgovina");
            String barkod = (String) intent.getSerializableExtra("barkod");
            Log.d("artikl - sif_trgovina", sif_trgovina);
            Log.d("artikl - barkod", barkod);

            final String[] id_opis = {null};
            Connector connector = Connector.getInstance(this);
            connector.fetch_artikl_u_trgovini(sif_trgovina, barkod, jsonArray -> {
                Log.d("artikl", jsonArray.toString());

                if (jsonArray.length() > 1) {
                    JSONObject opis = null;
                    try {
                        opis = new JSONObject(jsonArray.getJSONObject(1).get("fields").toString());
                        id_opis[0] = jsonArray.getJSONObject(1).get("pk").toString();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    TextView artikl = new TextView(this);
                    artikl.setText(opis.toString());
                    linearLayout.addView(artikl);
                }
            }, jsonArray -> Log.e("err", jsonArray.toString()));

            TextView upvote = new Button(this);
            upvote.setText("Upvote");
            linearLayout.addView(upvote);

            upvote.setOnClickListener(event -> {
                SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                Log.d("shared", (String) sp.getAll().get( (Object) "session"));
                String session_id = (String) sp.getAll().get( (Object) "session");
                connector.upvote(id_opis[0]
                        , session_id, response -> Log.d("res" , response.toString())
                        , response -> Log.e("res", response.toString())
                );
            });

            TextView downvote = new Button(this);
            downvote.setText("Downvote");
            linearLayout.addView(downvote);

            downvote.setOnClickListener(event -> {
                SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                Log.d("shared", (String) sp.getAll().get( (Object) "session"));
                String session_id = (String) sp.getAll().get( (Object) "session");
                connector.downvote(id_opis[0]
                        , session_id, response -> Log.d("res" , response.toString())
                        , response -> Log.e("res", response.toString())
                );
            });

        }

        //Toast.makeText(this, sif_trgovina + " - " + barkod, Toast.LENGTH_SHORT).show();


        TextView textView = new TextView(this);
        textView.setText("Prikaz artikla");
        linearLayout.addView(textView);
        setContentView(linearLayout);
    }
}