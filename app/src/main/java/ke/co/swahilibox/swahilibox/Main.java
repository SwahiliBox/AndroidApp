package ke.co.swahilibox.swahilibox;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    private ActionMode.Callback mActionModeCallback;


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
        this.listView.setEmptyView(findViewById(R.id.empty));   //if list view is empty dispay the empty textview
        messages = new ArrayList<>();

        updateDisplay();

        Intent intent = getIntent();

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
        mSelectedId = savedInstanceState == null ? 0 : savedInstanceState.getInt(SELECTED_ITEM_ID);
        navigate(mSelectedId);

        ParseUser user = ParseUser.getCurrentUser();
        String email = user.getEmail();
        user_name.setText(user.getUsername());

        if (email != null) {
            Log.i(TAG, "Parse user email" + user.getEmail());
            ParseUtil.subscribeWithEmail(user.getEmail());
        } else {
            Log.i(TAG, "Email is null. Not subscribing to parse!");
        }


        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            //use floating context menu on Froyo and Ginger bread
            registerForContextMenu(listView);
        } else {
            //use contextual action bar on Honeycomb and higher
            listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                    //inflate the delete_message menu resource file
                    MenuInflater inflater = actionMode.getMenuInflater();
                    inflater.inflate(R.menu.message_list_item_context, menu);
                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                    //action taken when a list item is long pressed
                    switch (menuItem.getItemId()) {
                        case R.id.delete_message:
                            MessageAdapter adapter = (MessageAdapter) listView.getAdapter();

                            //loop through all items
                            for (int i = adapter.getCount() - 1; i >= 0; i--) {
                                //check if item is checked
                                if (listView.isItemChecked(i)) {
                                    //delete selected item
                                    dataSource.deleteMessage(adapter.getItem(i));
                                }

                            }
                            actionMode.finish();
                            return true;

                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode actionMode) {
                    //update the display
                    messages = dataSource.findAll();
                    updateDisplay();
                }
            });
        }


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

        if (mSelectedId != 0) {
            switch (mSelectedId) {
                case R.id.log_out:
                    pref.logOut();
                    intent = new Intent(this, LogIn.class);
                    startActivity(intent);
                    overridePendingTransition(R.transition.push_down_in, R.transition.push_down_out);
                    finish();
                    break;
                case R.id.about_us:
                    intent = new Intent(this, AboutUs.class);
                    startActivity(intent);
                    Toast.makeText(this, "Loading...", Toast.LENGTH_LONG).show();
                    break;
                case R.id.invite:
                    intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "Hello, you are invited to join SwahiliBox. " +
                            "Check our website http://www.swahilibox.co.ke and download the " +
                            "SwahiliBox App to stay upto date");
                    startActivity(intent);
                    break;
            }
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.message_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        Message message = (Message) listView.getAdapter().getItem(position);
        switch (item.getItemId()) {
            case R.id.delete_message:
                dataSource.deleteMessage(message);
                updateDisplay();
                return true;
        }
        return super.onContextItemSelected(item);
    }

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
