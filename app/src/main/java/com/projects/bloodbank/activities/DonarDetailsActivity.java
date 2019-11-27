package com.projects.bloodbank.activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class DonarDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    DatabaseReference myRef;
    List<Details> detailsList;
   // DetailList detailsList1;
    ListView listView;
    Spinner spinner;
    String group;

    CustomAdapter adapter ;
    private static int count = 0;
    private static boolean isNotAdded = true;
    private CheckBox checkBox_header;
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
        donarSend=(Button)findViewById(R.id.donarSend) ;
        linearLayout=(LinearLayout) findViewById(R.id.linearLayout) ;
        relativeLayout=(RelativeLayout) findViewById(R.id.relativeLayout) ;
        donarSend.setOnClickListener(this);
        donarEdittext =(EditText)findViewById(R.id.donareditText) ;
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

                Query query = myRef.orderByChild("blood").equalTo(group);
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        detailsList.clear();
                        if (dataSnapshot.exists() || i==0) {
                            // dataSnapshot is the "issue" node with all children with id 0
                            for (DataSnapshot issue : dataSnapshot.getChildren()) {
                                // do something with the individual "issues"
                                Details details=issue.getValue(Details.class);
                                detailsList.add(details);
                            }
                            adapter=new CustomAdapter(DonarDetailsActivity.this,detailsList);
                            listView.setAdapter(adapter);
                            if (detailsList.size()>0){
                                relativeLayout.setVisibility(View.VISIBLE);

                            }
                        }
                        else{
                            relativeLayout.setVisibility(View.GONE);
                            Toast.makeText(DonarDetailsActivity.this, "No Blood Group Found", Toast.LENGTH_SHORT).show();
                            listView.setAdapter(null);
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

            donarSend.setEnabled(true);

            String toNumbers = "";

            for (String s : phonnoList) {
                Log.e("PHONELIST", "" + s);
                toNumbers = toNumbers + s + ";";


            }
            phonnoList.clear();
            toNumbers = toNumbers.substring(0, toNumbers.length() - 1);
            String message = "this is a custom message";

            Uri sendSmsTo = Uri.parse("smsto:" + toNumbers);
            Intent intent = new Intent(
                    Intent.ACTION_SENDTO, sendSmsTo);
            intent.putExtra("sms_body", donarMessage);
            startActivity(intent);
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

            textView.setText("Name:"+detailsList.get(position).getName());
            textView2.setText("Pincode:"+detailsList.get(position).getPincode());

           final CheckBox mCheckBox=mView.findViewById(R.id.checkBox);



            mCheckBox.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                linearLayout.setVisibility(View.VISIBLE);

                                /* * Saving Checked Position*/

                                mChecked.put(position, isChecked);

                                phonnoList.add(detailsList.get(position).getNumber());

                               /*  * Find if all the check boxes are true*/

                                if (isAllValuesChecked()) {
                                    linearLayout.setVisibility(View.VISIBLE);

                                    
                                    Log.e("CONDITION",""+isAllValuesChecked());
                                    checkBox_header.setChecked(isChecked);

                                    phonnoList.add(detailsList.get(position).getNumber());

                                }



                            } else {

                                linearLayout.setVisibility(View.GONE);

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

            for (int i = 0; i < count; i++) {
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
