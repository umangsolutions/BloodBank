package com.projects.bloodbank.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.telephony.SmsManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.bloodbank.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.projects.bloodbank.modals.Details;
import com.projects.bloodbank.utilities.ConstantValues;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class DonarDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference myRef;
    List<Details> detailsList;
   // DetailList detailsList1;
    ListView listView;
    Spinner spinner;
    String group;
    String date,date1;
    CustomAdapter adapter ;
    private static int count = 0;
    private static boolean isNotAdded = true;
    private CheckBox checkBox_header;
    private long diff;
    private ArrayList<String> phonnoList=new ArrayList<>();
    /**
     * To save checked items, and <b>re-add</b> while scrolling.
     */
    SparseBooleanArray mChecked = new SparseBooleanArray();

    EditText donarEdittext;
    Button donarSend;
    LinearLayout linearLayout;
    RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donar_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Donor");
        ConstantValues.internetCheck(DonarDetailsActivity.this);

        Date cd = Calendar.getInstance().getTime();
       System.out.println("Current time => " + cd);
     SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        date = df.format(cd);
        //String dateInString = "2011-09-13";  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

        Calendar c = Calendar.getInstance(); // Get Calendar Instance
        try {
            c.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, -180);  // add 45 days
        sdf = new SimpleDateFormat("MM/dd/yyyy");

        Date resultdate = new Date(c.getTimeInMillis());   // Get new time
        date1 = sdf.format(resultdate);
        System.out.println("String date:"+date1);




        Log.d("DATE1",date);
        donarSend=(Button)findViewById(R.id.donarSend) ;
        linearLayout=(LinearLayout) findViewById(R.id.linearLayout) ;
        relativeLayout=(RelativeLayout) findViewById(R.id.relativeLayout) ;
        donarSend.setOnClickListener(this);
        donarEdittext =(EditText)findViewById(R.id.donareditText);
        donarEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().trim().length() > 0) {
                    donarSend.setEnabled(true);
                } else {
                    donarSend.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });


        listView=findViewById(R.id.listdonar);
        detailsList=new ArrayList<>();
        myRef= FirebaseDatabase.getInstance().getReference("details");
        myRef.keepSynced(true);
        checkBox_header = (CheckBox) findViewById(
                R.id.checkBox_header);



        /*
         * To avoid adding multiple times
         */
        checkBox_header.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                linearLayout.setVisibility(View.VISIBLE);
                /*
                 * Set all the checkbox to True/False
                 */
                for (int i = 0; i < count; i++) {
                    mChecked.put(i, checkBox_header.isChecked());
                }


                /*
                 * Update View
                 */
                adapter.notifyDataSetChanged();

            }
        });



        spinner=findViewById(R.id.spinnerBloodGroup);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, final int i, long l) {
               group=spinner.getSelectedItem().toString().trim();
                String text = group+" "+ date;
                Query query = myRef.orderByChild("blood").equalTo(group);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        detailsList.clear();
                        if (dataSnapshot.exists() || i==0) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                Details details = issue.getValue(Details.class);
                                String lastdate = issue.getValue(Details.class).getLastDate();
                                String setdate = issue.getValue(Details.class).getSetDate().toString();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

                                Date cd = Calendar.getInstance().getTime();
                                System.out.println("Current time => " + cd);
                                SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
                                String currdate = df.format(cd);

                                try {
                                    Date date = dateFormat.parse(lastdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                //int days = Days.daysBetween(new LocalDate(lastdate),new LocalDate(setdate)).getDays();
                                SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

                                try {
                                    Date date1 = myFormat.parse(lastdate);
                                    Date date2 = myFormat.parse(currdate);
                                    diff = date2.getTime() - date1.getTime();
                                    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                if(diff > 93 || lastdate.isEmpty())
                                detailsList.add(details);
                                else {
                                    //Toast.makeText(DonarDetailsActivity.this, "" + diff, Toast.LENGTH_SHORT).show();
                                    //Toast.makeText(DonarDetailsActivity.this, "No Blood Group Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                            adapter=new CustomAdapter(DonarDetailsActivity.this,detailsList);
                            listView.setAdapter(adapter);
                            if (detailsList.size()>0){
                                relativeLayout.setVisibility(View.VISIBLE);
                                //donarSend.setEnabled(true);


                            }
                        }
                        else{
                            relativeLayout.setVisibility(View.GONE);
                            Toast.makeText(DonarDetailsActivity.this, "No Blood Group Found", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(null);
                            //donarEdittext.setEnabled(false);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.donarSend) {
            String donarMessage = donarEdittext.getText().toString();

            if (!donarMessage.isEmpty()) {
                donarSend.setEnabled(true);

                String toNumbers = "";

                for (String s : phonnoList) {
                    Log.e("PHONELIST", "" + s);
                    toNumbers = toNumbers + s + ";";


                }
                phonnoList.clear();
                if (!toNumbers.isEmpty()) {
                    toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
                    String message = "this is a custom message";
                    Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
                    Intent intent = new Intent(
                            Intent.ACTION_SENDTO, sendSmsTo);
                    intent.putExtra("sms_body", donarMessage);
                    startActivity(intent);
                }
            }
        }




    }

    /*
    * CustomAdapter
    */
    public class CustomAdapter extends BaseAdapter {

        Activity sActivity;
        List<Details> detailsList;

        private CustomAdapter(Activity sActivity, List<Details> detailsList) {
            this.sActivity = sActivity;
            this.detailsList = detailsList;
        }

        @Override
        public int getCount() {


             /* Length of our listView*/

            count = detailsList.size();
            return count;
        }

        @Override
        public Object getItem(int position) {


          /*Current Item*/

            return position;
        }

        @Override
        public long getItemId(int position) {


          /*Current Item's ID*/

            return position;
        }

        @SuppressLint({"InflateParams", "SetTextI18n"})
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View mView = convertView;

            if (mView == null) {

                /*LayoutInflater*/

                final LayoutInflater sInflater = (LayoutInflater) sActivity.getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);

             /*Inflate Custom List View*/

                assert sInflater != null;
                mView = sInflater.inflate(R.layout.custom_list_view, null, false);

            }


            TextView textView=(TextView)mView.findViewById(R.id.textView);
            TextView textView2=(TextView)mView.findViewById(R.id.pincode);

            textView.setText("Name: "+detailsList.get(position).getName());
            textView2.setText("Age: "+detailsList.get(position).getPincode());

           final CheckBox mCheckBox=mView.findViewById(R.id.checkBox);



            mCheckBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                linearLayout.setVisibility(View.VISIBLE);
                                donarSend.setEnabled(true);
                                //donarEdittext.setFocusableInTouchMode(true);
                                /* * Saving Checked Position*/
                                mChecked.put(position, isChecked);
//                                checkBox_header.setChecked(isChecked);
                                phonnoList.add(detailsList.get(position).getNumber());

                                /*  * Find if all the check boxes are true*/
                                if (isAllValuesChecked()) {
                                    linearLayout.setVisibility(View.VISIBLE);
                                    //mChecked.put(position, isChecked);
                                    //donarEdittext.setFocusableInTouchMode(true);
                                    Log.e("CONDITION", "" + isAllValuesChecked());
                                    checkBox_header.setChecked(isChecked);
                                    phonnoList.add(detailsList.get(position).getNumber());
                                }

                            }
                             else {
                                linearLayout.setVisibility(View.VISIBLE);
                                /* * Removed UnChecked Position*/
                                mChecked.delete(position);
                                phonnoList.remove(detailsList.get(position).getNumber());
                               /*  * Remove Checked in Header*/
                                checkBox_header.setChecked(false);

                            }

                        }
                    });


             /* * Set CheckBox "TRUE" or "FALSE" if mChecked == true*/

            mCheckBox.setChecked((mChecked.get(position)));

             /* **************ADDING CONTENTS**************** */


           /*  * Return View here*/

            return mView;
        }


        /* * Find if all values are checked.*/

        boolean isAllValuesChecked() {

            for (int i = 0;i < count; i++) {
                if (!mChecked.get(i)) {
                    return false;
                }
            }

            return true;
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
