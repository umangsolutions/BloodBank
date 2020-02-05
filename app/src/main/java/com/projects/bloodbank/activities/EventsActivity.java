package com.projects.bloodbank.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.projects.bloodbank.modals.EventItem;
import com.projects.bloodbank.adapters.EventsAdapter;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class EventsActivity extends AppCompatActivity {
private List<EventItem> eventItemList=new ArrayList<>();
ListView listView;
     EditText editTextUE,editTextLo;
     TextView textViewUE;
     DatabaseReference myRef;
     String test;
    EventItem event;
    MyAppPrefsManager myAppPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        ConstantValues.internetCheck(EventsActivity.this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Events");
        myAppPrefsManager=new MyAppPrefsManager(EventsActivity.this);

        listView=(ListView)findViewById(R.id.list_view);
        myRef = FirebaseDatabase.getInstance().getReference("Events");
        myRef.keepSynced(true);
        FloatingActionButton floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    updateEvent();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, final long id) {
               // Toast.makeText(EventsActivity.this, ""+position, Toast.LENGTH_SHORT).show();

                EventItem model = eventItemList.get(position);
                String email = ""+ model.getEmail();
                Log.d("MAILTEST",""+email );
                if (email.equalsIgnoreCase(myAppPrefsManager.getUserName())){
                    eventItemList.remove(position);
                    myRef.child(model.getId()).removeValue();
                }else{
                    Toast.makeText(EventsActivity.this, "You are not allowed to delete this event...!", Toast.LENGTH_SHORT).show();
                }


                return false;
            }
        });

    }
    private  void updateEvent(){
        AlertDialog.Builder builder2=new AlertDialog.Builder(this);
        LayoutInflater layoutInflater=getLayoutInflater();
        @SuppressLint("InflateParams")
        View view2=layoutInflater.inflate(R.layout.update_event,null);
        builder2.setView(view2);
        final AlertDialog alertDialog2=builder2.create();
        alertDialog2.show();
        editTextUE=view2.findViewById(R.id.eventupdate);
        editTextLo=view2.findViewById(R.id.editLocation);

        final Button buttonUE=view2.findViewById(R.id.btnEventupdate);
        final Button buttonDE=view2.findViewById(R.id.btnEventdelete);
        final ImageView imageViewuE=view2.findViewById(R.id.imageViewupdate);

        textViewUE=view2.findViewById(R.id.textView3update);






        imageViewuE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                        test="Date :"+" "+""+(month +1) + "/" + dayOfMonth + "/" + year;
                        Log.d("DATETEST",""+test);

                        if (test.equalsIgnoreCase("") && test.equalsIgnoreCase(null)){
                            editTextLo.setEnabled(false);
                            editTextUE.setEnabled(false);
                        }else {
                            textViewUE.setText(""+test);
                            editTextLo.setEnabled(true);
                            editTextUE.setEnabled(true);
                        }

                    }
                }, mYear, mMonth, mDay);

                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });
        buttonDE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog2.dismiss();
            }
        });
        buttonUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name=editTextUE.getText().toString().trim();
                String date1=textViewUE.getText().toString().trim();
                String location=editTextLo.getText().toString().trim();

                String email=myAppPrefsManager.getUserName();
                if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(date1) && !TextUtils.isEmpty(location)){
                    String id=myRef.push().getKey();
                   event =new EventItem(id,date1,location,name,email);

                    myRef.child(id).setValue(event);
                    Toast.makeText(EventsActivity.this, "Added", Toast.LENGTH_SHORT).show();
                    alertDialog2.dismiss();

                }else {
                    Toast.makeText(EventsActivity.this, "Enter Details", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {//finish();
            onBackPressed();
        }
        return true;
    }
    @Override
    protected void onStart() {
        super.onStart();
        myRef.orderByChild("date1").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventItemList.clear();
                for (DataSnapshot issuesnapshot:dataSnapshot.getChildren()){
                    EventItem event=issuesnapshot.getValue(EventItem.class);
                    eventItemList.add(event);
                }
                EventsAdapter adapter=new EventsAdapter(EventsActivity.this,eventItemList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


        });
    }
}
