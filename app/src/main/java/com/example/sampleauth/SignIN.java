package com.example.sampleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SignIN extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference smallnode;
    FirebaseDatabase bignode;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    String MailStr,PassStr;
    EditText LogMail,LogPass;
    Button LogIn;
    TextView navigate,reset;
    ProgressBar inLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_i_n);

        LogMail=findViewById(R.id.MailSignIN);
        LogPass=findViewById(R.id.PassSignIN);
        LogIn=findViewById(R.id.btLogin);
        mAuth=FirebaseAuth.getInstance();
        bignode=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        MailStr=LogMail.getText().toString();
        PassStr=LogPass.getText().toString();
        inLoad=findViewById(R.id.logProg);
        inLoad.setVisibility(View.INVISIBLE);
        navigate=findViewById(R.id.LogOldU);
        reset=findViewById(R.id.ResetPass);


        LogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                {
                    inLoad.setVisibility(View.VISIBLE);
                    BackEnd logob=new BackEnd();
                    logob.store();
                    if(!logob.check()){inLoad.setVisibility(View.INVISIBLE);return;}

                    mAuth.signInWithEmailAndPassword(MailStr,PassStr).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override

                        public void onSuccess(AuthResult authResult) {

                            if(mAuth.getCurrentUser().isEmailVerified()){
                            smallnode=bignode.getReference().child("UserData").child(mAuth.getCurrentUser().getUid()).child("Password");
                            smallnode.setValue(PassStr);
                            storageReference=firebaseStorage.getReference("Profile_Pic").child(mAuth.getCurrentUser().getUid());
                            storageReference.getDownloadUrl().addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    inLoad.setVisibility(View.INVISIBLE);
                                    startActivity(new Intent(com.example.sampleauth.SignIN.this,com.example.sampleauth.ProfilePIcUplPage.class));

                                }
                            }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    inLoad.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SignIN.this,"Redirecting to User_Profile Page",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(com.example.sampleauth.SignIN.this,com.example.sampleauth.DisplayInfo.class));
                                    finish();
                                }
                            });
                            }

                            else
                            {
                                inLoad.setVisibility(View.INVISIBLE);
                                Toast.makeText(SignIN.this, "Verify your Mail_ID to prove Authenticity", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            inLoad.setVisibility(View.INVISIBLE);
                            Toast.makeText(SignIN.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

        navigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.sampleauth.SignIN.this,com.example.sampleauth.SignUp.class));
                finish();
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.sampleauth.SignIN.this,com.example.sampleauth.ResetPass.class));
            }
        });
    }

    class BackEnd
    {
        public void store()
        {

            MailStr=LogMail.getText().toString().trim();
            PassStr=LogPass.getText().toString().trim();
        }

        public boolean check()
        {
            if(MailStr.isEmpty()||PassStr.isEmpty())
            {
                if(MailStr.isEmpty()){ LogMail.requestFocus(); LogMail.setError("Please Enter Your Mail_ID");}
                if(PassStr.isEmpty()){LogPass.requestFocus(); LogPass.setError("Enter Your Password to Login");}
                else {return false;}
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(MailStr).matches())
            {
                LogMail.requestFocus();LogMail.setError("Please Provide a Valid Mail_ID");return false;
            }
            if(LogPass.getText().toString().trim().length()<=6)
            {
                LogPass.requestFocus();LogPass.setError("Minimum Length of Password should be More than 6_UNITS");return false;
            }
            else {return true;}

        }

    }

}