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

import com.parse.ParseUser;
import com.parse.SignUpCallback;

import butterknife.ButterKnife;
import butterknife.InjectView;
import ke.co.swahilibox.swahilibox.helper.PrefManager;

public class SignUp extends AppCompatActivity {

    private static final String TAG = "SignupActivity";

    @InjectView(R.id.input_name)
    EditText name;
    @InjectView(R.id.input_email)
    EditText email;
    @InjectView(R.id.input_password)
    EditText password;
    @InjectView(R.id.btn_signup)
    Button signUp;
    @InjectView(R.id.link_login)
    TextView logInLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.inject(this);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        logInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LogIn.class);
                startActivity(intent);
                overridePendingTransition(R.transition.slide_in_left, R.transition.slide_out_right);
            }
        });
    }

    public void signup() {

        if (!validate()) {
            onSignupFailed();
            return;
        }

        signUp.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        String name = this.name.getText().toString();
        final String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(name);
        user.setPassword(password);
        user.signUpInBackground(new SignUpCallback() {


            @Override
            public void done(com.parse.ParseException e) {
                if (e == null) {
                    new PrefManager(SignUp.this).createLoginSession(email);
                    onSignupSuccess();
                } else {
                    onSignupFailed();
                }

                progressDialog.dismiss();
            }
        });

    }


    public void onSignupSuccess() {
        signUp.setEnabled(true);
        Intent intent = new Intent(SignUp.this, Splash.class);
        startActivity(intent);
        overridePendingTransition(R.transition.slide_in_right, R.transition.slide_out_left);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();

        signUp.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = this.name.getText().toString();
        String email = this.email.getText().toString();
        String password = this.password.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            this.name.setError("at least 3 characters");
            valid = false;
        } else {
            this.name.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            this.email.setError("enter a valid email address");
            valid = false;
        } else {
            this.email.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            this.password.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            this.password.setError(null);
        }

        return valid;
    }
}

