package com.projects.bloodbank.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
import com.projects.bloodbank.modals.Details;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ListIterator;


public class HomeActivity extends AppCompatActivity implements View.OnClickListener {
    LinearLayout donarLayout,chatLayout,eventsLayout,updatelayout,healthtips,donordifferal;
    Dialog dialog;

    boolean doubleBackToExitPressedOnce = false;
    //EditText lastDate;
    TextView lastDate,nameView,bloodgr;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Button save;
    private String id;
    private String name;
    private String email;
    private String bloodgroup;
    private String number;
    private String password1;
    private String blood;
    private String pincode;
    private String lastDate1;
    private Button closebtn;
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
        updatelayout = (LinearLayout)findViewById(R.id.updatedate);
        healthtips=(LinearLayout)findViewById(R.id.healthtipsLayout);
        donordifferal=(LinearLayout)findViewById(R.id.donordefferalLayout);


        nameView=(TextView) findViewById(R.id.nameView);
        bloodgr=(TextView) findViewById(R.id.bloodgr);
//        //lastDate=(EditText) findViewById(R.id.lastDate);
//        lastDate=(TextView) findViewById(R.id.lastDate);
//        save=(Button) findViewById(R.id.save);
//        database = FirebaseDatabase.getInstance();
//        myRef = database.getReference("details");
        CustomDialog();
        donarLayout.setOnClickListener(this);
        chatLayout.setOnClickListener(this);
       eventsLayout.setOnClickListener(this);
       updatelayout.setOnClickListener(this);
       healthtips.setOnClickListener(this);
       donordifferal.setOnClickListener(this);
//        final ImageView imageViewuE=(ImageView)findViewById(R.id.imageViewup);
//        imageViewuE.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar c = Calendar.getInstance();
//                int mYear = c.get(Calendar.YEAR);
//                int mMonth = c.get(Calendar.MONTH);
//                int mDay = c.get(Calendar.DAY_OF_MONTH);
//                DatePickerDialog datePickerDialog = new DatePickerDialog(HomeActivity.this, new DatePickerDialog.OnDateSetListener() {
//                    @SuppressLint("SetTextI18n")
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                        lastDate.setText(""+(month +1) + "/" + dayOfMonth + "/" + year);
//                    }
//                }, mYear, mMonth, mDay);
//                datePickerDialog.show();
//            }
//        });
//        //save.setOnClickListener(this);

        myRef= FirebaseDatabase.getInstance().getReference("details");
        myRef.keepSynced(true);
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot issue : dataSnapshot.getChildren()) {
                        // do something with the individual "issues"
                        Details details = issue.getValue(Details.class);
                        name=issue.getValue(Details.class).getName();
                        bloodgroup=issue.getValue(Details.class).getBlood();
                        }
                    bloodgr.setText(bloodgroup+"ve");
                    nameView.setText(name);


                    }





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void CustomDialog() {
        dialog=new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.customlayout);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        closebtn=(Button)dialog.findViewById(R.id.close);

        dialog.findViewById(R.id.image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LastDateActivity.class));
            }
        });

        closebtn.setEnabled(true);

        closebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

        dialog.show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.donarLayout:
                Intent intent=new Intent(HomeActivity.this,DonarDetailsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;

            case R.id.chatLayout:
                Intent intent1=new Intent(HomeActivity.this,ChatActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);

                break;

            case R.id.eventsLayout:
                Intent intent2=new Intent(HomeActivity.this,EventsActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent2);

                break;
            case R.id.updatedate:
                Intent intent3=new Intent(HomeActivity.this,LastDateActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent3);
                break;
            case R.id.healthtipsLayout:
                Intent intent4=new Intent(HomeActivity.this,Health_Tips.class);
                intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent4);
                break;
            case R.id.donordefferalLayout:
                Intent intent5=new Intent(HomeActivity.this,Deferral_Activity.class);
                intent5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent5);
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

            // Set isLogged_in of ConstantValues
            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
            FirebaseAuth.getInstance().signOut();
            Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            myAppPrefsManager.setUserLoggedIn(false);
            myAppPrefsManager.setUserName("");
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
