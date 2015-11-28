package ke.co.swahilibox.swahilibox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseUser;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ke.co.swahilibox.swahilibox.helper.PrefManager;

public class LogIn extends AppCompatActivity {

    private static final String TAG = LogIn.class.getSimpleName();
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_username)
    EditText username;
    @InjectView(R.id.input_password)
    EditText password;
    @InjectView(R.id.btn_login)
    Button logIn;
    @InjectView(R.id.link_signup)
    TextView signUp;

    PrefManager pref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.inject(this);
        pref = new PrefManager(this);
        logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
                finish();
            }
        });
    }

    public void logIn() {
        if (!validate()) {
            onLogInFailed();
            return;
        }

        logIn.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Logging you in...");
        progressDialog.show();

        final String userName = username.getText().toString();
        String _pass = password.getText().toString();

        ParseUser.logInInBackground(userName, _pass, new LogInCallback() {
            @Override
            public void done(ParseUser user, com.parse.ParseException e) {
                //If user exists and authenticated send them to main
                if (user != null) {
                    pref.createLoginSession(userName);
                    Intent intent = new Intent(LogIn.this, Main.class);
                    startActivity(intent);
                    overridePendingTransition(R.transition.push_up_in, R.transition.push_up_out);
                    finish();
                } else {
                    onLogInFailed();
                }

                progressDialog.dismiss();

            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
    }

    public void onLoginSuccess() {
        logIn.setEnabled(true);
        finish();
    }

    public void onLogInFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        logIn.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String userName = this.username.getText().toString();
        String _password = this.password.getText().toString();


        if (_password.isEmpty() || password.length() < 4 || password.length() > 10) {
            password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {

            password.setError(null);
        }

        return valid;
    }
}
