package ke.co.swahilibox.swahilibox.views;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;


import ke.co.swahilibox.swahilibox.R;

public class LogIn extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_SIGNUP = 0;

    SignInButton signInButton;
    private static final int RC_SIGN_IN = 1;
    private static final String TAG = "LogIn";
    private CallbackManager callbackManager;

    private GoogleSignInClient googleSignInClient;
    private int requestCode;
    private int resultCode;
    private Intent data;

    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        signInButton = findViewById(R.id.sign_in_button);
        //st size to standard
        signInButton.setSize(signInButton.SIZE_STANDARD);
        /*// Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.*/

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        signInButton.setOnClickListener(this);

        //facebook
        // Initialize your instance of callbackManager//
        callbackManager = CallbackManager.Factory.create();
        // Register your callback//
        LoginManager.getInstance().registerCallback(callbackManager,

                // If the login attempt is successful, then call onSuccess and pass the LoginResult//
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {

                        startActivity(new Intent(LogIn.this,Main.class));
                        // Print the user’s ID and the Auth Token to Android Studio’s Logcat Monitor//

                        Log.d(TAG, "User ID: " +
                                loginResult.getAccessToken().getUserId() + "\n" +
                                "Auth Token: " + loginResult.getAccessToken().getToken());
                    }

                    // If the user cancels the login, then call onCancel//
                    @Override
                    public void onCancel() {
                        //return to the same activity
                        finish();
                    }

                    // If an error occurs, then call onError//
                    @Override
                    public void onError(FacebookException exception) {
                        Log.e(TAG, "The error occurred at "+exception);
                    }
                });




    }

    public LogIn(Context context){}

    private void signIn(){
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
//            The task return from this intent is always completed
            Task<GoogleSignInAccount> task =
                    GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInEvent(task);
        }
    }

    public void handleSignInEvent(Task<GoogleSignInAccount> task) {

        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);

            //show progress dialog
            final ProgressDialog progressDialog = new ProgressDialog(LogIn.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Logging you in...");
            progressDialog.show();
            String status = "Login Success";

            LoginStatus(status);
            // Signed in successfully, show authenticated UI.
            startActivity(new Intent(LogIn.this,Main.class));
            updateUI(account);

            progressDialog.dismiss();

        } catch (ApiException e) {
            String status = "Failed";
            LoginStatus(status);
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    public String LoginStatus(String status) {
        if (status == "Login Success"){
            return "Logged in Successfully";
        }else{
            return "Failed to Log in at this time";
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if(account == null){
            signInButton.setEnabled(true);
        }else{
            signInButton.setEnabled(true);
            //startActivity(new Intent(MainActivity.this,StuffActivity.class));

        }

}
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sign_in_button:
                signIn();
                break;
        }
    }
}
