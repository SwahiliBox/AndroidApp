package ke.co.swahilibox.swahilibox.model;


import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;


@Entity(tableName = "notifications")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    public int notification_id;

    public String notification_title;
    public String notification_message;

    public Notification(){

    }

    public Notification(String notification_title, String notification_message) {
        this.notification_title = notification_title;
        this.notification_message = notification_message;
    }

    public void setNotification_id(int notification_id) {
        this.notification_id = notification_id;
    }

    public void setNotification_title(String notification_title) {
        this.notification_title = notification_title;
    }

    public void setNotification_message(String notification_message) {
        this.notification_message = notification_message;
    }

    public int getNotification_id() {
        return notification_id;
    }

    public String getNotification_title() {
        return notification_title;
    }

    public String getNotification_message() {
        return notification_message;
    }
}
