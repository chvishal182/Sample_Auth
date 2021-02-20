package com.example.sampleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public  class SignUp extends AppCompatActivity {


    static FirebaseAuth mAuth;
    static FirebaseDatabase parent;
    static DatabaseReference subnode;
    static EditText Name,Email,Pass,Phone;
    static String UName,UEmail,UPass,UPhone;
    static Button btnSignUp;
    TextView SOldU;
    ProgressBar progressBar;
    Map<String,String>userData= new HashMap<String,String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Name=(EditText)findViewById(R.id.EtxtName);
        Email=findViewById(R.id.EtxtMail);
        Pass=findViewById(R.id.EtxtPass);
        Phone=findViewById(R.id.EtxtNum);
        btnSignUp=findViewById(R.id.BtSignUp);
        SOldU=findViewById(R.id.LogOldU);
        mAuth=FirebaseAuth.getInstance();
        parent=FirebaseDatabase.getInstance();
        progressBar=findViewById(R.id.progressSU);
        progressBar.setVisibility(View.INVISIBLE);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignUpWork work=new SignUpWork();

                work.store();
                if(!work.check()){return;}
                progressBar.setVisibility(View.VISIBLE);
                work.createUser();


            }
        });

        SOldU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUp.this,com.example.sampleauth.SignIN.class));
            }
        });


    }
    class SignUpWork
    {
        public void store()
        {
            UName=Name.getText().toString().trim();
            UEmail=Email.getText().toString().trim();
            UPass=Pass.getText().toString().trim();
            UPhone=Phone.getText().toString().trim();
        }

        public boolean check()
        {
            if(UName.isEmpty()||UEmail.isEmpty()||UPhone.isEmpty()||UPass.isEmpty())
            {
                if(UName.isEmpty()){ Name.requestFocus();Name.setError("Please Enter Your Name");}
                if(UEmail.isEmpty()){ Email.requestFocus(); Email.setError("Please Enter Your Mail_ID");}
                if(UPass.isEmpty()){Pass.requestFocus(); Pass.setError("Set A Password to Secure your Account");}
                if(UPhone.isEmpty()){Phone.requestFocus(); Phone.setError("Please Enter Your Contact No.");}
               else {return false;}
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(UEmail).matches())
            {
                Email.requestFocus();Email.setError("Please Provide a Valid Mail_ID");return false;
            }
            if(UPass.length()<=6)
            {
                Pass.requestFocus();Pass.setError("Minimum Length of Password should be More than 6_UNITS");return false;
            }
            else {return true;}

        }

        private void createUser()
        {
            mAuth.createUserWithEmailAndPassword(UEmail,UPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if(task.isSuccessful())
                    {    progressBar.setVisibility(View.INVISIBLE);
                        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid)
                            {

                                Toast.makeText(SignUp.this, "User Registration has been Successful,\nA Verification Mail has been Sent ", Toast.LENGTH_LONG).show();
                                saveudate();

                                startActivity(new Intent(SignUp.this,com.example.sampleauth.SignIN.class));
                                    finish();

                            }


                        });
                    }else {
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(SignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        private void saveudate()
        {
            subnode=parent.getReference().child("UserData").child(mAuth.getCurrentUser().getUid());
            userData.put("Name",UName);
            userData.put("Mail_ID",UEmail);
            userData.put("Password",UPass);
            userData.put("Contact_Info",UPhone);
            subnode.setValue(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(SignUp.this,"User_Data has been Registered",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(SignUp.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}