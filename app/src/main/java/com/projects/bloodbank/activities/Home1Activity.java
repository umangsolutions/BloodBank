package com.projects.bloodbank.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.projects.bloodbank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

public class Home1Activity extends AppCompatActivity implements View.OnClickListener {
LinearLayout donarLayout,chatLayout,eventsLayout;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        overridePendingTransition(0,0);

        donarLayout=(LinearLayout)findViewById(R.id.donarLayout);
        chatLayout=(LinearLayout)findViewById(R.id.chatLayout);
        eventsLayout=(LinearLayout)findViewById(R.id.eventsLayout);



        donarLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        eventsLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.donarLayout:
                startActivity(new Intent(this,DonarDetailsActivity.class));
                break;

            case R.id.chatLayout:
                startActivity(new Intent(this,ChatActivity.class));
                break;

            case R.id.eventsLayout:
                startActivity(new Intent(this,EventsActivity.class));
                break;

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            // Set UserLoggedIn in MyAppPrefsManager
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(Home1Activity.this);
            myAppPrefsManager.setUserLoggedIn(false);
            // Set isLogged_in of ConstantValues
            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Home1Activity.this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce=false;


            }
        }, 2000);
    }
}
