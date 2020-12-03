package com.example.smartcart;

/**
 * Klasa u kojoj se nalaze String konstante koje predstavljaju
 * levele autorizacije na sustavu.
 *
 * Mogu biti intovi, ili enum.
 *
 *
 * */
public class AuthLevels {

    public static final String GOST = "gost";
    public static final String KUPAC = "kupac";
    public static final String TRGOVAC = "trgovac";
    public static final String ADMIN = "admin";
    public static final String DEFAULT = GOST;


    private AuthLevels() {}
}
