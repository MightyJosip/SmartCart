package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.Connector;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.font.TextAttribute;

import kotlin.reflect.KVariance;

public class PrikazTrgovine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz_trgovine);

        // koristi se ovaj programski layout, valja prebaciti na xml
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
                    Log.d("trgovina", jsonArray.getJSONObject(i).toString());
                    TextView textView = new TextView(this);
                    textView.setText(jsonArray.getJSONObject(i).get("fields").toString());
                    linearLayout.addView(textView);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, obj -> Log.e("err", obj.toString()));



        setContentView(linearLayout);
    }
}