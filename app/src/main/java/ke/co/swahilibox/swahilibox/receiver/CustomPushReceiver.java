package ke.co.swahilibox.swahilibox.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.parse.ParsePushBroadcastReceiver;

import java.util.List;

import ke.co.swahilibox.swahilibox.views.Splash;
import ke.co.swahilibox.swahilibox.model.ApiService;
import ke.co.swahilibox.swahilibox.utils.NotificationUtils;
import ke.co.swahilibox.swahilibox.model.Messages;
import ke.co.swahilibox.swahilibox.views.Main;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by japheth on 11/16/15.
 */
public class CustomPushReceiver extends ParsePushBroadcastReceiver {

    private final String TAG = CustomPushReceiver.class.getSimpleName();

    private NotificationUtils notificationUtils;


    private Intent parseIntent;

    public CustomPushReceiver() {
        super();
        Log.i(TAG, "Initialized the receiver");
    }

    @Override
    protected void onPushReceive(Context context, Intent intent) {
        super.onPushReceive(context, intent);

        if (intent == null)
            return;

        Main main = new Main();
        main.getData();
    }

    @Override
    protected void onPushOpen(Context context, Intent intent) {
        super.onPushOpen(context, intent);
    }

    private void parsePushJson() {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://dummy.restapiexample.com")//fake data
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);
            Observable<List<Messages>> observable = apiService.getMessages().subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

            observable.subscribe(new rx.Observer<List<Messages>>() {

                private String message;
                private String title;
                Context context;

                @Override
                public void onCompleted() {
                    //a callback when the task gets completed
                }

                @Override
                public void onError(Throwable e) {
                    Log.e(TAG, "The Error is: " + e.getMessage());
                }

                @Override
                public void onNext(List<Messages> messages) {

                    //testing with fakedata

                    for (int i = 0; i < messages.size(); i++) {
                        title = messages.get(i).getName();
                        message = messages.get(i).getSalary();


                       //notification will be showed every time a new item appears
                        Intent resultIntent = new Intent(context, Splash.class);
                        showNotificationMessage(context, title, message, resultIntent);
                    }

                }
            });


        } catch (Exception e) {
            Log.e(TAG, "Push message exception: " + e.getMessage());
        }
    }

    public void showNotificationMessage(Context context, String title, String message, Intent intent) {

        notificationUtils = new NotificationUtils(context);

        intent.putExtras(parseIntent.getExtras());

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        notificationUtils.showNotificationMessage(title, message, intent);
    }
}
