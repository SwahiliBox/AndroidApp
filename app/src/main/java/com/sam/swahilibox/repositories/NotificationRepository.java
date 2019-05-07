package com.sam.swahilibox.repositories;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;

import com.sam.swahilibox.model.Notification;
import com.sam.swahilibox.model.NotificationDao;
import com.sam.swahilibox.model.NotificationDatabase;

import java.util.List;

public class NotificationRepository {

    private static NotificationDatabase notificationsDatabase;
    private NotificationDao notificationDAO;
    private static final Object LOCK = new Object();

    public synchronized static NotificationDatabase getNotificationDatabase(Context context){
        if(notificationsDatabase == null) {
            synchronized (LOCK) {
                if (notificationsDatabase == null) {
                    notificationsDatabase = Room.databaseBuilder(context,
                            NotificationDatabase.class, "notifications db").build();
                }
            }
        }
        return notificationsDatabase;
    }
    public LiveData<List<Notification>> getNotifications(Context context) {
        if (notificationDAO == null) {
            notificationDAO = NotificationRepository.getNotificationDatabase(context).notificationDao();
        }
        return notificationDAO.getNotifications();
    }
    public void deleteMessage(Notification notification){
        new DeleteAsyncTask(notificationDAO).execute(notification);
    }
    public void DeleteAlLMessages(){new DeleteAllNotificationMessages(notificationDAO).execute();}
    //should be performed on background thread
    private static class DeleteAsyncTask extends  AsyncTask<Notification,Void,Void>{
        NotificationDao notificationDao;
        public DeleteAsyncTask(NotificationDao notificationDao){this.notificationDao = notificationDao;}
        @Override
        protected Void doInBackground(Notification... notifications) {
            notificationDao.delete(notifications[0]);
            return null;
        }
    }
    private static class DeleteAllNotificationMessages extends AsyncTask<Void,Void,Void>{
        NotificationDao notificationDao;
        public DeleteAllNotificationMessages(NotificationDao notificationDao){this.notificationDao = notificationDao;}
        @Override
        protected Void doInBackground(Void... voids) {
            notificationDao.deleteAllNotificationMessages();
            return null;
        }
    }
}
