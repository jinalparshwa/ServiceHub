package com.servicehub.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.Custom_Edittext_Bold;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_User;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    Custom_Edittext_Bold et_username;
    Custom_Edittext_Bold et_email;
    Custom_Edittext_Bold et_mobile;
    Custom_Edittext_Bold et_password;
    Custom_Edittext_Bold et_retype_pwd;
    RelativeLayout rr_sign_up;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    String reg_id;

    ArrayList<Model_User> Array_registraion = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        try {
            reg_id = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            reg_id = FirebaseInstanceId.getInstance().getToken();
        }

        et_username = (Custom_Edittext_Bold) findViewById(R.id.et_username);
        et_email = (Custom_Edittext_Bold) findViewById(R.id.et_email);
        et_mobile = (Custom_Edittext_Bold) findViewById(R.id.et_mobile);
        et_password = (Custom_Edittext_Bold) findViewById(R.id.et_password);
        et_retype_pwd = (Custom_Edittext_Bold) findViewById(R.id.et_retype_pwd);
        rr_sign_up = (RelativeLayout) findViewById(R.id.rr_sign_up);
        rr_sign_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rr_sign_up:
                Validate();
                break;

        }
    }


    private void Validate() {
        if (et_username.getText().toString().equals("")) {
            et_username.setError("Enter Username");

        } else if (et_email.getText().toString().equals("")) {
            et_email.setError("Enter Email Address");

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                et_email.getText().toString()).matches()) {
            et_email.setError("Invalid Email");

        } else if (et_mobile.getText().toString().equals("")) {
            et_mobile.setError("Enter Mobile number");

        } else if (et_mobile.getText().toString().length() != 10) {
            et_mobile.setError("Invalid Mobile");

        } else if (et_password.getText().toString().equals("")) {
            et_password.setError("Enter password");

        } else if (!et_password.getText().toString().equals(et_retype_pwd.getText().toString())) {
            et_retype_pwd.setError("Password does not match");

        } else {
            Array_registraion.clear();
            Model_User user = new Model_User();
            user.setUsername(et_username.getText().toString().trim());
            user.setEmail(et_email.getText().toString().trim());
            user.setMobile(et_mobile.getText().toString().trim());
            user.setPassword(et_password.getText().toString().trim());
            user.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            user.setDevicetoken(reg_id);
            Array_registraion.add(user);
            Registration_api();

        }
    }

    public void Registration_api() {

        obj_dialog.show();

        String url = Configr.app_url + "registeruser";
        String json = "";

        json = JSON.add_1(Array_registraion, pref, "registeruser");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                obj_dialog.dismiss();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {
                        JSONObject jarray = new JSONObject(jobj.getString("data"));


                        Model_User model = new Model_User();
                        model.setUserid(jarray.getString("userid"));

                        pref.setUserid(jarray.getString("userid"));
                        Array_registraion.add(model);


                        pref.setLogin_Flag("login");
                        Intent i = new Intent(RegistrationActivity.this, LoginActivity.class);
                        startActivity(i);
                        finish();

                    } else {
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };

        Response.ErrorListener lis_error = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //  signup.setClickable(true);
                obj_dialog.dismiss();
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_res, lis_error);
    }
}
