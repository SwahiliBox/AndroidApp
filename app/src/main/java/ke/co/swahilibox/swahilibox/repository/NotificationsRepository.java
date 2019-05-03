package ke.co.swahilibox.swahilibox.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;


import java.util.List;

import ke.co.swahilibox.swahilibox.model.Notification;
import ke.co.swahilibox.swahilibox.model.NotificationsDao;
import ke.co.swahilibox.swahilibox.model.NotificationsDatabase;

public class NotificationsRepository {

    private NotificationsDao notificationsDao;
    private LiveData<List<Notification>> listNotifications;

    public NotificationsRepository(Application application){
        NotificationsDatabase notificationsDatabase = NotificationsDatabase.getInstance(application);
        notificationsDao = notificationsDatabase.notificationsDao();
        listNotifications = notificationsDao.displayNotifications();
    }

    public LiveData<List<Notification>> getAllNotifications(){
        return listNotifications;
    }

    public void insert(Notification notification){new InsertNotificationAsyncTask(notificationsDao).execute(notification);}

    public void delete(Notification notification){new DeleteNotificationAsyncTask(notificationsDao).execute(notification);}

    public void deleteAll(){new DeleteAllNotificationsAsyncTask(notificationsDao).execute();}

    private static class DeleteNotificationAsyncTask extends AsyncTask<Notification, Void,Void>{
        private NotificationsDao notificationsDao;

        public DeleteNotificationAsyncTask(NotificationsDao notificationsDao){this.notificationsDao = notificationsDao;}
        @Override
        protected Void doInBackground(Notification... notifications) {
            notificationsDao.delete(notifications[0]);
            return null;

        }
    }

    private static class InsertNotificationAsyncTask extends AsyncTask<Notification, Void,Void>{
        private NotificationsDao notificationsDao;

        public InsertNotificationAsyncTask(NotificationsDao notificationsDao){this.notificationsDao = notificationsDao;}
        @Override
        protected Void doInBackground(Notification... notifications) {
            notificationsDao.delete(notifications[0]);
            return null;

        }
    }

    private static class DeleteAllNotificationsAsyncTask extends AsyncTask<Void,Void, Void>{

        private NotificationsDao notificationsDao;

        public DeleteAllNotificationsAsyncTask(NotificationsDao notificationsDao){this.notificationsDao = notificationsDao;}

        @Override
        protected Void doInBackground(Void... voids) {
            notificationsDao.deleteAllNotifications();
            return null;
        }
    }
}
