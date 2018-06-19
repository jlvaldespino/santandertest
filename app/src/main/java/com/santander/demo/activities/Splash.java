package com.santander.demo.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.santander.demo.R;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        Thread t=new Thread() {
            public void run() {

                try {
                    sleep(5000);


                    Intent i=new Intent(Splash.this,LoginActivity.class);
                    startActivity(i);

                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };


        t.start();
    }
}
