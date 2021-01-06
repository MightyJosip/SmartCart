package com.example.smartcart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AccountSettingsActivity extends AppCompatActivity implements ChangePasswordDialog.ChangePasswordDialogListener{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.account_settings);

        TextView hi = findViewById(R.id.hiMessage);

        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        hi.setText("Hi, " + sp.getString("auth_level", AuthLevels.DEFAULT));

    }

    public void changePassword(View view) {

        openDialog();
    }

    private void openDialog() {

        ChangePasswordDialog cpd = new ChangePasswordDialog();
        cpd.show(getSupportFragmentManager(), "Password change dialog");
    }


    @Override
    public void getText(String oldPassword, String newPassword, String newPasswordAgain) {

        if(newPassword.equals(newPasswordAgain)){

            SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
            String sessionId = sp.getString("session", "");
            Connector conn = Connector.getInstance(this);
            conn.changePassword(sessionId,
                    oldPassword,
                    newPassword,
                    response -> {
                        Toast.makeText(this, "Password successfully changed!", Toast.LENGTH_SHORT).show();
                        finish();
                        },
                    error -> Toast.makeText(this, "Incorrect old password!", Toast.LENGTH_SHORT).show());
        }
        else{
            Toast.makeText(this,"New password doesn't match!", Toast.LENGTH_LONG).show();
        }
    }
}
