package com.servicehub.Employee;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.servicehub.Activity.LoginActivity;
import com.servicehub.Activity.MainActivity;
import com.servicehub.Activity.My_order;
import com.servicehub.Activity.Profile;
import com.servicehub.Adapter.My_earning_Adapter;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Model.Model_profile;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainEmployee_Activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawer;
    Context context = this;
    Pref_Master pref;
    ImageView notify_bar;
    Activity_indicator obj_dialog;
    CustomTextview count_new_inquiry;
    CustomTextview count_my_inquiry;
    CustomTextview count_my_earning;
    RelativeLayout rl_new_inquiry;
    RelativeLayout rl_inquiry;
    RelativeLayout rl_earning;
    CircleImageView emp_profile;
    CustomTextview_Bold emp_name_;
    ArrayList<Model_profile> array_profile = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_employee);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        drawer = (DrawerLayout) findViewById(R.id.emp_drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.emp_nav_view);
        navigationView.setNavigationItemSelectedListener(MainEmployee_Activity.this);
        emp_name_ = (CustomTextview_Bold) navigationView.getHeaderView(0).findViewById(R.id.emp_name_);
        emp_profile = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.emp_profile);
        notify_bar = (ImageView) findViewById(R.id.emp_notify_bar);
        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
                Profile();
            }
        });
        count_new_inquiry = (CustomTextview) findViewById(R.id.count_new_inquiry);
        count_my_inquiry = (CustomTextview) findViewById(R.id.count_my_inquiry);
        count_my_earning = (CustomTextview) findViewById(R.id.count_my_earning);

        rl_new_inquiry = (RelativeLayout) findViewById(R.id.rl_new_inquiry);
        rl_inquiry = (RelativeLayout) findViewById(R.id.rl_inquiry);
        rl_earning = (RelativeLayout) findViewById(R.id.rl_earning);

        rl_new_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainEmployee_Activity.this, NewInquiryActivity.class);
                startActivity(i);
            }
        });

        rl_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainEmployee_Activity.this, MyInquiryActivity.class);
                startActivity(i);
            }
        });

        rl_earning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainEmployee_Activity.this, MyEarningActivity.class);
                startActivity(i);
            }
        });
        get_total();


    }

    @Override
    protected void onResume() {
        super.onResume();
        get_total();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.emp_drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_empprofile) {
            Intent i1 = new Intent(MainEmployee_Activity.this, Employee_profile.class);
            startActivity(i1);
        } else if (id == R.id.nav_emplogout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage("Are you sure you want to exit?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Log_out();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();

            String myclr = "#19b41e";
            Button bq = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            bq.setTextColor(Color.parseColor(myclr));

            Button bq1 = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            bq1.setTextColor(Color.parseColor(myclr));

        } else if (id == R.id.nav_my_inquiry) {
            Intent i = new Intent(MainEmployee_Activity.this, MyInquiryActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_my_earning) {
            Intent i = new Intent(MainEmployee_Activity.this, MyEarningActivity.class);
            startActivity(i);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.emp_drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Log_out() {

        obj_dialog.show();

        String url = Configr.app_url + "logout/" + pref.getStr_Userid();

        Map<String, String> params = new HashMap<>();

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

                        pref.clear_pref();
                        Intent i = new Intent(MainEmployee_Activity.this, LoginActivity.class);
                        startActivity(i);
                        System.exit(0);

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
                obj_dialog.dismiss();
            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }


    public void get_total() {

        obj_dialog.show();

        String url = Configr.app_url + "getmyearn/" + pref.getStr_Userid();

        Map<String, String> params = new HashMap<>();

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
                        JSONObject jsonObj = jarray.getJSONObject("earn");

                        count_new_inquiry.setText(jsonObj.getString("newinquiry"));
                        count_my_inquiry.setText(jsonObj.getString("myinquiry"));
                        count_my_earning.setText(jsonObj.getString("totearn"));

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
                obj_dialog.dismiss();
            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

    public void Profile() {

        obj_dialog.show();

        String url = Configr.app_url + "myprofile/" + pref.getStr_Userid();

        Map<String, String> params = new HashMap<>();

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
                        JSONArray jarray1 = jarray.getJSONArray("user");

                        for (int i = 0; i < jarray1.length(); i++) {
                            JSONObject jsonObject = jarray1.getJSONObject(i);
                            Model_profile model = new Model_profile();
                            model.setUsertype_id(jsonObject.getString("usertypeid"));
                            model.setName(jsonObject.getString("name"));
                            model.setImage(jsonObject.getString("userimg"));
                            array_profile.add(model);

                            emp_name_.setText(jsonObject.getString("name"));
                            if (jsonObject.getString("userimg").equals("")) {
                                emp_profile.setImageResource(R.drawable.default_imggg);
                            } else {
                                Glide.with(context).load(jsonObject.getString("userimg")).into(emp_profile);
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
                obj_dialog.dismiss();
            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }
}
