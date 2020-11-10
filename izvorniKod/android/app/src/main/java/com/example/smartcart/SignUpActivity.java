package com.example.smartcart;

        import androidx.appcompat.app.AppCompatActivity;

        import android.os.Bundle;
        import android.view.View;
        import android.view.Window;
        import android.widget.EditText;
        import android.widget.Toast;

        import com.android.volley.Request;
        import com.android.volley.RequestQueue;
        import com.android.volley.Response;
        import com.android.volley.VolleyError;
        import com.android.volley.toolbox.StringRequest;
        import com.android.volley.toolbox.Volley;

        import java.math.BigInteger;
        import java.security.MessageDigest;
        import java.security.NoSuchAlgorithmException;

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

        int secret;

        EditText etEmail = (EditText) findViewById(R.id.edit_email);
        String email = etEmail.getText().toString();

        EditText etSecret = (EditText) findViewById(R.id.edit_secret);
        if(etSecret.getText().toString().equals("")){
            secret = 0;
        }

        else{
            secret = Integer.parseInt(etSecret.getText().toString());
        }



        EditText etPassword = findViewById(R.id.edit_password);
        String password = etPassword.getText().toString();

        EditText etPassword2 = findViewById(R.id.edit_password2);
        String password2 = etPassword2.getText().toString();


        if (password.equals(password2)){
            Toast t1 = Toast.makeText(this, "Passwords are matching", Toast.LENGTH_LONG);
            t1.show();

          Connector conn = Connector.getInstance(this);
            conn.signUp(email, password, secret, response -> {
                // Display the first 20 characters of the response string.
                Toast t2 = Toast.makeText(this, "Response is: " + response.toString(), Toast.LENGTH_LONG);
                t2.show();
            }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

        }

        else{
            Toast t3 = Toast.makeText(this, "Passwords are not matching, try again", Toast.LENGTH_LONG);
            t3.show();
        }


    }
}