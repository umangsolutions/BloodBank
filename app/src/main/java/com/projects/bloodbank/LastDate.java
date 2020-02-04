package com.projects.bloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.projects.bloodbank.activities.HomeActivity;
import com.projects.bloodbank.modals.Details;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

import java.util.Calendar;

public class LastDate extends AppCompatActivity {

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
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_date);

        overridePendingTransition(0,0);

        ConstantValues.internetCheck(LastDate.this);
        myAppPrefsManager=new MyAppPrefsManager(LastDate.this);
        email=myAppPrefsManager.getUserName();
        //lastDate=(EditText) findViewById(R.id.lastDate);
        lastDate=(TextView) findViewById(R.id.lastDate);
        save=(Button) findViewById(R.id.save);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("details");
        save.setEnabled(false);
        final ImageView imageViewuE=(ImageView)findViewById(R.id.imageViewup);
        imageViewuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LastDate.this, new DatePickerDialog.OnDateSetListener() {
                //DatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lastDate.setText(""+(month +1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                save.setEnabled(true);
            }
        });

        if(lastDate.equals("Last Blood Donation Date")) {
            Toast.makeText(this, "Please choose Date !", Toast.LENGTH_SHORT).show();
        }
        else
            {
            findViewById(R.id.save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query query = myRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
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
                            Details details = new Details(id,name,email,number,password1,blood,pincode,lastDate1,lastDate1);
                            myRef.child(id).setValue(details);
                            Toast.makeText(LastDate.this,"Blood Donation Date Updated",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//finish();
            onBackPressed();
        }
        return true;
    }

}
