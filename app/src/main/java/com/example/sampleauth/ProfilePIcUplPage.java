package com.example.sampleauth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import io.grpc.Context;

public class ProfilePIcUplPage extends AppCompatActivity {

    ImageView ProfilePic;
    final int GINT=1;
    Uri StorePic;
    Button btUpload,btNext;
    FirebaseStorage parent;
    StorageReference subnode;

    FirebaseAuth mAuth;

    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_p_ic_upl_page);
        final ProgressDialog dialog=new ProgressDialog(ProfilePIcUplPage.this);
        mAuth=FirebaseAuth.getInstance();

        parent=FirebaseStorage.getInstance();
        btUpload=findViewById(R.id.btnUpload);
        btNext=findViewById(R.id.btNext);
        btNext.setVisibility(View.INVISIBLE);
        ProfilePic=findViewById(R.id.ImgDp);
        ProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               chooseDp();
            }
        });

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.setTitle("File Uploader");
                dialog.show();
                subnode=parent.getReference().child("Profile_Pic").child(mAuth.getCurrentUser().getUid());
                subnode.putFile(StorePic).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double percent= (100*snapshot.getBytesTransferred()/(snapshot.getTotalByteCount()));
                        dialog.setMessage("Uploaded :"+(int)percent+" %");
                    }
                })
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        dialog.dismiss();
                        btNext.setVisibility(View.VISIBLE);
                        Toast.makeText(ProfilePIcUplPage.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProfilePIcUplPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


            }
        });
        btNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfilePIcUplPage.this,com.example.sampleauth.DisplayInfo.class));
                finish();
            }
        });
    }

    private void chooseDp() {

        Intent gallery=new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(gallery,GINT);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GINT&&resultCode==RESULT_OK&&data!=null)
        {
            StorePic=data.getData();
            ProfilePic.setImageURI(StorePic);
        }
    }
}