package com.example.sampleauth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {


     TextView oldU,newU;
     ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newU =findViewById(R.id.newU);
        oldU=findViewById(R.id.oldU);
        back=findViewById(R.id.rainbow);
        back.setTranslationY(-1500);
        newU.setTranslationX(-1000);
        oldU.setTranslationX(1000);
        newU.animate().setDuration(2000).translationX(-50);
        oldU.animate().setDuration(1800).translationX(30);
        back.animate().setDuration(2000).translationY(10);

        newU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                back.animate().rotation(7200).setDuration(2000);
                back.animate().translationY(-10000).setDuration(6000);
                newU.animate().setDuration(2000).translationX(1100);
                oldU.animate().setDuration(2000).translationX(-1100);
                Toast.makeText(MainActivity.this, "Redirecting to Registration Page", Toast.LENGTH_SHORT).show();
                new Timer().schedule(new TimerTask(){
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                startActivity(new Intent(MainActivity.this, SignUp.class));
                                finish();
                            }
                        });
                    }
                }, 2000);
            }
        });

        oldU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back.animate().rotation(7200).setDuration(2000);
                back.animate().translationY(10000).setDuration(6000);
                Toast.makeText(MainActivity.this, "Redirecting to Login Page", Toast.LENGTH_SHORT).show();
                newU.animate().setDuration(2000).translationX(1100);
                oldU.animate().setDuration(2000).translationX(-1100);
                new Timer().schedule(new TimerTask(){
                    public void run() {
                        MainActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                startActivity(new Intent(MainActivity.this, SignIN.class));
                                finish();
                            }
                        });
                    }
                }, 2000);
            }
        });

    }



}