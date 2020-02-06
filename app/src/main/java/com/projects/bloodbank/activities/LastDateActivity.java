package com.projects.bloodbank.activities;

import androidx.annotation.NonNull;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projects.bloodbank.R;
import com.projects.bloodbank.modals.Details;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

import java.util.Calendar;
import java.util.Objects;

public class LastDateActivity extends AppCompatActivity {

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
    private String age;
    private String ldate;
    private String lastDate1;
    MyAppPrefsManager myAppPrefsManager;
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_date);

        overridePendingTransition(0,0);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Last Donar Date");
        ConstantValues.internetCheck(LastDateActivity.this);
        myAppPrefsManager=new MyAppPrefsManager(LastDateActivity.this);
        email=myAppPrefsManager.getUserName();
        lastDate=(TextView) findViewById(R.id.lastDate);
        save=(Button) findViewById(R.id.save);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("details");

        final ImageView imageViewuE=(ImageView)findViewById(R.id.imageViewup);
        imageViewuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(LastDateActivity.this, new DatePickerDialog.OnDateSetListener() {
                    //DatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        lastDate.setText("" + (month + 1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);


                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show();

            }
        });


            save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Query query = myRef.orderByChild("email").equalTo(email);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.exists()) {
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                Details details=issue.getValue(Details.class);

                                id=""+details.getId();
                                name=""+details.getName();
                                number=""+details.getNumber();
                                password1=""+details.getPassword();
                                blood=""+details.getBlood();
                                age=""+details.getAge();
                            }
                            lastDate1=lastDate.getText().toString().trim();
                            if(!lastDate1.isEmpty()) {
                                Details details = new Details(id, name, email, number, password1, blood, age, lastDate1, lastDate1);
                                myRef.child(id).setValue(details);
                                Toast.makeText(LastDateActivity.this, "Blood Donation Date Updated", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(LastDateActivity.this,HomeActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            } else {
                                Toast.makeText(LastDateActivity.this, "Please Select Date", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

}
