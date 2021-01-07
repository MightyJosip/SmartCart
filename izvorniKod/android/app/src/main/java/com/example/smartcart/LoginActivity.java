package com.example.smartcart;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

public class LoginActivity extends AppCompatActivity {

    private boolean isFirstLaunch = false;
    private static final String TAG = "LoginActivity";

    private GoogleSignInClient gsiClient;
    private static final int RC_GOOGLE = 17;

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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken("197701493351-l5j9llbv8r93kce4ajgf1kque7bcqqhr.apps.googleusercontent.com")
                .build();
        gsiClient = GoogleSignIn.getClient(this, gso);

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
            if (isFirstLaunch) {
                Intent intent = new Intent(LoginActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
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

    @Override
    protected void onStart() {
        super.onStart();
        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(acc);
    }

    private void updateUI(GoogleSignInAccount acc) {
        if (acc == null) {
            // Ako nismo ulogirani, nastavi po starom
        } else {
            SharedPreferences.Editor editor =
                    getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
            // TODO: javiti se serveru
            Log.d(TAG, acc.getIdToken());
            editor.putString("session", acc.getIdToken());
            editor.putString("auth_level", AuthLevels.KUPAC);
            editor.putBoolean("is_google_signed", true);
            editor.apply();
            startActivity(new Intent(this, HomeScreenActivity.class));
            finish();
        }
    }

    public void googleSignIn(View v) {
        Intent intent = gsiClient.getSignInIntent();
        startActivityForResult(intent, RC_GOOGLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_GOOGLE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            updateUI(account);
        } catch (ApiException e) {
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode(), e);
            updateUI(null);
        }
    }

    public void sendData(View v) {
        //Toast.makeText(this, "Hah", Toast.LENGTH_LONG).show();
        EditText userNameField = findViewById(R.id.editTextTextPersonName);
        String userName = userNameField.getText().toString();

        EditText passwordField = findViewById(R.id.editTextTextPassword);
        String pw = passwordField.getText().toString();

        Connector conn = Connector.getInstance(this);

        conn.logIn(userName, pw, response -> {
            //Toast t = Toast.makeText(this, "Uspjeh. Poruka: " + response.toString(), Toast.LENGTH_LONG);
            //t.show();

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