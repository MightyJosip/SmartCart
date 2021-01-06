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

    public static final String GOST = "Gost";
    public static final String KUPAC = "Kupac";
    public static final String TRGOVAC = "Trgovac";
    public static final String ADMIN = "Admin";
    public static final String DEFAULT = GOST;


    private AuthLevels() {}
}
