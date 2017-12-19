package com.servicehub.Json_Model;

import android.util.Log;

import com.servicehub.Model.Model_Book;
import com.servicehub.Model.Model_User;
import com.servicehub.Model.Model_feedback;
import com.servicehub.Model.Model_profile;
import com.servicehub.Shared.Pref_Master;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2017.
 */

public class JSON {

    public static String add_1(ArrayList<Model_User> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("registeruser")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_User model = array_user.get(i);
                    jobj_main.put("username", model.getUsername());
                    jobj_main.put("usertypeid", "2");
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("mobile", model.getMobile());
                    jobj_main.put("password", model.getPassword());
                    jobj_main.put("deviceid", model.getDeviceid());
                    jobj_main.put("devicetoken", model.getDevicetoken());
                    jobj_main.put("devicetype", "0");

                }
            } else if (array_nm.equals("loginuser")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_User model = array_user.get(i);
                    jobj_main.put("mobile", model.getMobile());
                    jobj_main.put("password", model.getPassword());
                    jobj_main.put("deviceid", model.getDeviceid());
                    jobj_main.put("devicetoken", model.getDevicetoken());
                    jobj_main.put("devicetype", "0");

                }
            } else if (array_nm.equals("changepassword")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_User model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("oldpass", model.getOldpwd());
                    jobj_main.put("newpass", model.getNewpwd());

                }
            }

            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_2(ArrayList<Model_Book> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("bookservice")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_Book model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("city", model.getCity());
                    jobj_main.put("service", model.getService());
                    jobj_main.put("area", model.getArea());
                    jobj_main.put("landmark", model.getLandmark());
                    jobj_main.put("address", model.getAddress());
                    jobj_main.put("hours", model.getHours());
                    jobj_main.put("cost", model.getCost());
                    jobj_main.put("stime", model.getStime());
                    jobj_main.put("etime", model.getEtime());
                    jobj_main.put("date", model.getDate());
                    jobj_main.put("rate", model.getRate());


                }
            }
            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_3(ArrayList<Model_profile> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("user")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_profile model = array_user.get(i);
                    jobj_main.put("userid", pref.getStr_Userid());
                    jobj_main.put("name", model.getName());
                    jobj_main.put("email", model.getEmail());
                    jobj_main.put("mobile", model.getMobile());


                }
            }
            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

    public static String add_4(ArrayList<Model_feedback> array_user, Pref_Master pref, String array_nm) {
        JSONObject jobj_data = new JSONObject();
        JSONObject jobj_main = new JSONObject();

        try {
            if (array_nm.equals("feedback")) {
                for (int i = 0; i < array_user.size(); i++) {
                    Model_feedback model = array_user.get(i);
                    jobj_main.put("inquiryid", model.getInq_id());
                    jobj_main.put("rate", model.getRatings());
                    jobj_main.put("comment", model.getComments());


                }
            }
            JSONArray jarray_main = new JSONArray();
            jarray_main.put(jobj_main);

            jobj_data.put(array_nm, jarray_main);

        } catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

        Log.i("Json request", jobj_data.toString().

                replaceAll("\\\\", "")

        );

        return jobj_data.toString().

                replaceAll("\\\\", "");
    }

}
