package com.servicehub.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.servicehub.Adapter.Category_list_Adapter;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_User;
import com.servicehub.Model.Model_profile;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    GridView gridview;
    Category_list_Adapter adapter;
    ArrayList<Model_service_list> arrayList = new ArrayList<>();
    // ArrayList<Integer> arrayList = new ArrayList<>();
    DrawerLayout drawer;
    ImageView notify_bar;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    NavigationView navigationView;
    ArrayList<Model_profile> array_profile = new ArrayList<>();
    CustomTextview_Bold username;
    CircleImageView profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));


        gridview = (GridView) findViewById(R.id.gridview);

        adapter = new Category_list_Adapter(MainActivity.this, arrayList);
        gridview.setAdapter(adapter);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        username = (CustomTextview_Bold) navigationView.getHeaderView(0).findViewById(R.id.username);
        profile = (CircleImageView) navigationView.getHeaderView(0).findViewById(R.id.profile);

        notify_bar = (ImageView) findViewById(R.id.notify_bar);
        notify_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
                Profile();
            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (arrayList.get(i).getStatus() == 1) {
                    Model_service_list model = arrayList.get(i);
                    Intent i1 = new Intent(MainActivity.this, Add_Address.class);
                    i1.putExtra("model", model);
                    startActivity(i1);
                } else {
                    for (int pos = 0; pos < arrayList.size(); pos++) {
                        if (pos == i) {
                            arrayList.get(pos).setStatus(1);
                        } else {
                            arrayList.get(pos).setStatus(0);
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
        Service_list();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Service_list();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        if (id == R.id.nav_profile) {
            Intent i1 = new Intent(MainActivity.this, Profile.class);
            startActivity(i1);
        } else if (id == R.id.nav_my_order) {
            Intent i = new Intent(MainActivity.this, My_order.class);
            startActivity(i);
        } else if (id == R.id.nav_change_pwd) {
            Intent i = new Intent(MainActivity.this, Change_pwd_Activity.class);
            startActivity(i);
        } else if (id == R.id.nav_logout) {
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

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Service_list() {

        obj_dialog.show();

        String url = Configr.app_url + "servicelist/" + pref.getStr_Userid();

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
                        JSONArray jarray1 = jarray.getJSONArray("service");

                        arrayList.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            JSONObject jsonObject = jarray1.getJSONObject(i);
                            Model_service_list model = new Model_service_list();
                            model.setService_id(jsonObject.getString("serviceid"));
                            model.setService_name(jsonObject.getString("name"));
                            model.setService_image(jsonObject.getString("image"));
                            model.setSelected_image(jsonObject.getString("selectimage"));
                            model.setService_rate(jsonObject.getString("rate"));
                            model.setStatus(0);

                            arrayList.add(model);
                        }

                        adapter.notifyDataSetChanged();
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

                            username.setText(jsonObject.getString("name"));
                            if (jsonObject.getString("userimg").equals("")) {
                                profile.setImageResource(R.drawable.default_imggg);
                            } else {
                                Glide.with(context).load(jsonObject.getString("userimg")).into(profile);
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
                        Intent i = new Intent(MainActivity.this, LoginActivity.class);
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

}
