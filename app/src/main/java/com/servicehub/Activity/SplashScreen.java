package com.servicehub.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.servicehub.Employee.MainEmployee_Activity;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

public class SplashScreen extends AppCompatActivity {


    Pref_Master pref;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        pref = new Pref_Master(context);
        Thread t1 = new Thread() {
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (pref.getStr_login_flag().equals("login")) {
                        Intent i = new Intent(SplashScreen.this, MainActivity.class);
                        startActivity(i);
                    }
                    else if(pref.getStr_login_flag().equals("login_emp")) {
                        Intent i1 = new Intent(SplashScreen.this, MainEmployee_Activity.class);
                        startActivity(i1);
                    }
                    else {
                        Intent i1 = new Intent(SplashScreen.this, LoginActivity.class);
                        startActivity(i1);
                    }
                }
            }

        };
        t1.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
