package ke.co.swahilibox.swahilibox.model;



import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NotificationsDao {

    @Insert
    void insert(Notification notification);
    @Delete
    void delete(Notification notification);

    @Query("DELETE FROM notifications")
    void deleteAllNotifications();

    @Query("SELECT * FROM notifications ORDER BY notification_title DESC")
    LiveData<List<Notification>> displayNotifications();

}
