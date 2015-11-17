package ke.co.swahilibox.swahilibox;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ke.co.swahilibox.swahilibox.helper.PrefManager;


public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PrefManager manager = new PrefManager(this);

        if (manager.isLoggedIn()) {
            Intent intent = new Intent(Splash.this, Main.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(Splash.this, LogIn.class);
            startActivity(intent);
            finish();
        }

    }
}
