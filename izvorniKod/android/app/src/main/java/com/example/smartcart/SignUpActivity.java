package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_signup);
    }

    public void sendData(View v) {
        // TODO: dodati provjeru jesu li lozinke iste
        // TODO: dodati mogućnost za trgovca, validaciju

        EditText etEmail = findViewById(R.id.edit_email);
        String email = etEmail.getText().toString();

        EditText etSecret = findViewById(R.id.edit_secret);

        int secret = etSecret.getText().toString().equals("") ? 0 : Integer.parseInt(etSecret.getText().toString());

        EditText etPassword = findViewById(R.id.edit_password);
        String password = etPassword.getText().toString();

        EditText etPassword2 = findViewById(R.id.edit_password2);
        String password2 = etPassword2.getText().toString();

        if (password.equals(password2)){

          Connector conn = Connector.getInstance(this);
            conn.signUp(email, password, secret, response -> finish(), error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());
        }
        else{
            Toast t3 = Toast.makeText(this, "Passwords are not matching, try again", Toast.LENGTH_LONG);
            t3.show();
        }
    }
}