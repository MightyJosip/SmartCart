package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        setContentView(R.layout.activity_prikaz_artikla);

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


            Button dodajnapopis = (Button) findViewById(R.id.btn_dodaj_na_popis);
            //JSONObject finalOpis = opis;

            SharedPreferences sp1 = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            if(sp1.getString("auth_level", AuthLevels.DEFAULT).equals(AuthLevels.DEFAULT)){
                dodajnapopis.setVisibility(View.GONE);
            }

            dodajnapopis.setOnClickListener(v -> {


                Intent intent2 = new Intent(PrikazArtikla.this, Odabir_popisa.class);
                intent2.putExtra("sif_trgovina", sif_trgovina);
                intent2.putExtra("barkod", barkod);
                            /*try {
                                intent2.putExtra("naziv", finalOpis.get("naziv_artikla").toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }*/
                startActivity(intent2);



            });

            final String[] id_opis = {null};
            Connector connector = Connector.getInstance(this);
            connector.fetch_artikl_u_trgovini(sif_trgovina, barkod, jsonArray -> {
                Log.d("artikl", jsonArray.toString());

                if (jsonArray.length() > 1) {
                    JSONObject opis = null;
                    try {
                        opis = new JSONObject(jsonArray.getJSONObject(1).get("fields").toString());
                        id_opis[0] = jsonArray.getJSONObject(1).get("pk").toString();

                        TextView txt_naziv = (TextView) findViewById(R.id.txt_naziv);
                        TextView txt_opis = (TextView) findViewById(R.id.txt_opis);
                        TextView txt_broj_glasova = (TextView) findViewById(R.id.txt_broj_glasova);

                        txt_naziv.setText(opis.get("naziv_artikla").toString());
                        txt_opis.setText(opis.get("opis_artikla").toString());
                        txt_broj_glasova.setText("Broj glasova : " + opis.get("broj_glasova").toString());




                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    TextView artikl = new TextView(this);
                    artikl.setText(opis.toString());
                    linearLayout.addView(artikl);
                } else {
                    // ako nema opisa
                    TextView txt_opis = (TextView) findViewById(R.id.txt_opis);
                    txt_opis.setText("Ovaj artikl nema opisa");
                    // TODO: korisniku dodati mogućnost da doda opis
                }
            }, jsonArray -> Log.e("err", jsonArray.toString()));


            Button upvote = (Button) findViewById(R.id.btn_upvote);

            upvote.setOnClickListener(event -> {
                SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                if(sp.getString("auth_level", AuthLevels.DEFAULT).equals(AuthLevels.DEFAULT)){
                    Intent intentLogin = new Intent(PrikazArtikla.this, LoginActivity.class);
                    startActivity(intentLogin);
                }
                sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                Log.d("shared", (String) sp.getAll().get( (Object) "session"));
                String session_id = (String) sp.getAll().get( (Object) "session");
                connector.upvote(id_opis[0]
                        , session_id, response -> Log.d("res" , response.toString())
                        , response -> Log.e("res", response.toString())
                );
            });


            Button downvote = (Button) findViewById(R.id.btn_downvote);

            downvote.setOnClickListener(event -> {
                SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                if(sp.getString("auth_level", AuthLevels.DEFAULT).equals(AuthLevels.DEFAULT)){
                    Intent intentLogin = new Intent(PrikazArtikla.this, LoginActivity.class);
                    startActivity(intentLogin);
                }

                sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String session_id = (String) sp.getAll().get( (Object) "session");
                Log.d("shared", session_id);
//                String session_id = (String) sp.getAll().get( (Object) "session");
                if (id_opis == null) return;

                connector.downvote(id_opis[0]
                        , session_id, response -> Log.d("res" , response.toString())
                        , response -> Log.e("res", response.toString())
                );

                // nakon downvote-a slijedi prikaz alternativnih opisa
                Intent intent1 = new Intent(PrikazArtikla.this, PrikazOpisaActivity.class);
                intent1.putExtra("sif_trgovina", sif_trgovina);
                intent1.putExtra("barkod", barkod);
                startActivity(intent1);
            });



        }



    }
}