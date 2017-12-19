package com.servicehub.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.servicehub.Custom_Compo.Custom_Edittext_Bold;
import com.servicehub.R;

public class OtpActivity extends AppCompatActivity implements View.OnClickListener {

    Custom_Edittext_Bold et_otp;
    RelativeLayout rr_otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        et_otp = (Custom_Edittext_Bold) findViewById(R.id.et_otp);
        rr_otp = (RelativeLayout) findViewById(R.id.rr_otp);
        rr_otp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.rr_otp:
                i = new Intent(OtpActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
                break;

        }
    }
}
