package com.nikola.task.utils;

/**
 * Constants
 *
 * @author Nikola Aleksic
 */
public class Constants {

    /**
     * Login screen constants
     */
    public final static String EMAIL_KEY = "email";
    public final static String EMAIL_VALUE = "test@testmenu.com";
    public final static String PASSWORD_KEY = "password";
    public final static String PASSWORD_VALUE = "test1234";
    public final static String URL_BASE_LOGIN = "https://usemenu.com/playground/public/api/v2/customer/login?app_version=2.8.1";

    /**
     * Details screen constants
     */
    public final static String TABLE_BEACON = "table_beacon";
    public final static String MAJOR_KEY = "major";
    public final static int MAJOR_VALUE = 5;
    public final static String MINOR_KEY = "minor";
    public final static int MINOR_VAlUE = 1;
    public final static String ACCESS_TOKEN_KEY = "access_token";
    public final static String URL_BASE_DETAILS = "https://usemenu.com/playground/public/api/v2/restaurant/info?app_version=2.8.1";

    /**
     * Splash screen constants
     */
    public final static int SPLASH_TIMEOUT_DELAY = 2000;

    /**
     * Shared preferences constants
     */
    public static final String SHARED_PREFS = "SHARED_PREFS";
    public static final String TOKEN_KEY = "TOKEN_KEY";
    public static final String LOGIN_KEY = "LOGIN_KEY";

    /**
     * Volley constants
     */
    public final static String VOLLEY_REQUEST = "VOLLEY_REQUEST";
    public final static int VOLLEY_TIMEOUT_SOCKET = 10000;
}
