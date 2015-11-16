package ke.co.swahilibox.swahilibox.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONObject;

import ke.co.swahilibox.swahilibox.helper.NotificationUtils;
import ke.co.swahilibox.swahilibox.helper.SBJSONParser;

/**
 * Created by japheth on 11/16/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private static String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;
    private Intent intent;

    public CustomPushReceiver() {
        super();
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        if (intent == null) return;

        try {

            JSONObject jsonObject = new JSONObject(intent.getExtras().getString("com.parse.Data"));
            Log.i(TAG, "Push received: " + jsonObject);
            this.intent = intent;
            SBJSONParser.parsePushJson(context, jsonObject);
        } catch (Exception e) {
            Log.e(TAG, "Push message exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    public void showNotificationMessage(Context context, String title, String message, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.putExtras(this.intent.getExtras());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        notificationUtils.showNotificationMessage(title, message, intent);
    }


}
