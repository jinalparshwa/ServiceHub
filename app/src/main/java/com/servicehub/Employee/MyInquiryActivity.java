package com.servicehub.Employee;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.servicehub.Adapter.My_inquiry_Adapter;
import com.servicehub.Adapter.New_inquiry_Adapter;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Model.Model_New_Inquiry;
import com.servicehub.Model.Model_service_list;
import com.servicehub.Model.Model_time;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyInquiryActivity extends AppCompatActivity {

    ArrayList<Model_New_Inquiry> arrayList = new ArrayList<>();
    ArrayList<Model_New_Inquiry> arrayList1 = new ArrayList<>();
    ListView listview_Myinquiry;
    My_inquiry_Adapter adapter;
    ImageView back;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    CustomTextview_Bold txt_total_job, txt_total;
    ArrayList<Model_time> model_time = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inquiry);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        listview_Myinquiry = (ListView) findViewById(R.id.listview_Myinquiry);

        adapter = new My_inquiry_Adapter(MyInquiryActivity.this, arrayList, MyInquiryActivity.this,pref);
        listview_Myinquiry.setAdapter(adapter);

        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        txt_total_job = (CustomTextview_Bold) findViewById(R.id.txt_total_job);
        txt_total = (CustomTextview_Bold) findViewById(R.id.txt_total);
        getmyinquiry();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getmyinquiry();
    }

    public void getmyinquiry() {

        obj_dialog.show();

        String url = Configr.app_url + "getmyinquiry/" + pref.getStr_Userid();

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
                        JSONArray jarray1 = jarray.getJSONArray("inquiry");
                        JSONArray jarray2 = jarray.getJSONArray("total_job");

                        arrayList.clear();
                        arrayList1.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            JSONObject jsonObject = jarray1.getJSONObject(i);
                            Model_New_Inquiry model = new Model_New_Inquiry();
                            model.setInquiry_id(jsonObject.getString("inquiryid"));
                            model.setInquiry_no(jsonObject.getString("inquiryno"));
                            model.setS_time(jsonObject.getString("starttime"));
                            model.setHours(jsonObject.getString("hours"));
                            model.setE_time(jsonObject.getString("endtime"));
                            model.setAmount(jsonObject.getString("paymentamount"));
                            model.setCode_status(jsonObject.getString("code_status"));
                            arrayList.add(model);
                        }

                        for (int i = 0; i < jarray2.length(); i++) {
                            Model_New_Inquiry model = new Model_New_Inquiry();
                            JSONObject jobj1 = jarray2.getJSONObject(i);
                            model.setNo_of_job(jobj1.getString("no_of_job"));
                            model.setTotal(jobj1.getString("total"));
                            arrayList1.add(model);
                        }

                        if (arrayList1.size() != 0) {
                            txt_total_job.setText(arrayList1.get(0).getNo_of_job());
                            txt_total.setText(arrayList1.get(0).getTotal() + "/-");

                        } else {
                            txt_total_job.setText("");
                            txt_total.setText("");
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

    public void Check_code(String inquiry_id, String srt) {


        String url = Configr.app_url + "checkcode/" + inquiry_id + "/" + srt;

        Map<String, String> params = new HashMap<>();

        Response.Listener<String> lis_res = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);

                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");

                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {

                        JSONObject jarray = new JSONObject(jobj.getString("data"));
                        JSONObject jsonObj = jarray.getJSONObject("code");

                        Model_time model=new Model_time();
                        model.setStart_time(jsonObj.getString("starttime"));
                        model.setEnd_time(jsonObj.getString("endtime"));
                        model.setHours(jsonObj.getString("hours"));
                        model.setInq_id(jsonObj.getString("inquiryid"));
                        model_time.add(model);

                        Intent i = new Intent(context, Service_time.class);
                        Log.e("Start_time",":"+model);
                        Log.e("Inquiry_pass",model.getInq_id());
                        i.putExtra("start_time",model);
                        context.startActivity(i);
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
            }
        };
        Connection.getconnectionVolley(url, params, Configr.getHeaderParam(), context, lis_res, lis_error);
    }

}
