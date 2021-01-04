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

import org.json.JSONException;
import org.json.JSONObject;

public class PrikazOpisaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // riješiti preko xml-a
        //setContentView(R.layout.activity_prikaz_opisa);

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Intent intent = getIntent();
        String sif_trgovina = (String) intent.getSerializableExtra("sif_trgovina");
        String barkod = (String) intent.getSerializableExtra("barkod");

        Connector connector = Connector.getInstance(this);
        connector.fetch_opisi(sif_trgovina, barkod, jsonArray -> {
            //Log.d("opis", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Log.d("opis", obj.toString());

                    TextView textView = new TextView(this);
                    textView.setText(obj.toString());
                    linearLayout.addView(textView);

                    // get shared preferences
                    SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                    String session_id = (String) sp.getAll().get((Object) "session");
                    Button button = new Button(this);
                    button.setOnClickListener(l -> {
                        try {
                            connector.upvote(obj.get("pk").toString(), session_id, response -> {
                            }, response -> {
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });

                    linearLayout.addView(button);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, v -> Log.e("opis", v.toString()));

        setContentView(linearLayout);

    }
}