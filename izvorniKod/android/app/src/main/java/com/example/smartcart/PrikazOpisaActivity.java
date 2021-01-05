package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.LinkedList;
import java.util.List;

public class PrikazOpisaActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    OpisAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // riješiti preko xml-a

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        Intent intent = getIntent();
        String sif_trgovina = (String) intent.getSerializableExtra("sif_trgovina");
        String barkod = (String) intent.getSerializableExtra("barkod");

        Connector connector = Connector.getInstance(this);
        List<JSONObject> opisi = new LinkedList<JSONObject>();
        connector.fetch_opisi(sif_trgovina, barkod, jsonArray -> {
            //Log.d("opis", jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject tmp = new JSONObject(obj.get("fields").toString());
                    // add "sif_opis" into "fields" and send "fields" to OpisAdapter
                    tmp.accumulate("sif_opis", obj.get("pk"));
                    Log.d("aaaaargh", tmp.toString());
                    opisi.add(tmp);
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
            Log.d("opisi", opisi.toString());
            recyclerView = findViewById(R.id.opisi_list);
            draw_opisi(opisi);

        }, v -> Log.e("opis", v.toString()));

        setContentView(R.layout.activity_prikaz_opisa);
        //setContentView(linearLayout);

    }

    private void draw_opisi(List<JSONObject> opisi) {
        // get session_id from shared preferences for upvote button
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String session_id = (String) sp.getAll().get((Object) "session");

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new OpisAdapter(getApplicationContext(), opisi , this, session_id);
        recyclerView.setAdapter(adapter);
    }

}

