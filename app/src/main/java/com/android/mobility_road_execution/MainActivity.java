package com.android.mobility_road_execution;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
TextView t1 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        t1=findViewById(R.id.textView);
int i =0;
        final  Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                t1.append(".");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        t1.append(".");
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                t1.append(".");
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(MainActivity.this,Emotion_Detect.class);
                                    startActivity(intent);
                                }
                            },500);
                            }
                        },1200);

                    }
                },1200);

            }
        },1200);


    }

}
