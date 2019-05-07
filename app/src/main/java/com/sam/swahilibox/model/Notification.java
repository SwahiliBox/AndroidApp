package com.sam.swahilibox.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "table_notification")
public class Notification {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;

    private String message;

    private String date;

    public Notification(String title, String message,String date) {
        this.title = title;
        this.message = message;
        this.date = date;
    }

    public Notification() {
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }
}

