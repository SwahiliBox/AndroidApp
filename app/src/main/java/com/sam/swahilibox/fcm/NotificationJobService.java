package com.sam.swahilibox.fcm;

import android.os.Bundle;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.sam.swahilibox.model.Notification;
import com.sam.swahilibox.model.NotificationDao;
import com.sam.swahilibox.repositories.NotificationRepository;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class NotificationJobService extends JobService {
    private static final String TAG = "NotificationJobService";
    private final Executor executor = Executors.newFixedThreadPool(2);
    private NotificationDao notificationDao = NotificationRepository.getNotificationDatabase(this).notificationDao();

    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d(TAG, "updating ROOM database with latest data");

        addNotificationDataToSQLiteDatabase(job.getExtras());
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }

    //add data to sqlite database using room
    //should be done on seperate thread
    private void addNotificationDataToSQLiteDatabase(Bundle bundle) {

        final Notification notification = getNotificationObjectFromBundle(bundle);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                long rec = notificationDao.insert(notification);
                Log.d(TAG, "added record to db " + rec);
            }
        });

    }

    private Notification getNotificationObjectFromBundle(Bundle bundle) {
        Notification notification1 = new Notification();
        notification1.setTitle(bundle.getString("title"));
        notification1.setMessage(bundle.getString("message"));
        notification1.setDate(bundle.getString("date"));

        return notification1;

    }
}
