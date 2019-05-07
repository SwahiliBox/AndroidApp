package com.sam.swahilibox;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;
import com.sam.swahilibox.adapters.NotificationAdapter;
import com.sam.swahilibox.model.Notification;
import com.sam.swahilibox.viewmodels.NotificationViewModel;

import java.util.List;

public class Alerts extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private NotificationViewModel notificationViewModel;
    private RecyclerView rv_notification_message;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        rv_notification_message = findViewById(R.id.rv_notification_messages);
        rv_notification_message.setLayoutManager(new LinearLayoutManager(this));
        rv_notification_message.setHasFixedSize(true);

        rv_notification_message = findViewById(R.id.rv_notification_messages);
        rv_notification_message.setLayoutManager(new LinearLayoutManager(this));
        rv_notification_message.setHasFixedSize(true);

        final NotificationAdapter notificationAdapter = new NotificationAdapter();
        rv_notification_message.setAdapter(notificationAdapter);

        notificationViewModel = ViewModelProviders.of(this).get(NotificationViewModel.class);
        notificationViewModel.getAllNotes().observe(this,(new Observer<List<Notification>>() {
            @Override
            public void onChanged(List<Notification> notifications) {
                //update recyclerview
                notificationAdapter.submitList(notifications);
            }
        }));
        subscribeToFcmDealsTopic();

        //swipe to delete a notification message
        new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                notificationViewModel.delete(notificationAdapter.getNotificationMessageAt(viewHolder.getAdapterPosition()));

            }
        }).attachToRecyclerView(rv_notification_message);

    }



    //subscribes to firebase cloud messaging topic one time
    private void subscribeToFcmDealsTopic(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if (!prefs.getBoolean("firstRun", false)) {

            FirebaseMessaging.getInstance().subscribeToTopic("notifications");

            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("firstRun", true);
            editor.commit();
        }
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
        getMenuInflater().inflate(R.menu.alerts, menu);
        getMenuInflater().inflate(R.menu.menu_item,menu);
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
        if (id == R.id.delete_all_messages){
            notificationViewModel.deleteAll();
            Toast.makeText(this, "All Notes Deleted", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.log_out) {
            intent = new Intent(this, LogIn.class);
            startActivity(intent);

            finish();
        } else if (id == R.id.about_us) {
            intent = new Intent(this, AboutUs.class);
            startActivity(intent);
            Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();

        } else if (id == R.id.invite) {
            intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, "Hello, you are invited to join SwahiliBox. " +
                    "Check our website http://www.swahilibox.co.ke and download the " +
                    "SwahiliBox App to stay upto date");
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
