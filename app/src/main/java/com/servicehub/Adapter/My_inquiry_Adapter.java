package com.servicehub.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Employee.MyInquiryActivity;
import com.servicehub.Employee.Service_time;
import com.servicehub.Model.Model_New_Inquiry;
import com.servicehub.Model.Model_time;
import com.servicehub.R;
import com.servicehub.Shared.Pref_Master;

import java.util.ArrayList;

/**
 * Created by admin on 1/27/2017.
 */

public class My_inquiry_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Model_New_Inquiry> Greet_list = new ArrayList<>();
    ViewHolder holder;
    String srt;
    MyInquiryActivity myInquiryActivity;
    Pref_Master pref;

    public My_inquiry_Adapter(Context context, ArrayList<Model_New_Inquiry> Em_list, MyInquiryActivity myInquiryActivity, Pref_Master pref) {
        super();
        this.context = context;
        this.Greet_list = Em_list;
        this.myInquiryActivity = myInquiryActivity;
        this.pref = pref;
    }


    @Override
    public int getCount() {
        return Greet_list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        holder = new ViewHolder();

        final Model_New_Inquiry model = Greet_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_my_inquiry, null);
            holder.rr_order = (RelativeLayout) convertView.findViewById(R.id.rr_order);
            holder.txt_amount = (CustomTextview_Bold) convertView.findViewById(R.id.txt_amount);
            holder.txt_order_name = (CustomTextview_Bold) convertView.findViewById(R.id.txt_order_name);
            holder.txt_start_time = (CustomTextview) convertView.findViewById(R.id.txt_start_time);
            holder.txt_end_time = (CustomTextview) convertView.findViewById(R.id.txt_end_time);
            holder.rr_my_inquiry = (RelativeLayout) convertView.findViewById(R.id.rr_my_inquiry);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_amount.setText(model.getAmount() + "/-");
        holder.txt_order_name.setText(model.getInquiry_no());
        holder.txt_start_time.setText(model.getS_time());
        holder.txt_end_time.setText(model.getE_time());


        holder.rr_my_inquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (model.getCode_status().equals("1") && pref.getStr_inquiry().equals(model.getInquiry_id())) {
                    Model_time model1 = new Model_time();
                    model1.setInq_id(model.getInquiry_id());
                    model1.setStart_time(model.getS_time());
                    model1.setEnd_time(model.getE_time());
                    model1.setHours(model.getHours());

                    Intent i = new Intent(context, Service_time.class);
                    i.putExtra("start_time", model1);
                    context.startActivity(i);
                } else if (pref.getStr_inquiry().equals("10101")) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("Enter Your Secret Code");

                    final EditText input = new EditText(context);
                    alert.setView(input);

                    alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            srt = input.getEditableText().toString();
                            myInquiryActivity.Check_code(model.getInquiry_id(), srt);
                            pref.setStr_inquiry(model.getInquiry_id());
                        }
                    });
                    alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {

                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                } else {
                    Toast.makeText(context,"Your Service is already Running",Toast.LENGTH_SHORT).show();
                }
            }
        });


        return convertView;

    }

    private class ViewHolder {
        RelativeLayout rr_order;
        CustomTextview_Bold txt_amount;
        CustomTextview_Bold txt_order_name;
        CustomTextview txt_start_time;
        CustomTextview txt_end_time;
        RelativeLayout rr_my_inquiry;
    }

}
