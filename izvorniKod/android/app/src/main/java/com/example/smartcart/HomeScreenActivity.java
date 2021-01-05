package com.example.smartcart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.smartcart.database.Popis;
import com.example.smartcart.database.SmartCartDatabase;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HomeScreenActivity extends AppCompatActivity{

    private static final int MENU_LOGIN = 1;
    private static final int MENU_SIGNUP = 2;
    private static final int MENU_MYLISTS = 3;
    private static final int MENU_LOGOUT = 4;
    private static final int MENU_ACCOUNTSETTINGS = 5;
    public static FragmentManager fragmentManager;
    private static List<Trgovina> trgovine = new ArrayList<>();
    RecyclerView recyclerView;
    Adapter adapter;
    Connector conn;

    @Override
    protected void onResume() {
        super.onResume();
        conn.fetchTrgovine(response -> {

            Gson gson = new Gson();
            Type storeType = new TypeToken<ArrayList<Object>>(){}.getType();
            List<Object> trgovineHelper = gson.fromJson(response, storeType);

            fetchStores(trgovineHelper);

            drawOnScreenStores();

        }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());
    }

    private void fetchStores(List<Object> trgovineHelper) {

        trgovine.clear();
        for(Object o : trgovineHelper) {
            Map<String, Object> bla = (Map<String, Object>) (((LinkedTreeMap<String, Object>) o).get("fields"));
            trgovine.add(new Trgovina(((Double) ((LinkedTreeMap<String, Object>) o).get("pk")).intValue(), (String) bla.get("naz_trgovina"), (String) bla.get("adresa_trgovina"), Time.valueOf((String) bla.get("radno_vrijeme_pocetak")),
                    Time.valueOf((String) bla.get("radno_vrijeme_kraj")), ((Double) bla.get("vlasnik")).intValue()));
        }
        System.out.println(trgovine);
    }

    private void drawOnScreenStores() {

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter = new Adapter(getApplicationContext(),trgovine, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fragmentManager = getSupportFragmentManager();
        //myAppDatabase = SmartCartDatabase.getInstance(this);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.home_screen);
        recyclerView = findViewById(R.id.storeList);

        conn = Connector.getInstance(this);

        final EditText findStoreOrItem = findViewById(R.id.search_field);

        findStoreOrItem.setOnKeyListener((v, keyCode, event) -> {
            // If the event is a key-down event on the "enter" button
            if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {

                conn.fetch_artikl_u_trgovini("naz_trgovina", findStoreOrItem.getText().toString(), response -> {

                    Gson gson = new Gson();
                    Type storeType = new TypeToken<ArrayList<Object>>(){}.getType();
                    List<Object> trgovineHelper = gson.fromJson(String.valueOf(response), storeType);

                    fetchStores(trgovineHelper);

                    drawOnScreenStores();

                }, error -> Toast.makeText(HomeScreenActivity.this, error.toString(), Toast.LENGTH_SHORT).show());
                return true;
            }
            return false;
        });

        ImageView settingsButton = findViewById(R.id.imageView7);
        registerForContextMenu(settingsButton);
        settingsButton.setOnClickListener(this::openContextMenu);
        settingsButton.setOnLongClickListener(v -> true);



//        if(findViewById(R.id.fragment_container) != null ){
//
//            if( savedInstanceState!= null ){
//                return;
//            }
//
//            fragmentManager.beginTransaction().add(R.id.fragment_container, new AddPopisHomeFragment()).commit();
//        }
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
                Intent intent = new Intent(HomeScreenActivity.this, PrikazPopisaActivity.class);
                startActivity(intent);
                break;
            }

            case MENU_ACCOUNTSETTINGS: startAccountSettingsActivity(); break;
//                Toast.makeText(this, "Nije implementirano :(", Toast.LENGTH_LONG).show();
//                break;

            case MENU_LOGOUT:
                SharedPreferences prefs = getSharedPreferences("user_info", Context.MODE_PRIVATE);
                String sessionId = prefs.getString("session", "");
                Connector conn = Connector.getInstance(this);

                conn.logOut(sessionId, response -> {
                    prefs.edit().remove("session").apply();
                    prefs.edit().remove("auth_level").apply();
                }, error -> Toast.makeText(this, error.toString(), Toast.LENGTH_SHORT).show());

                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
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

    private void startAccountSettingsActivity(){
        Intent intent = new Intent(this, AccountSettingsActivity.class);
        startActivity(intent);
    }

}
