package ke.co.swahilibox.swahilibox.helper;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

import ke.co.swahilibox.swahilibox.AppConfig;

/**
 * Created by japheth on 11/15/15.
 */
public class ParseUtil {
    /**
     * Contains utility methods to interact with the parse api,ie
     * initializing parse, subscribing using email to send individual notifications
     */
    private static String TAG = ParseUtil.class.getSimpleName();

    public static void verifyParseConfiguration(Context context) {
        if (TextUtils.isEmpty(AppConfig.PARSE_APPLICATION_ID) || TextUtils.isEmpty(AppConfig.PARSE_CLIENT_KEY))
            Toast.makeText(context, "Please configure your app", Toast.LENGTH_SHORT).show();
        ((Activity) context).finish();
    }

    public static void registerParse(Context context) {
        // initializing parse library
        Parse.initialize(context, AppConfig.PARSE_APPLICATION_ID, AppConfig.PARSE_CLIENT_KEY);
        PushService.startServiceIfRequired(context);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        ParsePush.subscribeInBackground(AppConfig.PARSE_CHANNEL, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "Successfully subscribed to Parse!");
            }
        });
    }


    public static void subscribeWithEmail(String email) {
        ParseInstallation installation = new ParseInstallation();
        installation.put("email", email);
        installation.saveInBackground();
    }
}
