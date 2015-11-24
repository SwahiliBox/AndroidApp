package ke.co.swahilibox.swahilibox.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import ke.co.swahilibox.swahilibox.Splash;
import ke.co.swahilibox.swahilibox.database.SwahiliBoxDatasource;
import ke.co.swahilibox.swahilibox.helper.NotificationUtils;
import ke.co.swahilibox.swahilibox.model.Message;

/**
 * Created by japheth on 11/16/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;
    SwahiliBoxDatasource datasource;

    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
        Log.i(TAG, "Initialized the receiver");
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);
        datasource = new SwahiliBoxDatasource(context);
        datasource.open();

        if (intent == null)
            return;

        try {
            JSONObject json = new JSONObject(intent.getExtras().getString("com.parse.Data"));

            Log.e(TAG, "Push received: " + json);

            parseIntent = intent;

            parsePushJson(context, json);

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPushDismiss(Context context, Intent intent) {
        super.onPushDismiss(context, intent);
        datasource.close();
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    /**
     * Parses the push notification json
     *
     * @param context
     * @param json
     */
    private void parsePushJson(Context context, JSONObject json) {
        try {
            boolean isBackground = json.getBoolean("is_background");
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message = data.getString("message");

            /**
             * Create message object
             * set attributes
             * save it
             */
            Message mess = new Message();
            mess.setMessage(message);
            mess.setTitle(title);

            datasource.createNotification(mess);

            Log.i(TAG, "Parsed JSON: " + title + message);

            if (!isBackground) {
                Intent resultIntent = new Intent(context, Splash.class);
                showNotificationMessage(context, title, message, resultIntent);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Push message json exception: " + e.getMessage());
        }
    }

    /**
     * Shows the notification message in the notification bar
     * If the app is in background, launches the app
     *
     * @param context
     * @param title
     * @param message
     * @param intent
     */
    public void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}
