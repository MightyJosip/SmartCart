package com.example.smartcart;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Connector {

    private static final String HOST = "http://10.0.2.2:8000/";

    private static Connector singleInstance;
    private static RequestQueue requestQueue;
    private static Context appContext;

    private Connector(Context c) {
        appContext = c;
        requestQueue = getRequestQueue();
    }

    public static RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(appContext);
        }
        return requestQueue;
    }

    public static Connector getInstance(Context c) {
        if (singleInstance == null) {
            singleInstance = new Connector(c.getApplicationContext());
        }
        return singleInstance;
    }

    public void logIn(String email, String password, Response.Listener<JSONObject> onSuccess,
                      Response.ErrorListener onFail) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("password", password);
        } catch (JSONException e) {
            // za printati stacktrace napraviti stringwriter/printwriter wrapper i upisati u string
            Log.e("Login", e.toString());
        }
        String url = HOST + "android/login";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jo, onSuccess, onFail);

        getRequestQueue().add(jor);
    }

    public void signUp(String email, String password, int secretCode,
                       Response.Listener<JSONObject> onSuccess, Response.ErrorListener onFail) {
        JSONObject jo = new JSONObject();
        try {
            jo.put("email", email);
            jo.put("password", password);
            jo.put("secretCode", secretCode);
        } catch (JSONException e) {
            // za printati stacktrace napraviti stringwriter/printwriter wrapper i upisati u string
            Log.e("Signup", e.toString());
        }
        String url = HOST + "android/signup";
        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.POST, url, jo, onSuccess, onFail);

        getRequestQueue().add(jor);

    }
}
