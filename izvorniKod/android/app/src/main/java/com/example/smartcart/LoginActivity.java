package com.example.smartcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private boolean isFirstLaunch = false;
    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirstLaunch = getIntent().getBooleanExtra("isFirstLaunch", false);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        TextView signup = findViewById(R.id.textView3);
        Button continueAsGuest = findViewById(R.id.guestBtn);
        TextView login = findViewById(R.id.loginBtn);
        // zašto ovo nisu gumbi?


        signup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        });

        continueAsGuest.setOnClickListener(v -> {
            SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor spe = sp.edit();
            // TODO: ovakve stvari kao "Gost" izvući u neku enum ili constants klasu da bude
            // TODO: na jednom mjestu
            spe.putString("auth_level", AuthLevels.DEFAULT);
            spe.apply();
            Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
            startActivity(intent);
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


    public void sendData(View v) {
        EditText userNameField = findViewById(R.id.editTextTextPersonName);
        String userName = userNameField.getText().toString();

        EditText passwordField = findViewById(R.id.editTextTextPassword);
        String pw = passwordField.getText().toString();

        Connector conn = Connector.getInstance(this);

        conn.logIn(userName, pw, response -> {

            SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            SharedPreferences.Editor spe = sp.edit();
            try {
                spe.putString("session", response.getString("session_id"));
                spe.putString("auth_level", response.getString("authorisation_level"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            spe.apply();

            Intent intent = new Intent(this, HomeScreenActivity.class);
            intent.putExtra("isFirstLaunch", false);
            startActivity(intent);

            finish();
        }, err -> {
            Toast t = Toast.makeText(this, "NEUSPJEH. Krivo korisničko ime ili lozinka", Toast.LENGTH_LONG);
            t.show();
        });
    }

}