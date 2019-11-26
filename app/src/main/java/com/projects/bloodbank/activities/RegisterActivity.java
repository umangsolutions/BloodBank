package com.projects.bloodbank.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.projects.bloodbank.R;
import com.projects.bloodbank.modals.Details;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.projects.bloodbank.utilities.ConstantValues;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText etFullName,etMobile,etEmail,etPassword,etAddress;
    String name,email,number,password,bloodgroup,pincode;
    Button btnSave;
    Spinner spinner;
    private FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef;
    TextView textView;
    FirebaseAuth firebaseAuth ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Register");
        progressDialog=new ProgressDialog(this);
        ConstantValues.internetCheck(RegisterActivity.this);
        mAuth = FirebaseAuth.getInstance();
         database = FirebaseDatabase.getInstance();
         myRef = database.getReference("details");
        firebaseAuth = FirebaseAuth.getInstance();
        spinner=findViewById(R.id.spinnerBlood);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        etFullName = (EditText) findViewById(R.id.etFullName);
        etMobile = (EditText) findViewById(R.id.etMobile);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etAddress = (EditText) findViewById(R.id.etAddress);
        textView=findViewById(R.id.txtLogin);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent register = new Intent(RegisterActivity.this, LoginActivity.class);
                register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(register);
            }
        });


        btnSave = (Button) findViewById(R.id.buttonSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=etFullName.getText().toString().trim();
                email =etEmail.getText().toString().trim();
                number =etMobile.getText().toString().trim();
                pincode =etAddress.getText().toString().trim();
                bloodgroup=spinner.getSelectedItem().toString();
                password =etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(name)) {
                    etFullName.setError("Invalid Name");
                }
                if (!isValidEmail(email)) {
                    etEmail.setError("Invalid Email");
                }

                if (!isValidNumber(number)) {
                    etMobile.setError("Invalid Number");
                }

                if (!isValidPincode(pincode)) {
                    etAddress.setError("Invalid Pincode");
                }

                if (!isValidPassword(password)) {
                    Toast.makeText(RegisterActivity.this, "Password Should be Minimum 6 Letters", Toast.LENGTH_SHORT).show();
                }


                if(TextUtils.isEmpty(email))
                {
                    if ((TextUtils.isEmpty(password)))
                        Toast.makeText(RegisterActivity.this,"Please Enter Details", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(email))
                {
                    Toast.makeText(RegisterActivity.this,"Please Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password))
                {
                    Toast.makeText(RegisterActivity.this,"Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (bloodgroup.equals("Select Your Blood Group")) {
                    Toast.makeText(RegisterActivity.this, "Select Your Blood Group", Toast.LENGTH_SHORT).show();
                }
                else {

                    saveDate();
                }


            }
        });

    }
    private boolean isValidEmail(String Emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Emailid);
        return matcher.matches();
    }

    public boolean isValidNumber(String num)
    {
        return num.length() == 10;
    }
    public boolean isValidPassword(String pass) {
        return pass.length() >= 6;
    }
    public boolean isValidPincode(String pin) {
        return pin.length() == 6;
    }

    private void saveDate(){


        if (!name.isEmpty() && !pincode.isEmpty() && isValidPincode(pincode) && !email.isEmpty() &&isValidEmail(email)  && !number.isEmpty() &&isValidNumber(number) && !password.isEmpty() && isValidPassword(password) && !bloodgroup.equals("Select Your Blood Group") ) {

            String id = myRef.push().getKey();
            Details details = new Details(id, name,email,number,password,bloodgroup,pincode);
            myRef.child(id).setValue(details);
            progressDialog.setMessage("Please Wait");
            progressDialog.show();
            mAuth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                        finish();

                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Email Already Exists.",
                                Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });


        } else {
            Toast.makeText(getApplicationContext(), "Please enter your details!", Toast.LENGTH_LONG).show();
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
