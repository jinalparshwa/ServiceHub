package com.servicehub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.servicehub.Activity.Add_Address;
import com.servicehub.Activity.My_order;
import com.servicehub.Activity.Review_Activity;
import com.servicehub.Model.Model_Order;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Custom_Compo.CustomTextview;

import java.util.ArrayList;

/**
 * Created by Nirav on 10/17/2016.
 */

public class Order_list_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Model_Order> Greet_list = new ArrayList<>();
    ViewHolder holder;

    public Order_list_Adapter(Context context, ArrayList<Model_Order> Em_list) {
        super();
        this.context = context;
        this.Greet_list = Em_list;
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

        final Model_Order model = Greet_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_order, null);
            holder.rr_order = (RelativeLayout) convertView.findViewById(R.id.rr_order);
            holder.txt_amount = (CustomTextview_Bold) convertView.findViewById(R.id.txt_amount);
            holder.txt_payment = (CustomTextview_Bold) convertView.findViewById(R.id.txt_payment);
            holder.txt_order_name = (CustomTextview_Bold) convertView.findViewById(R.id.txt_order_name);
            holder.txt_start_time = (CustomTextview) convertView.findViewById(R.id.txt_start_time);
            holder.txt_end_time = (CustomTextview) convertView.findViewById(R.id.txt_end_time);
            holder.txt_status = (CustomTextview) convertView.findViewById(R.id.txt_status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_amount.setText(model.getAmount() + "/-");
        holder.txt_payment.setText(model.getPayment());
        holder.txt_order_name.setText(model.getInquiry_no());
        holder.txt_start_time.setText(model.getS_time());
        holder.txt_end_time.setText(model.getE_time());
        holder.txt_status.setText(model.getStatus());

        holder.rr_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, Review_Activity.class);
                i.putExtra("empid",model.getEmpid());
                i.putExtra("inquiry_id",model.getInquiry_id());
                context.startActivity(i);
            }
        });

        return convertView;

    }

    private class ViewHolder {

        RelativeLayout rr_order;
        CustomTextview_Bold txt_amount;
        CustomTextview_Bold txt_payment,txt_order_name;
        CustomTextview txt_start_time;
        CustomTextview txt_end_time;
        CustomTextview txt_status;
    }
}
