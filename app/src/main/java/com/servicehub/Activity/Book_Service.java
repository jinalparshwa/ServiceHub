package com.servicehub.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Rupee;
import com.servicehub.Custom_Compo.Custom_Edittext;
import com.servicehub.Json_Model.JSON;
import com.servicehub.Model.Model_Book;
import com.servicehub.Model.Model_City_Area;
import com.servicehub.Model.Model_User;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class Book_Service extends AppCompatActivity {

    ImageView back;
    RelativeLayout rr_date;
    CustomTextview date;
    CustomTextview tv_rate;
    PopupMenu popup_hours;
    CustomTextview hours_textview;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    ArrayList<Model_City_Area> model_hour = new ArrayList<>();
    ArrayList<String> Array_hour = new ArrayList<>();
    Model_service_list model;
    CustomTextview service_name;
    ImageView service_image;
    TextView tv_cost;
    int rate;
    int hours = 1;
    int total;
    CustomTextview txt_time;
    String s_starttime = "", s_endtime = "";
    int s_time, e_time;
    RelativeLayout r_book;

    Model_Book modelBook;

    ArrayList<Model_Book> Array_book = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_service);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        model = (Model_service_list) getIntent().getSerializableExtra("model");

        modelBook = (Model_Book) getIntent().getSerializableExtra("model_book");


        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rr_date = (RelativeLayout) findViewById(R.id.rr_date);
        date = (CustomTextview) findViewById(R.id.date);
        rr_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDateTimeField(date);
            }
        });

        tv_rate = (CustomTextview) findViewById(R.id.tv_rate);
        hours_textview = (CustomTextview) findViewById(R.id.hours_textview);
        hours_textview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_hours.show();
            }
        });
        popup_hours = new PopupMenu(Book_Service.this, hours_textview);
        get_hours();

        service_name = (CustomTextview) findViewById(R.id.service_name);
        service_image = (ImageView) findViewById(R.id.service_image);
        tv_cost = (TextView) findViewById(R.id.tv_cost);

        tv_rate.setText(model.getService_rate());
        service_name.setText(model.getService_name());
        Glide.with(context).load("" + model.getService_image()).into(service_image);

        rate = Integer.parseInt(model.getService_rate());
        total = (rate * hours);
        tv_cost.setText("" + total);


        txt_time = (CustomTextview) findViewById(R.id.txt_time);
        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTimePickerDialog();
            }
        });

        Calendar ca = Calendar.getInstance();
        s_starttime = Configr.df5.format(ca.getTime());
        Set_Time();
        r_book = (RelativeLayout) findViewById(R.id.r_book);
        r_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();
            }
        });

    }

    public void Validate() {
        if (date.getText().toString().equals("Select Date")) {
            date.setError("Select Date");
        } else {
            Array_book.clear();
            Model_Book model_Book = new Model_Book();
            model_Book.setService(model.getService_id());
            model_Book.setCity(modelBook.getCity());
            model_Book.setArea(modelBook.getArea());
            model_Book.setLandmark(modelBook.getLandmark());
            model_Book.setAddress(modelBook.getAddress());
            model_Book.setHours(String.valueOf(hours));
            model_Book.setCost(tv_cost.getText().toString());
            model_Book.setStime(s_starttime);
            model_Book.setEtime(s_endtime);
            model_Book.setDate(date.getText().toString());
            model_Book.setRate(model.getService_rate());
            Array_book.add(model_Book);
            Book_service();
        }
    }

    public void createPopup_State() {
        popup_hours = new PopupMenu(Book_Service.this, hours_textview);
        for (int i = 0; i < Array_hour.size(); i++) {
            popup_hours.getMenu().add(Menu.NONE, i, Menu.NONE, Array_hour.get(i));
        }
        popup_hours.setOnMenuItemClickListener(onClick_State_popupmenu);
    }

    PopupMenu.OnMenuItemClickListener onClick_State_popupmenu = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            for (int i = 0; i < Array_hour.size(); i++) {
                if (Array_hour.get(i).equals(String.valueOf(item))) {
                    hours_textview.setText(Array_hour.get(i) + " Hour");
                    hours = Integer.parseInt(Array_hour.get(i));
                    total = (rate * hours);
                    tv_cost.setText("" + total);
                    Set_Time();
                    return true;
                }
            }
            return false;
        }
    };

    private void setDateTimeField(final TextView et) {
        final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        Calendar newCalendar = Calendar.getInstance();

        DatePickerDialog fromDatePickerDialog = new DatePickerDialog(Book_Service.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        // TODO Auto-generated method stub
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        et.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        fromDatePickerDialog.getDatePicker().setMinDate(newCalendar.getTimeInMillis());
        fromDatePickerDialog.show();
    }

    private void openTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(Book_Service.this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Calendar calNow = Calendar.getInstance();
                        Calendar calSet = (Calendar) calNow.clone();
                        calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calSet.set(Calendar.MINUTE, minute);
                        calSet.set(Calendar.SECOND, 0);
                        calSet.set(Calendar.MILLISECOND, 0);

                        s_starttime = Configr.df5.format(calSet.getTime());
                        Set_Time();
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), false);
        timePickerDialog.setTitle("Set Book time");
        timePickerDialog.show();

    }

    public void Set_Time() {

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(Configr.df5.parse(s_starttime));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.HOUR, hours);

        s_endtime = Configr.df5.format(calendar.getTime());

        txt_time.setText("" + s_starttime + " - " + s_endtime);
    }

    public void get_hours() {

        obj_dialog.show();

        String url = Configr.app_url + "hourslist/" + pref.getStr_Userid();
        String json = "";

        HashMap<String, String> param = new HashMap<>();
        param.put("data", json);
        Log.e("jinal", ":" + param.toString());

        Response.Listener<String> lis_pat = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("Response", response);
                obj_dialog.dismiss();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String res_flag = jobj.getString("status");
                    String res_msg = jobj.getString("message");
                    if (res_flag.equals("200")) {


                        JSONObject job = new JSONObject(jobj.getString("data"));

                        JSONArray jarray1 = new JSONArray(job.getString("hours"));
                        model_hour.clear();

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_City_Area model = new Model_City_Area();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setHours(jobj1.getString("hournm"));
                            model_hour.add(model);
                        }

                        Array_hour.clear();
                        for (Model_City_Area model : model_hour)
                            Array_hour.add(model.getHours());

                        createPopup_State();

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
        Connection.getconnectionVolley(url, param, Configr.getHeaderParam(), context, lis_pat, lis_error);
    }

    public void Book_service() {

        obj_dialog.show();

        String url = Configr.app_url + "bookservice";
        String json = "";

        json = JSON.add_2(Array_book, pref, "bookservice");

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
                        Intent i=new Intent(Book_Service.this,My_order.class);
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
