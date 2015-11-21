package ke.co.swahilibox.swahilibox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

import ke.co.swahilibox.swahilibox.adapter.MessageAdapter;
import ke.co.swahilibox.swahilibox.database.SwahiliBoxDatasource;
import ke.co.swahilibox.swahilibox.helper.ParseUtil;
import ke.co.swahilibox.swahilibox.helper.PrefManager;
import ke.co.swahilibox.swahilibox.model.Message;


/**
 * Created by japheth on 11/15/15.
 */
public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = Main.class.getSimpleName();

    private static final String SELECTED_ITEM_ID = "selected_item_id";
    private static final String FIRST_TIME = "first_time";
    private Toolbar mToolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private int mSelectedId;
    private boolean mUserSawDrawer = false;
    private ListView listView;
    private MessageAdapter adapter;
    private List<Message> messages;
    private PrefManager pref;
    private TextView user_name;
    private SwahiliBoxDatasource dataSource;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = new PrefManager(this);
        dataSource = new SwahiliBoxDatasource(this);
        dataSource.open();

        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        user_name = (TextView) findViewById(R.id.username);
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        listView = (ListView) findViewById(R.id.list_view);
        messages = new ArrayList<>();

        updateDisplay();

        Intent intent = getIntent();

        ParseUser user = ParseUser.getCurrentUser();
        String email = user.getEmail();

        user_name.setText(user.getUsername().toString());

        if (email != null) {
            Log.i(TAG, "Parse user email" + user.getEmail());
            ParseUtil.subscribeWithEmail(user.getEmail());
        } else {
            Log.i(TAG, "Email is null. Not subscribing to parse!");
        }

        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        if (!didUserSeeDrawer()) {
            showDrawer();
            markDrawerSeen();
        } else {
            hideDrawer();
        }
        mSelectedId = savedInstanceState == null ? R.id.invite : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(mSelectedId);
    }

    private boolean didUserSeeDrawer() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = sharedPreferences.getBoolean(FIRST_TIME, false);
        return mUserSawDrawer;
    }

    private void markDrawerSeen() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mUserSawDrawer = true;
        sharedPreferences.edit().putBoolean(FIRST_TIME, mUserSawDrawer).apply();
    }

    private void showDrawer() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

    private void hideDrawer() {
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }

    //navigate the navigation view menu
    private void navigate(int mSelectedId) {
        Intent intent = null;
        switch(mSelectedId){
            case R.id.log_out:
              pref.logOut();
                finish();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {

        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();

        navigate(mSelectedId);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SELECTED_ITEM_ID, mSelectedId);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //add the notification messages to a listview
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        Log.i("NewIntent", "Message Notification");
//        String message = intent.getStringExtra("message");
//
////        Message m = new Message(message, System.currentTimeMillis());
//        messages.add(0, m);
//        adapter.notifyDataSetChanged();
//    }

    public void updateDisplay() {
        //get list of messages from the database and set
        //the adapter
        messages = dataSource.findAll();
        adapter = new MessageAdapter(this, messages);
        listView.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
    }
}
