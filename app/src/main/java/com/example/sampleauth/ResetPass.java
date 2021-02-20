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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;

public class ResetPass extends AppCompatActivity {

    FirebaseAuth reAuth;
    EditText ResetMail;
    Button btnReset;
    String reMail;
    ProgressBar resetProg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        reAuth = FirebaseAuth.getInstance();
        ResetMail = findViewById(R.id.ResetMail);
        btnReset = findViewById(R.id.btnReset);
        reMail=ResetMail.getText().toString().trim();
        resetProg=findViewById(R.id.restProg);

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkreMail())
                {
                    resetProg.setVisibility(View.VISIBLE);
                    reAuth.sendPasswordResetEmail(reMail).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            resetProg.setVisibility(View.INVISIBLE);
                            Toast.makeText(ResetPass.this, "A Reset Password Mail has been sent please check your Mail", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(com.example.sampleauth.ResetPass.this,com.example.sampleauth.SignIN.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resetProg.setVisibility(View.INVISIBLE);
                            Toast.makeText(ResetPass.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean checkreMail() {


        if(!Patterns.EMAIL_ADDRESS.matcher(reMail).matches())
        {
            ResetMail.requestFocus();ResetMail.setError("Please Provide a Valid Mail_ID");return false;
        }
        if(reMail.isEmpty())
        {
             ResetMail.requestFocus(); ResetMail.setError("Please Enter Your Mail_ID");
            return false;
        }
        else {return true;}
    }
    }