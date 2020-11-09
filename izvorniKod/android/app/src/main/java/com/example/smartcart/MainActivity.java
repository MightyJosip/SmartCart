package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private boolean isFirstLaunch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstLaunch = getIntent().getBooleanExtra("isFirstLaunch", false);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        TextView signup = (TextView) findViewById(R.id.textView3);
        CardView continueAsGuest = (CardView) findViewById(R.id.CardView2);
        // zašto ovo nisu gumbi?

        signup.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);

            }
        });

        continueAsGuest.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor spe = sp.edit();
            // TODO: ovakve stvari kao "Gost" izvući u neku enum ili constants klasu da bude
            // TODO: na jednom mjestu
            spe.putString("NacinPrijave", "Gost");
            spe.apply();
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (isFirstLaunch) {
            finishAffinity();
        }
    }

}