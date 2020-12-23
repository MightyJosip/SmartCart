package com.example.smartcart;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Toast;

public class PrikazStavkiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.prikaz_stavki);

        final Intent intent = getIntent();
        final int myExtra= intent.getIntExtra("id", -1);
        Toast.makeText(this, Integer.toString(myExtra), Toast.LENGTH_SHORT)
                .show();





    }
}