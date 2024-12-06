package io.vn.dungxnd.duckabary.domain.service;

import java.util.prefs.Preferences;

public class PreferencesManager {
    private static final Preferences prefs =
            Preferences.userNodeForPackage(PreferencesManager.class);
    private static final String KEY_REMEMBERED_USERNAME = "remembered_username";

    public static void saveLoginCredentials(String username) {
        prefs.put(KEY_REMEMBERED_USERNAME, username);
    }

    public static void clearLoginCredentials() {
        prefs.remove(KEY_REMEMBERED_USERNAME);
    }

    public static String getRememberedUsername() {
        return prefs.get(KEY_REMEMBERED_USERNAME, null);
    }
}
