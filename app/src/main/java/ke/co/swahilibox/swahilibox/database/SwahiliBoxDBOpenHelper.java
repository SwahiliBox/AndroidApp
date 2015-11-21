package ke.co.swahilibox.swahilibox.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by japheth on 11/20/15.
 */
public class SwahiliBoxDBOpenHelper extends SQLiteOpenHelper {

    private static final String TAG = SwahiliBoxDBOpenHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "SwahiliBox";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NOTIFICATIONS = "notifications";
    public static final String COLUMN_NOTIFICATION_ID = "notification_id";
    public static final String COLUMN_NOTIFICATION_TITLE = "notification_title";
    public static final String COLUMN_NOTIFICATION_MESSAGE = "notification_message";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NOTIFICATIONS + " (" +
                    COLUMN_NOTIFICATION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_NOTIFICATION_TITLE + " TEXT, " +
                    COLUMN_NOTIFICATION_MESSAGE + " TEXT " + ")";

    public SwahiliBoxDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
        Log.i(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        onCreate(sqLiteDatabase);
    }
}
