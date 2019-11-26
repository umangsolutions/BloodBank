package com.projects.bloodbank;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.projects.bloodbank.chatmessage.ChatActivity;
import com.projects.bloodbank.donardetails.DonarDetailsActivity;
import com.projects.bloodbank.eventactivities.EventsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class Home1Activity extends AppCompatActivity implements View.OnClickListener {
LinearLayout donarLayout,chatLayout,eventsLayout,linksLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        overridePendingTransition(0,0);

        donarLayout=(LinearLayout)findViewById(R.id.donarLayout);
        chatLayout=(LinearLayout)findViewById(R.id.chatLayout);
        eventsLayout=(LinearLayout)findViewById(R.id.eventsLayout);
        linksLayout=findViewById(R.id.donarLinks);


        donarLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        eventsLayout.setOnClickListener(this);
        linksLayout.setOnClickListener(this);
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
            case R.id.donarLinks:
                startActivity(new Intent(this,LinksActivity.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Home1Activity.this,LoginActivity.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(Home1Activity.this);
        } else {
            builder = new AlertDialog.Builder(Home1Activity.this);
        }
        builder.setTitle("Confirm Exit ")
                .setMessage("Do you want to exit app?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        moveTaskToBack(true);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(1);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
