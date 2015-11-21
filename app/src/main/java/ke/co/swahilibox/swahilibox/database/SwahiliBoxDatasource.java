package ke.co.swahilibox.swahilibox.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import ke.co.swahilibox.swahilibox.model.Message;

/**
 * Created by japheth on 11/20/15.
 */
public class SwahiliBoxDatasource {
    /**
     * All datatbase base interactions are through this class
     */

    public static final String TAG = SwahiliBoxDatasource.class.getSimpleName();

    SQLiteOpenHelper dbhelper;
    SQLiteDatabase database;

    private static final String[] allColumns = {
            SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_ID,
            SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_TITLE,
            SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_MESSAGE};

    public SwahiliBoxDatasource(Context context) {
        dbhelper = new SwahiliBoxDBOpenHelper(context);
    }

    public void open() {
        Log.i(TAG, "Database opened");
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        Log.i(TAG, "Database closed");
        dbhelper.close();
    }

    public Message createNotification(Message message) {
        ContentValues values = new ContentValues();
        values.put(SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_TITLE, message.getTitle());
        values.put(SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_MESSAGE, message.getMessage());
        long insertId = database.insert(SwahiliBoxDBOpenHelper.TABLE_NOTIFICATIONS, null, values);
        message.setId(insertId);

        return message;
    }

    public List<Message> findAll() {

        Cursor cursor = database.query(SwahiliBoxDBOpenHelper.TABLE_NOTIFICATIONS, allColumns,
                null, null, null, null, null);
        Log.i(TAG, "Returned: " + cursor.getCount());
        List<Message> messages = cursortoList(cursor);
        return messages;
    }

    private List<Message> cursortoList(Cursor cursor) {
        List<Message> messages = new ArrayList<>();
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setId(cursor.getLong(cursor.getColumnIndex(SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_ID)));
                message.setTitle(cursor.getString(cursor.getColumnIndex(SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_TITLE)));
                message.setMessage(cursor.getString(cursor.getColumnIndex(SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_MESSAGE)));

                messages.add(message);
            }
        }

        return messages;
    }

    public boolean deleteMessage(Message message){

        String where = SwahiliBoxDBOpenHelper.COLUMN_NOTIFICATION_ID + "=" + message.getId();
        int result = database.delete(SwahiliBoxDBOpenHelper.TABLE_NOTIFICATIONS , where, null);
        return (result == 1);
    }
}
