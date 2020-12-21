package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartcart.database.Popis;
import com.example.smartcart.database.SmartCartDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class HomeScreenActivity extends AppCompatActivity{

    private static final int MENU_LOGIN = 1;
    private static final int MENU_SIGNUP = 2;
    private static final int MENU_MYLISTS = 3;
    private static final int MENU_LOGOUT = 4;
    private static final int MENU_ACCOUNTSETTINGS = 5;
    public static FragmentManager fragmentManager;
    //public static SmartCartDatabase myAppDatabase;

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
        if (!sp.contains("auth_level")) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.putExtra("isFirstLaunch", true);
            startActivity(intent);
        }
        Connector conn = Connector.getInstance(this);
        conn.fetchTrgovine(response -> {
            Toast.makeText(this, response.toString(), Toast.LENGTH_SHORT).show();
        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentManager = getSupportFragmentManager();
        //myAppDatabase = SmartCartDatabase.getInstance(this);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.home_screen);



        // TODO: sve na što korisnik kika bi trebalo biti "button" po PS-u, ali ako vas ne smeta,
        // TODO: neka ostane
        ImageView settingsButton = (ImageView) findViewById(R.id.imageView7);
        registerForContextMenu(settingsButton);
        settingsButton.setOnClickListener(this::openContextMenu);
        settingsButton.setOnLongClickListener(v -> true);



        if(findViewById(R.id.fragment_container) != null ){

            if( savedInstanceState!= null ){
                return;
            }

            fragmentManager.beginTransaction().add(R.id.fragment_container, new AddPopisHomeFragment()).commit();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        /*
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        */
        super.onCreateContextMenu(menu, v, menuInfo);

        String authLevel = getAuthLevel();

        // treba dodati još opcija
        boolean enableLogin = false;
        boolean enableSignup = false;
        boolean enableMyLists = true;
        boolean enableLogout = false;
        boolean enableAccountSettings = true;

        switch(authLevel) {
            case AuthLevels.GOST:
                enableLogin = true;
                enableSignup = true;
                enableAccountSettings = false;
                break;

            case AuthLevels.KUPAC:
            case AuthLevels.TRGOVAC:
            case AuthLevels.ADMIN:
                enableLogout = true;
                break;
        }


        if (enableLogin)
            menu.add(Menu.NONE, MENU_LOGIN, Menu.NONE, R.string.log_in);
        if (enableSignup)
            menu.add(Menu.NONE, MENU_SIGNUP, Menu.NONE, R.string.sign_up);
        if (enableMyLists)
            menu.add(Menu.NONE, MENU_MYLISTS, Menu.NONE, "Moji popisi"); //treba dodati to u Strings konstante
        if (enableLogout)
            menu.add(Menu.NONE, MENU_LOGOUT, Menu.NONE, "Log out"); //treba dodati to u Strings konstante
        if (enableAccountSettings)
            menu.add(Menu.NONE, MENU_ACCOUNTSETTINGS, Menu.NONE, "Postavke računa"); //treba dodati to u Strings konstante



    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        switch(itemId) {
            case MENU_LOGIN: startLogInActivity(); break;
            case MENU_SIGNUP: startSignUpActivity(); break;
            case MENU_MYLISTS: {
                StringBuilder sb = new StringBuilder();
                List<Popis> svi = SmartCartDatabase.getInstance(this)
                            .popisDao().dohvatiSvePopise();
                for (Popis p : svi)
                    sb.append(p).append('\n');
                String sviStr = sb.toString();
                Toast.makeText(this, sviStr, Toast.LENGTH_LONG).show();
                break;
            }

            case MENU_ACCOUNTSETTINGS:
                Toast.makeText(this, "Nije implementirano :(", Toast.LENGTH_LONG).show();
                break;

            case MENU_LOGOUT:
                SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String sessionId = prefs.getString("session", "");
                Connector conn = Connector.getInstance(this);

                conn.logOut(sessionId, response -> {
                    prefs.edit().remove("session").apply();
                    prefs.edit().remove("auth_level").apply();
                }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

        }
        return super.onContextItemSelected(item);
    }


    public void startLogInActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void startSignUpActivity() {
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private String getAuthLevel() {
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);

        return sp.getString("auth_level", AuthLevels.DEFAULT);
    }


}
