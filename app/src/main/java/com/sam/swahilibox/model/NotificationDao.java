package com.sam.swahilibox.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotificationDao {

    @Insert
    long insert(Notification notification);

    @Update
    void update(Notification notification);

    @Delete
    void delete(Notification notification);

    @Query("DELETE FROM table_notification")
    void deleteAllNotificationMessages();

    @Query("SELECT * FROM table_notification Order By date DESC")
    LiveData<List<Notification>> getNotifications();
}
