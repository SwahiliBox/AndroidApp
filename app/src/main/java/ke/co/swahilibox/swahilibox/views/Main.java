package ke.co.swahilibox.swahilibox.views;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import ke.co.swahilibox.swahilibox.DataBinding.NotificationAdapter;
import ke.co.swahilibox.swahilibox.R;
import ke.co.swahilibox.swahilibox.ViewModels.NotificationViewModel;
import ke.co.swahilibox.swahilibox.model.ApiService;
import ke.co.swahilibox.swahilibox.model.Messages;
import ke.co.swahilibox.swahilibox.model.Notification;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class Main extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Intent intent = null;
    RecyclerView mNotificationList;

    private static final String TAG = "Main";

    private NotificationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);



        mNotificationList = findViewById(R.id.list_notifications);
        mNotificationList.setLayoutManager(new LinearLayoutManager(this));
        mNotificationList.setHasFixedSize(true);

        final NotificationAdapter adapter = new NotificationAdapter();
        mNotificationList.setAdapter(adapter);

        viewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        viewModel.getAllNotifications().observe(this,(new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                /*power of RxJava*/
                //update recyclerview

                adapter.submitList(notifications);
            }
        }));

        getData();

        //swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewModel.DeleteNotification(adapter.getNotificationAt(viewHolder.getAdapterPosition()));
                Toast.makeText(Main.this, "Notification Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(mNotificationList);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.message_list_item_context,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id== R.id.delete_message){
            viewModel.DeleteAllNotifications();
            Toast.makeText(this, "Notification Deleted Successfully!!", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.invite) {
            // Handle the camera action
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, you are invited to join SwahiliBox. " +
                    "Check our website http://www.swahilibox.co.ke and download the " +
                    "SwahiliBox App to stay upto date");
            startActivity(intent);

        } else if (id == R.id.about_us) {

            intent = new Intent(this, AboutUs.class);
            startActivity(intent);
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();

        } else if (id == R.id.log_out) {
            
            intent = new Intent(this, LogIn.class);
            startActivity(intent);
            overridePendingTransition(R.transition.push_down_in, R.transition.push_down_out);
            finish();


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dummy.restapiexample.com")//fake data
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);
        Observable<List<Messages>> observable = apiService.getMessages().subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new rx.Observer<List<Messages>>() {
            @Override
            public void onCompleted() {
                //a callback when the task gets completed
            }

            @Override
            public void onError(Throwable e) {
                Log.e(TAG,"The Error is: "+e.getMessage());
            }

            @Override
            public void onNext(List<Messages> messages) {

                //getAll the data and save it in the database

                for(int i=0;i<messages.size();i++){

                    viewModel.insert(new Notification(messages.get(i).getName(),messages.get(i).getSalary()));
                }



            }
        });
    }
}
