package ke.co.swahilibox.swahilibox.helper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONObject;

import ke.co.swahilibox.swahilibox.Main;
import ke.co.swahilibox.swahilibox.receiver.CustomPushReceiver;

/**
 * Created by japheth on 11/16/15.
 */
public class SBJSONParser {

    private static final String TAG = SBJSONParser.class.getSimpleName();
    private NotificationUtils notificationUtils;

    public static void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            if (!isBackground) {
                Intent intent = new Intent(context, Main.class);
                new CustomPushReceiver().showNotificationMessage(context, title, message, intent);
            }
        } catch (Exception e) {
            Log.e(TAG, "An error ocurred during parsing");
        }
    }
}
