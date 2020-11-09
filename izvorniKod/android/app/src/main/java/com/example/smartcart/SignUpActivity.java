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
        RequestQueue queue = Volley.newRequestQueue(this);
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            Toast.makeText(this , "Can't hash password", Toast.LENGTH_SHORT).show();
            return;
        }
        // TODO: dodati provjeru jesu li lozinke iste, možda ovou obradu izbaciti u zasebnu klasu
        // TODO: dodati mogućnost za trgovca, validaciju
        EditText etPwd = (EditText) findViewById(R.id.edit_password);
        md.update(etPwd.getText().toString().getBytes());
        String pwdDigest = String.format("%064x", new BigInteger(1, md.digest()));

        EditText etEmail = (EditText) findViewById(R.id.edit_email);
        String email = etEmail.getText().toString();

        String url = Constants.HOST + "sign_up?email=" + email + "&password=" + pwdDigest + "&authorisation_level=kupac";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 20 characters of the response string.
                    Toast t = Toast.makeText(this, "Response is: " + response.substring(0, 20), Toast.LENGTH_LONG);
                    t.show();
                }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }
}