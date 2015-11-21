package ke.co.swahilibox.swahilibox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by japheth on 11/20/15.
 */
public class SwahiliBoxDatasource {

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
}
