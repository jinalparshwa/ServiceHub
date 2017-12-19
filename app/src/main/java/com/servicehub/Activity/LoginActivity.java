package com.servicehub.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.firebase.iid.FirebaseInstanceId;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.Custom_Edittext_Bold;
import com.servicehub.Employee.MainEmployee_Activity;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_User;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    Custom_Edittext_Bold et_uname;
    Custom_Edittext_Bold et_pwd;
    CustomTextview forgot_pwd;
    RelativeLayout r_sign_in;
    CustomTextview register;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    public static String reg_id = "";

    ArrayList<Model_User> Array_login = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        et_uname = (Custom_Edittext_Bold) findViewById(R.id.et_uname);
        et_pwd = (Custom_Edittext_Bold) findViewById(R.id.et_pwd);
        forgot_pwd = (CustomTextview) findViewById(R.id.forgot_pwd);
        r_sign_in = (RelativeLayout) findViewById(R.id.r_sign_in);
        register = (CustomTextview) findViewById(R.id.register);

        r_sign_in.setOnClickListener(this);
        register.setOnClickListener(this);

        try {
            reg_id = FirebaseInstanceId.getInstance().getToken();
        } catch (Exception e) {
            e.printStackTrace();
            reg_id = FirebaseInstanceId.getInstance().getToken();
        }
    }

    @Override
    public void onClick(View v) {
        Intent i;
        switch (v.getId()) {
            case R.id.r_sign_in:
                Validate();
                break;
            case R.id.register:
                i = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(i);
                break;
            case R.id.forgot_pwd:
                //i=new Intent(LoginActivity.this,LoginActivity.class);
                //startActivity(i);
                break;
        }
    }

    public void Validate() {
        if (et_uname.getText().toString().equals("")) {
            et_uname.setError("Enter Mobile No.");

        } else if (et_uname.getText().toString().length() != 10) {
            et_uname.setError("Invalid Mobile");

        } else if (et_pwd.getText().toString().equals("")) {
            et_pwd.setError("Enter Password");
        } else {
            Array_login.clear();
            Model_User user = new Model_User();
            user.setMobile(et_uname.getText().toString().trim());
            user.setPassword(et_pwd.getText().toString().trim());
            user.setDeviceid("" + Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            user.setDevicetoken(reg_id);
            Array_login.add(user);
            Login_api();

        }
    }

    public void Login_api() {

        obj_dialog.show();

        String url = Configr.app_url + "loginuser";
        String json = "";

        json = JSON.add_1(Array_login, pref, "loginuser");

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
                        model.setUsername(jarray.getString("name"));
                        model.setUsertypeid(jarray.getString("usertypeid"));


                        pref.setUserid(jarray.getString("userid"));
                        pref.setfname(jarray.getString("name"));
                        Array_login.add(model);


                        if (jarray.getString("usertypeid").equals("2")) {
                            pref.setLogin_Flag("login");
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else if(jarray.getString("usertypeid").equals("3")) {
                            pref.setLogin_Flag("login_emp");
                            Intent i = new Intent(LoginActivity.this, MainEmployee_Activity.class);
                            startActivity(i);
                            finish();
                        }


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
