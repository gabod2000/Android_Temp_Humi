package io.terasyshub.controllers;

import com.pixplicity.easyprefs.library.Prefs;

import io.terasyshub.constants.APP;

public class AuthController {
    private static AuthController authInstance;

    private AuthController(){
        if (authInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public synchronized static AuthController getInstance(){
        if (authInstance == null) {
            authInstance = new AuthController();

        }
        return authInstance;
    }

    public void setToken (String token) {
        Prefs.putString(APP.PREF_TOKEN, token);
    }

    public String getToken () {
        return Prefs.getString(APP.PREF_TOKEN, "");
    }

    public boolean checkAuth() {
        return !Prefs.getString(APP.PREF_TOKEN, "").equals("");
    }

    public void logout() {
        Prefs.clear();
    }
}
