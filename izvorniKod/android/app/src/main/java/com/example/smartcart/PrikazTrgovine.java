package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartcart.Connector;

import java.awt.font.TextAttribute;

public class PrikazTrgovine extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikaz_trgovine);

        // koristi se ovaj programski layout, valja prebaciti na xml
        LinearLayout linearLayout = new LinearLayout(this);

        Intent intent = getIntent();
        String name = "";
        if (intent.hasExtra("name")) {
            name = (String)  intent.getSerializableExtra("name");
            TextView textView = new TextView(this);
            textView.setText(name);
            linearLayout.addView(textView);
        }
        setContentView(linearLayout);



        String id = name.split(" ")[2];
        Toast.makeText(this, id, Toast.LENGTH_SHORT).show();


    }
}