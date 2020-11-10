package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class HomeScreenActivity extends AppCompatActivity{

    /**
     * Provjerava je li korisnik odabrao hoće li se prijaviti. Ako nije, otvara se MainActivity kako
     * bi mogao odabrati.
     * Tamo se pritisak na back presreće da se ne vrati nazad ovdje
     * Alternativni način:
     * https://android.jlelse.eu/login-and-main-activity-flow-a52b930f8351
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        if (!sp.contains("NacinPrijave")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("isFirstLaunch", true);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.home_screen);



        // TODO: sve na što korisnik kika bi trebalo biti "button" po PS-u, ali ako vas ne smeta,
        // TODO: neka ostane
        ImageView cartButton = (ImageView) findViewById(R.id.imageView7);
        registerForContextMenu(cartButton);
        cartButton.setOnClickListener(this::openContextMenu);
        cartButton.setOnLongClickListener(v -> true);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
    }

    public void startLogInActivity(MenuItem mi) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startSignUpActivity(MenuItem mi) {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }


}
