package com.sam.swahilibox.model;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = Notification.class,version = 1)
public abstract class NotificationDatabase extends RoomDatabase {

    public abstract NotificationDao notificationDao();

}
