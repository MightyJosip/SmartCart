package com.example.smartcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (sp.getBoolean("is_google_signed", false)) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .requestIdToken("197701493351-l5j9llbv8r93kce4ajgf1kque7bcqqhr.apps.googleusercontent.com")
                    .build();
            GoogleSignInClient gsiClient = GoogleSignIn.getClient(this, gso);
            gsiClient.silentSignIn()
                    .addOnCompleteListener(this::handleSignInResult)
                    .addOnCanceledListener(this::startLogInActivity);
            return;
        }
        Log.d("MainActivity", String.valueOf(sp.contains("auth_level")));
        if (!sp.contains("auth_level")) {
            startLogInActivity();
        } else {
            Intent intent = new Intent(this, HomeScreenActivity.class);
            getIntent().putExtra("isFirstLaunch", false); // ??
            startActivity(intent);
        }
    }

    private void startLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.putExtra("isFirstLaunch", true);
        startActivity(intent);
        finish();
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.d("MainActivity", "Handling sign in result");
            SharedPreferences.Editor editor =
                    getSharedPreferences("user_info", Context.MODE_PRIVATE).edit();
            // TODO: provjeriti treba li se tu prvo s ovim tokenom javiti serveru
            editor.putString("session", account.getIdToken());
            editor.putString("auth_level", AuthLevels.KUPAC);
            editor.putBoolean("is_google_signed", true);
            editor.apply();
            startActivity(new Intent(this, HomeScreenActivity.class));
        } catch (ApiException e) {
            startLogInActivity();
        }
    }
}
