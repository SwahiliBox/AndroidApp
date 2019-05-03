package ke.co.swahilibox.swahilibox.ViewModels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;


import java.util.List;

import ke.co.swahilibox.swahilibox.model.Notification;
import ke.co.swahilibox.swahilibox.repository.NotificationsRepository;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationsRepository notificationsRepository;
    private LiveData<List<Notification>> listNotifications;
    public NotificationViewModel(@NonNull Application application) {
        super(application);
        notificationsRepository = new NotificationsRepository(application);
        listNotifications = notificationsRepository.getAllNotifications();
    }

    public LiveData<List<Notification>> getAllNotifications(){return listNotifications;}
    public void insert(Notification notification){notificationsRepository.insert(notification);}
    public void DeleteAllNotifications(){notificationsRepository.deleteAll();}
    public void DeleteNotification(Notification notification){
        notificationsRepository.delete(notification);
    }
}
