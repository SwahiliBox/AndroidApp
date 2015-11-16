package ke.co.swahilibox.swahilibox.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by japheth on 11/16/15.
 */
public class PrefManager {

    //shared pref filename
    private static final String PREF_NAME = "SwahiliBox";
    //all shared pref keys
    private static final String IS_LOGIN = "IsLoggedIn";
    //email address
    private static final String KEY_EMAIL = "email";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public PrefManager(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    //login session
    public void createLoginSession(String email) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_EMAIL, email);
        editor.commit();
    }

    public String getEmail() {
        return pref.getString(KEY_EMAIL, null);
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void logOut() {
        editor.clear();
        editor.commit();
    }
}
