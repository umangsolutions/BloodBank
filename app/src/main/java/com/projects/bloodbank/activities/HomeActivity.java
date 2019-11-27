package com.projects.bloodbank.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projects.bloodbank.R;
import com.google.firebase.auth.FirebaseAuth;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;
import com.projects.bloodbank.modals.Details;



public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout donarLayout,chatLayout,eventsLayout;

    boolean doubleBackToExitPressedOnce = false;
    //EditText lastDate;
    TextView lastDate;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button save;
    private String id;
    private String name;
    private String email;
    private String number;
    private String password1;
    private String blood;
    private String pincode;
    private String lastDate1;
    MyAppPrefsManager myAppPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        overridePendingTransition(0,0);

        ConstantValues.internetCheck(HomeActivity.this);
        myAppPrefsManager=new MyAppPrefsManager(HomeActivity.this);
        email=myAppPrefsManager.getUserName();
        donarLayout=(LinearLayout)findViewById(R.id.donarLayout);
        chatLayout=(LinearLayout)findViewById(R.id.chatLayout);
        eventsLayout=(LinearLayout)findViewById(R.id.eventsLayout);
        lastDate=(TextView) findViewById(R.id.lastDate);
        save=(Button) findViewById(R.id.save);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("details");
        donarLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
        eventsLayout.setOnClickListener(this);
        save.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.save:


                Query query = myRef.orderByChild("email").equalTo(email);
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                Details details=issue.getValue(Details.class);
                                id=""+details.getId();
                                name=""+details.getName();
                                number=""+details.getNumber();
                                password1=""+details.getPassword1();
                                blood=""+details.getBlood();
                                pincode=""+details.getPincode();
                            }
                            lastDate1=lastDate.getText().toString().trim();
                            Details details = new Details(id,name,email,number,password1,blood,pincode,lastDate1);
                            myRef.child(id).setValue(details);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                break;
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
            MyAppPrefsManager myAppPrefsManager = new MyAppPrefsManager(HomeActivity.this);
            myAppPrefsManager.setUserLoggedIn(false);
            // Set isLogged_in of ConstantValues
            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
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
