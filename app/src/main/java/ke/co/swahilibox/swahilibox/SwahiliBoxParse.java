package ke.co.swahilibox.swahilibox;

import android.app.Application;
import android.util.Log;

import com.parse.ParseACL;
import com.parse.ParseUser;

import ke.co.swahilibox.swahilibox.helper.ParseUtil;

/**
 * Created by japheth on 11/16/15.
 */
public class SwahiliBoxParse extends Application {
    /**
     * This is an application class, executed on app launch
     * Parse is initialized here
     */
    private static SwahiliBoxParse mInstance;

    public static synchronized SwahiliBoxParse getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("SwahiliBoxParse", "SwahiliBoxParse on create");

        mInstance = this;
        //register with parse
        ParseUtil.registerParse(getApplicationContext());
        ParseUser.enableAutomaticUser();
        ParseACL defaultAcl = new ParseACL();

        defaultAcl.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultAcl, true);
    }
}
