package com.projects.bloodbank.app;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by evolutyz on 18/12/17.
 */

public class MyFirebaseApp extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
    /* Enable disk persistence  */
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
