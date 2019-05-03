package ke.co.swahilibox.swahilibox.views;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import ke.co.swahilibox.swahilibox.utils.PrefManager;


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
