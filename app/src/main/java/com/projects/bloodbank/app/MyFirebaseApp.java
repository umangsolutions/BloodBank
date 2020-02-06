package com.projects.bloodbank.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;

import com.google.firebase.database.FirebaseDatabase;
import com.projects.bloodbank.receiver.NetworkStateChangeReceiver;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by evolutyz on 18/12/17.
 */

public class MyFirebaseApp extends Application {
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";


    @Override
    public void onCreate() {
        super.onCreate();
        registerForNetworkChangeEvents(getApplicationContext());

        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }


    public static void registerForNetworkChangeEvents( Context context) {
        NetworkStateChangeReceiver networkStateChangeReceiver = new NetworkStateChangeReceiver();
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
    }




}
