package com.servicehub.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Employee.NewInquiryActivity;
import com.servicehub.Model.Model_New_Inquiry;
import com.servicehub.R;

import java.util.ArrayList;


/**
 * Created by admin on 1/27/2017.
 */

public class New_inquiry_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Model_New_Inquiry> Greet_list = new ArrayList<>();
    ViewHolder holder;
    NewInquiryActivity newInquiryActivityClass;

    public New_inquiry_Adapter(Context context, ArrayList<Model_New_Inquiry> Em_list, NewInquiryActivity newInquiryActivityClass) {
        super();
        this.context = context;
        this.Greet_list = Em_list;
        this.newInquiryActivityClass = newInquiryActivityClass;
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
            convertView = inflater.inflate(R.layout.raw_new_inquiry, null);
            holder.rr_order = (RelativeLayout) convertView.findViewById(R.id.rr_order);
            holder.txt_amount = (CustomTextview_Bold) convertView.findViewById(R.id.txt_amount);
            holder.txt_order_name = (CustomTextview_Bold) convertView.findViewById(R.id.txt_order_name);
            holder.txt_start_time = (CustomTextview) convertView.findViewById(R.id.txt_start_time);
            holder.txt_end_time = (CustomTextview) convertView.findViewById(R.id.txt_end_time);
            holder.btn_accept = (Button) convertView.findViewById(R.id.btn_accept);
            holder.btn_reject = (Button) convertView.findViewById(R.id.btn_reject);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Log.e("Inquiry_id_accept",model.getInquiry_id());
        holder.txt_amount.setText(model.getAmount() + "/-");
        holder.txt_order_name.setText(model.getInquiry_no());
        holder.txt_start_time.setText(model.getS_time());
        holder.txt_end_time.setText(model.getE_time());
        holder.btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Inquiry_id_accept",model.getInquiry_id());
                newInquiryActivityClass.Accept_status(2,model.getInquiry_id());
            }
        });

        holder.btn_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Inquiry_id_reject",model.getInquiry_id());
               newInquiryActivityClass.Accept_status(4, model.getInquiry_id());
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
        Button btn_accept;
        Button btn_reject;
    }



}