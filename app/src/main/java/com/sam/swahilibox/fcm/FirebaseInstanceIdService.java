package com.sam.swahilibox.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;

public class FirebaseInstanceIdService extends com.google.firebase.iid.FirebaseInstanceIdService {

    private static final String TAG = "FirebaseInstanceIdServi";

    @Override
    public void onTokenRefresh() {


        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        //creates a log that will enable us to get a token
        Log.d(TAG,"Our token is: "+refreshToken);

    }
}
