package com.servicehub.Activity;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.Custom_Edittext_Bold;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_User;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Change_pwd_Activity extends AppCompatActivity {

    ImageView back;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    RelativeLayout rr_change_pwd;
    ArrayList<Model_User> Array_change_pwd = new ArrayList<>();
    Custom_Edittext_Bold et_old_pwd, et_new_pwd, et_conf_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        et_old_pwd = (Custom_Edittext_Bold) findViewById(R.id.et_old_pwd);
        et_new_pwd = (Custom_Edittext_Bold) findViewById(R.id.et_new_pwd);
        et_conf_pwd = (Custom_Edittext_Bold) findViewById(R.id.et_conf_pwd);
        rr_change_pwd = (RelativeLayout) findViewById(R.id.rr_change_pwd);
        rr_change_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });
    }

    //
    public void Validate() {
        if (et_old_pwd.getText().toString().equals("")) {
            et_old_pwd.setError("Enter Old Password");

        } else if (et_new_pwd.getText().toString().equals("")) {
            et_new_pwd.setError("Enter New Password");

        } else if (!et_new_pwd.getText().toString().equals(et_conf_pwd.getText().toString())) {
            et_conf_pwd.setError("Password does not match");

        } else {
            Array_change_pwd.clear();
            Model_User user=new Model_User();
            user.setOldpwd(et_old_pwd.getText().toString().trim());
            user.setNewpwd(et_new_pwd.getText().toString().trim());
            Array_change_pwd.add(user);
            Change_pwd();
        }
    }


    public void Change_pwd() {

        obj_dialog.show();
        String url = Configr.app_url + "changepassword";
        String json = "";

        json = JSON.add_1(Array_change_pwd, pref, "changepassword");

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);


        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();

                String toastMsg = "";
                toastMsg = "";


                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    String otp = "", userid = "";

                    if (res_flag.equals("200")) {

                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
            }
        };
        Connection.postconnection(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }

}
