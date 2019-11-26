package com.projects.bloodbank.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.projects.bloodbank.R;
import com.projects.bloodbank.receiver.NetworkStateChangeReceiver;
import com.projects.bloodbank.utilities.ConstantValues;
import com.projects.bloodbank.utilities.MyAppPrefsManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.projects.bloodbank.receiver.NetworkStateChangeReceiver.IS_NETWORK_AVAILABLE;

public class LoginActivity extends Activity implements View.OnClickListener {
    EditText etMobile,etPassword;
    Button btnSignIn;
    TextView txtRegister;
    private FirebaseAuth mAuth;
    //private FirebaseAuth.AuthStateListener authStateListener;
    ProgressDialog progressDialog;
    MyAppPrefsManager myAppPrefsManager;
    boolean doubleBackToExitPressedOnce = false;

    Snackbar snackbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        myAppPrefsManager= new MyAppPrefsManager(LoginActivity.this);
        ConstantValues.internetCheck(LoginActivity.this);


      /*  authStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    //Toast.makeText(LoginActivity.this, "hi", Toast.LENGTH_SHORT).show();

                }

            }
        };*/
        etMobile = (EditText) findViewById(R.id.etMobile);
        etPassword = (EditText) findViewById(R.id.etPassword);

        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        btnSignIn.setOnClickListener(this);

        txtRegister = (TextView) findViewById(R.id.txtRegister);
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent register = new Intent(LoginActivity.this, RegisterActivity.class);
                register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(register);

            }
        });
    }
   /* @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);


    }*/
    private boolean isValidEmail(String Emailid) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(Emailid);
        return matcher.matches();
    }
    public void onClick(View V){
        String email=etMobile.getText().toString().trim();
        if (!isValidEmail(email)) {
            etMobile.setError("Invalid Email");
        }
        String password=etPassword.getText().toString().trim();
        if(TextUtils.isEmpty(email))
        {
            if ((TextUtils.isEmpty(password)))
                Toast.makeText(this,"Please Enter Email and Password", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this,"Please Enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this,"Plesse Enter Password", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Please Wait");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){


                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this,"Please Enter Valid Email/Password", Toast.LENGTH_SHORT).show();


                        }
                        
                        else {
                            myAppPrefsManager.setUserLoggedIn(true);

                            // Set isLogged_in of ConstantValues
                            ConstantValues.IS_USER_LOGGED_IN = myAppPrefsManager.isUserLoggedIn();
                            Toast.makeText(LoginActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                            finish();

                            startActivity(new Intent(getApplicationContext(), HomeActivity.class));


                        }

                    }
                });
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
