package com.projects.bloodbank.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Events");

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
        editTextLo.setEnabled(false);
        editTextUE.setEnabled(false);
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
                editTextLo.setEnabled(true);
                editTextUE.setEnabled(true);
                DatePickerDialog datePickerDialog = new DatePickerDialog(EventsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewUE.setText("Date :"+" "+""+(month +1) + "/" + dayOfMonth + "/" + year);
                    }
                }, mYear, mMonth, mDay);
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

            nextEvent();
            alertDialog2.dismiss();



            }
        });

    }
    private void nextEvent(){
        String name=editTextUE.getText().toString().trim();
        String date1=textViewUE.getText().toString();
        String location=editTextLo.getText().toString();
        if(!TextUtils.isEmpty(name)){
            String id=myRef.push().getKey();
            EventItem event =new EventItem(id,date1,location,name);
            myRef.child(id).setValue(event);
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();

        }else {
            Toast.makeText(this, "Enter event", Toast.LENGTH_SHORT).show();
        }

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
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventItemList.clear();
                for (DataSnapshot issuesnapshot:dataSnapshot.getChildren()){
                    EventItem event=issuesnapshot.getValue(EventItem.class);
                    eventItemList.add(event);
                }
                EventsAdapter adapter=new EventsAdapter(EventsActivity.this,eventItemList);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });
    }
}
