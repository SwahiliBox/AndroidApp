package com.sam.swahilibox.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.sam.swahilibox.model.Notification;
import com.sam.swahilibox.repositories.NotificationRepository;

import java.util.List;

public class NotificationViewModel extends AndroidViewModel {
    private NotificationRepository repository;
    private LiveData<List<Notification>> allNotes;

    public NotificationViewModel(@NonNull Application application) {
        super(application);
        repository = new NotificationRepository();
        allNotes = repository.getNotifications(application);
    }
    public LiveData<List<Notification>> getAllNotes(){
        return allNotes;
    }
    public void delete(Notification notification){repository.deleteMessage(notification);}
    public void deleteAll(){repository.DeleteAlLMessages();}
}

