package com.servicehub.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Custom_Compo.Custom_Edittext;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_Book;
import com.servicehub.Model.Model_City_Area;
import com.servicehub.Model.Model_User;
import com.servicehub.Model.Model_employee;
import com.servicehub.Model.Model_feedback;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Review_Activity extends AppCompatActivity {

    ImageView back;
    CircleImageView img_pic;
    Context context = this;
    Pref_Master pref;
    String empid, inq_id;
    CustomTextview_Bold emp_name;
    RelativeLayout r_save;
    RatingBar rating;
    Custom_Edittext et_comment;
    Activity_indicator obj_dialog;
    ArrayList<Model_employee> model_employee = new ArrayList<>();
    ArrayList<Model_feedback> model_feedback = new ArrayList<>();
    String rate, inqid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            empid = extras.getString("empid");
            inq_id = extras.getString("inquiry_id");
            Log.e("empppidd", "" + empid);

        } else {
            Log.e("elseeeeeee", "Enter");
        }


        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        rating = (RatingBar) findViewById(R.id.rating);
        rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rate = String.valueOf(v);


            }
        });

        et_comment = (Custom_Edittext) findViewById(R.id.et_comment);

        img_pic = (CircleImageView) findViewById(R.id.img_pic);
        emp_name = (CustomTextview_Bold) findViewById(R.id.emp_name);
        r_save = (RelativeLayout) findViewById(R.id.r_save);
        r_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });

        get_employee();
    }

    public void Validate() {
        if (et_comment.getText().toString().equals("")) {
            et_comment.setError("Enter Comments");
        } else {
            model_feedback.clear();
            Model_feedback model = new Model_feedback();
            model.setInq_id(inqid);
            model.setRatings(rate);
            model.setComments(et_comment.getText().toString());
            model_feedback.add(model);
            Feedback();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void get_employee() {

        //obj_dialog.show();

        String url = Configr.app_url + "getemployeeDetails/" + empid + "/" + inq_id;
        String json = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                // obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {

                        JSONObject job = new JSONObject(jobj.getString("data"));
                        JSONArray jarray1 = new JSONArray(job.getString("employee"));

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_employee model = new Model_employee();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setEmp_id(jobj1.getString("employeeid"));
                            model.setEmp_name(jobj1.getString("employeename"));
                            model.setEmp_image(jobj1.getString("employeeimg"));
                            model.setInq_id(jobj1.getString("inquiryid"));
                            inqid = (jobj1.getString("inquiryid"));
                            model_employee.add(model);

                            emp_name.setText(jobj1.getString("employeename"));
                            if (jobj1.getString("employeeimg").equals("")) {
                                img_pic.setImageResource(R.drawable.default_imggg);
                            } else {
                                Glide.with(context).load(jobj1.getString("employeeimg")).into(img_pic);
                            }
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
                //obj_dialog.dismiss();

            }
        };
        Connection.getconnectionVolley(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }

    public void Feedback() {

        obj_dialog.show();

        String url = Configr.app_url + "feedback";
        String json = "";

        json = JSON.add_4(model_feedback, pref, "feedback");

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
                        Toast.makeText(context, res_msg, Toast.LENGTH_SHORT).show();
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
