package com.servicehub.Shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by admin on 10/1/2016.
 */
public class Pref_Master {

    // code for construstor of : class - Pref_Master
    public Pref_Master(Context context) {
        this.context = context;
        pref = PreferenceManager.getDefaultSharedPreferences(context);
    }

    // code for define variable of : shared preference
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    // code for define variable of : string

    private String str_login_flag = "loginflag";
    private String default_login_flag = "0";

    private String str_emp_login = "login_emp";
    private String default_emp_login = "0";

    private String str_Userid = "userid";
    private String default_Userid = "0";

    private String str_Email = "email";
    private String default_Email = "0";


    private String str_Mobile = "mobile";
    private String default_Mobile = "0";

    private String str_Pwd = "password";
    private String default_pwd = "0";

    private String str_otp = "otpcode";
    private String default_otp = "0";

    private String str_fname = "fname";
    private String default_fname = " ";

    private String str_lname = "lname";
    private String default_lname = " ";


    private String str_profile = "profile";
    private String default_profile = " ";

    private String str_time = "time";
    private int default_time = 1;

    private String str_inquiry = "inquiry";
    private String default_inquiry = "10101";



    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************
    // code for define and initialize :  get methods
    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************

    public String getStr_login_flag() {
        return pref.getString(str_login_flag, default_login_flag);
    }

    public String getStr_login_email() {
        return pref.getString(str_Email, default_Email);
    }

    public String getStr_login_mobile() {
        return pref.getString(str_Mobile, default_Mobile);
    }

    public String getStr_Pwd() {
        return pref.getString(str_Pwd, default_pwd);
    }

    public String getStr_Userid() {
        return pref.getString(str_Userid, default_Userid);
    }

    public String getStr_otp() {
        return pref.getString(str_otp, default_otp);
    }

    public String getStr_fname() {
        return pref.getString(str_fname, default_fname);
    }

    public String getStr_lname() {
        return pref.getString(str_lname, default_lname);
    }

    public String getStr_profile() {
        return pref.getString(str_profile, default_profile);
    }

    public String getStr_emp_login() {
        return pref.getString(str_emp_login, default_emp_login);
    }

    public int getTime() {
        return pref.getInt(str_time,default_time);
    }

    public String getStr_inquiry() {
        return pref.getString(str_inquiry, default_inquiry);
    }


    // *******************************************************************************************
    // *******************************************************************************************
    // ********************************************************************************************
    // code for define and initialize :  set methods

    public void setLogin_Flag(String name) {
        editor = pref.edit();
        editor.putString(str_login_flag, name);
        editor.apply();
    }

    public void setUserid(String name) {
        editor = pref.edit();
        editor.putString(str_Userid, name);
        editor.apply();
    }

    public void setEmail(String name) {
        editor = pref.edit();
        editor.putString(str_Email, name);
        editor.apply();
    }

    public void setMobile(String name) {
        editor = pref.edit();
        editor.putString(str_Mobile, name);
        editor.apply();
    }

    public void setPassword(String name) {
        editor = pref.edit();
        editor.putString(str_Pwd, name);
        editor.apply();
    }

    public void setotp(String name) {
        editor = pref.edit();
        editor.putString(str_otp, name);
        editor.apply();
    }

    public void setfname(String name) {
        editor = pref.edit();
        editor.putString(str_fname, name);
        editor.apply();
    }

    public void setlname(String name) {
        editor = pref.edit();
        editor.putString(str_lname, name);
        editor.apply();
    }

    public void setprofile(String name) {
        editor = pref.edit();
        editor.putString(str_profile, name);
        editor.apply();
    }

    public void setStr_emp_login(String name) {
        editor = pref.edit();
        editor.putString(str_emp_login, name);
        editor.apply();
    }

    public void setTime(int time) {
        editor = pref.edit();
        editor.putInt(str_time, time);
        editor.apply();
    }

    public void setStr_inquiry(String inquiry) {
        editor = pref.edit();
        editor.putString(str_inquiry, inquiry);
        editor.apply();
    }




    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************
    // code to clear all preferences
    // *******************************************************************************************
    // *******************************************************************************************
    // *******************************************************************************************

    public void clear_pref() {
        pref.edit().clear().apply();
    }
}

