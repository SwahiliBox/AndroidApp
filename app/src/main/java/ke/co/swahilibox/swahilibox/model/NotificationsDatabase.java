package ke.co.swahilibox.swahilibox.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;


@Database(entities = Notification.class, version = 1)
public abstract class NotificationsDatabase extends RoomDatabase {

    private static NotificationsDatabase instance;
    public abstract NotificationsDao notificationsDao();
    public static synchronized NotificationsDatabase getInstance(Context context){

        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotificationsDatabase.class,"swahilibox")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }



}
