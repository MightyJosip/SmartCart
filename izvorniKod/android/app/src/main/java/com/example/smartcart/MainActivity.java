package com.example.smartcart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (!sp.contains("auth_level")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("isFirstLaunch", true);
            startActivity(intent);

        }
        else{
            Intent intent = new Intent(this, HomeScreenActivity.class);
            getIntent().putExtra("isFirstLaunch", false);
            startActivity(intent);
        }
    }
}
