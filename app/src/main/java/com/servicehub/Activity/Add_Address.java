package com.servicehub.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.servicehub.Controller.Activity_indicator;
import com.servicehub.Controller.Configr;
import com.servicehub.Controller.Connection;
import com.servicehub.Custom_Compo.Custom_Edittext;
import com.servicehub.Model.Model_Book;
import com.servicehub.Model.Model_City_Area;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Add_Address extends AppCompatActivity {

    ImageView back;
    RelativeLayout r_save;
    Custom_Edittext landmark;
    Custom_Edittext full_address;
    Spinner spinner_city;
    Spinner spinner_area;
    Activity_indicator obj_dialog;
    Context context = this;
    Pref_Master pref;
    ArrayList<Model_City_Area> model_city = new ArrayList<>();
    ArrayList<String> Array_city = new ArrayList<>();

    ArrayList<Model_City_Area> model_area = new ArrayList<>();
    ArrayList<String> Array_area = new ArrayList<>();

    Model_service_list model;
    TextView service_name;
    ImageView service_image;
    String city_id, area_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        pref = new Pref_Master(context);
        obj_dialog = new Activity_indicator(context);
        obj_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));

        model = (Model_service_list) getIntent().getSerializableExtra("model");

        Init();
        Set_Click();
        get_City();
        //get_Area();

        service_name.setText(model.getService_name());
        Glide.with(context).load("" + model.getService_image()).into(service_image);

    }


    public void Init() {
        back = (ImageView) findViewById(R.id.back);
        r_save = (RelativeLayout) findViewById(R.id.r_save);

        landmark = (Custom_Edittext) findViewById(R.id.landmark);
        full_address = (Custom_Edittext) findViewById(R.id.full_address);

        spinner_city = (Spinner) findViewById(R.id.spinner_city);
        spinner_area = (Spinner) findViewById(R.id.spinner_area);

        service_name = (TextView) findViewById(R.id.service_name);
        service_image = (ImageView) findViewById(R.id.service_image);
    }

    public void Set_Click() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        r_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Validate();

            }
        });
        landmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new PlaceAutocomplete
                            .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                            .build(Add_Address.this);
                    startActivityForResult(intent, 1);
                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });


        spinner_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!spinner_city.getSelectedItem().equals("Select City")) {
                    for (int pos = 0; pos < model_city.size(); pos++) {
                        if (model_city.get(pos).getCityname().equals(spinner_city.getSelectedItem())) {
                            get_Area(model_city.get(pos).getCityid());
                            break;
                        }
                    }

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void Validate() {
        if (spinner_city.getSelectedItem().toString().equals("Select City")) {
            TextView errorText = (TextView) spinner_city.getSelectedView();
            errorText.setError("Select State");
            errorText.setTextColor(Color.RED);
        } else if (spinner_area.getSelectedItem().toString().equals("Select Area")) {
            TextView errorText = (TextView) spinner_area.getSelectedView();
            errorText.setError("Select State");
            errorText.setTextColor(Color.RED);
        } else if (landmark.getText().toString().equals("")) {
            landmark.setError("Select Landmark");

        } else if (full_address.getText().toString().equals("")) {
            full_address.setError("Enter Address");
        } else {
            for (Model_City_Area model : model_city) {
                if (model.getCityname().equals(spinner_city.getSelectedItem())) {
                    Log.e("City", model.getCityname());
                    city_id = "" + model.getCityid();
                    break;
                }
            }
            for (Model_City_Area model : model_area) {
                if (model.getAreaname().equals(spinner_area.getSelectedItem())) {
                    Log.e("Area", model.getAreaname());
                    area_id = "" + model.getAreaid();
                    break;
                }
            }
            Check_area();

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // retrive the data by using getPlace() method.
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.e("Tag", "Place: " + place.getAddress() + place.getPhoneNumber());

                landmark.setText(place.getAddress() + "\n" + place.getPhoneNumber());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.e("Tag", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }


    public void get_City() {

        obj_dialog.show();

        String url = Configr.app_url + "citylist/" + pref.getStr_Userid();
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

                        JSONArray jarray1 = new JSONArray(job.getString("city"));
                        model_city.clear();

                        Model_City_Area model1 = new Model_City_Area();
                        model1.setCityid("-1");
                        model1.setCityname("Select City");
                        model_city.add(model1);

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_City_Area model = new Model_City_Area();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setCityid(jobj1.getString("cityid"));
                            model.setCityname(jobj1.getString("name"));
                            model_city.add(model);
                        }

                        Array_city.clear();
                        for (Model_City_Area model : model_city)
                            Array_city.add(model.getCityname());

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_city);
                        adapter1.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_city.setAdapter(adapter1);

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


    public void get_Area(String cityid) {

        //obj_dialog.show();

        String url = Configr.app_url + "arealist/" + pref.getStr_Userid() + "/" + cityid;
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

                        JSONArray jarray1 = new JSONArray(job.getString("area"));
                        model_area.clear();

                        Model_City_Area model1 = new Model_City_Area();
                        model1.setAreaid("-1");
                        model1.setAreaname("Select Area");
                        model_area.add(model1);

                        for (int i = 0; i < jarray1.length(); i++) {
                            Model_City_Area model = new Model_City_Area();
                            JSONObject jobj1 = jarray1.getJSONObject(i);
                            model.setAreaid(jobj1.getString("areaid"));
                            model.setAreaname(jobj1.getString("areanm"));
                            model_area.add(model);
                        }

                        Array_area.clear();
                        for (Model_City_Area model : model_area)
                            Array_area.add(model.getAreaname());

                        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(context, R.layout.spinner_textview, Array_area);
                        adapter1.setDropDownViewResource(R.layout.spinner_textview);
                        spinner_area.setAdapter(adapter1);

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

    public void Check_area() {

        //obj_dialog.show();

        String url = Configr.app_url + "checkarea/" + model.getService_id() + "/" + city_id + "/" + area_id;
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

                        Intent i = new Intent(Add_Address.this, Book_Service.class);
                        Model_Book Book = new Model_Book();
                        Book.setCity(city_id);
                        Book.setArea(area_id);
                        Book.setLandmark(landmark.getText().toString());
                        Book.setAddress(full_address.getText().toString());
                        i.putExtra("model_book", Book);
                        i.putExtra("model", model);
                        startActivity(i);

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
}
