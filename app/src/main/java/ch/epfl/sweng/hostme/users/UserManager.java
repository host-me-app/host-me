package ch.epfl.sweng.hostme.users;

import static ch.epfl.sweng.hostme.utils.Constants.KEY_PREFERENCE_NAME;

import android.content.Context;
import android.content.SharedPreferences;

public class UserManager {

    private final SharedPreferences sharedPreferences;

    public UserManager(Context context) {
        sharedPreferences = context.getSharedPreferences(KEY_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * put string in the shared preferences
     * @param key of the editor
     * @param value to be set
     */
    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    /**
     * get valie from the corresponding key in the shared
     * preferences
     * @param key to retrieve
     * @return the value that corresponds to the key
     */
    public String getString(String key) {
        return sharedPreferences.getString(key, null);
    }

    /**
     * clear the shared preferences
     */
    public void clear() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
