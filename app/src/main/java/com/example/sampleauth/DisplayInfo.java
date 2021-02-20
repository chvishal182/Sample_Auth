package com.example.sampleauth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;

public class DisplayInfo extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseStorage  pub;
    StorageReference sub;
    ImageView DP;
    TextView showName,userName,showPhone,userEmail,showEmail,userPhone;
    Button Signout,ChangePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_info);

        showName=findViewById(R.id.showName);
        Signout=findViewById(R.id.btnSignout);
        ChangePic=findViewById(R.id.btnChange);
        ChangePic.setVisibility(View.INVISIBLE);
        userName=findViewById(R.id.userName);
        showPhone=findViewById(R.id.showPhn);
        userEmail=findViewById(R.id.userMail);
        showEmail=findViewById(R.id.showMail);
        userPhone=findViewById(R.id.userPhn);
        DP=findViewById(R.id.ImgDisplay);
        pub=FirebaseStorage.getInstance();
        firebaseDatabase=FirebaseDatabase.getInstance();
        mAuth=FirebaseAuth.getInstance();
        sub=pub.getReference("Profile_Pic").child(mAuth.getCurrentUser().getUid());
        databaseReference=firebaseDatabase.getReference("UserData").child(mAuth.getCurrentUser().getUid());

        try {
            final File file=File.createTempFile("image","jpeg");
            sub.getFile(file).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap=BitmapFactory.decodeFile(file.getAbsolutePath());
                    DP.setImageBitmap(bitmap);
                    ChangePic.setVisibility(View.VISIBLE);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               userName.setText(snapshot.child("Name").getValue().toString().trim());
               userEmail.setText(snapshot.child("Mail_ID").getValue().toString().trim());
               userPhone.setText(snapshot.child("Contact_Info").getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        Signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                startActivity(new Intent(com.example.sampleauth.DisplayInfo.this,com.example.sampleauth.SignIN.class));
                finish();
            }
        });

        ChangePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(com.example.sampleauth.DisplayInfo.this,com.example.sampleauth.ProfilePIcUplPage.class));

            }
        });
    }
}