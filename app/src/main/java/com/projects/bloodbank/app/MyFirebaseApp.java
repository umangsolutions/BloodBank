package com.projects.bloodbank.app;

import android.content.Context;
import android.content.IntentFilter;

import com.google.firebase.database.FirebaseDatabase;
import com.projects.bloodbank.receiver.NetworkStateChangeReceiver;

import static android.net.ConnectivityManager.CONNECTIVITY_ACTION;

/**
 * Created by evolutyz on 18/12/17.
 */

public class MyFirebaseApp extends android.app.Application {
    private static final String WIFI_STATE_CHANGE_ACTION = "android.net.wifi.WIFI_STATE_CHANGED";

    @Override
    public void onCreate() {
        super.onCreate();
        registerForNetworkChangeEvents(this);

        /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


    public static void registerForNetworkChangeEvents(final Context context) {
        NetworkStateChangeReceiver networkStateChangeReceiver = new NetworkStateChangeReceiver();
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(CONNECTIVITY_ACTION));
        context.registerReceiver(networkStateChangeReceiver, new IntentFilter(WIFI_STATE_CHANGE_ACTION));
    }

}
