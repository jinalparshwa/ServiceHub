package com.servicehub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Custom_Compo.CustomTextview_Bold;
import com.servicehub.Employee.MyEarningActivity;
import com.servicehub.Model.Model_New_Inquiry;
import com.servicehub.R;

import java.util.ArrayList;

/**
 * Created by admin on 1/30/2017.
 */

public class My_earning_Adapter extends BaseAdapter {

    Context context;
    ArrayList<Model_New_Inquiry> Greet_list = new ArrayList<>();
    ViewHolder holder;
    String srt;
    MyEarningActivity myEarning;

    public My_earning_Adapter(Context context, ArrayList<Model_New_Inquiry> Em_list, MyEarningActivity myEarning) {
        super();
        this.context = context;
        this.Greet_list = Em_list;
        this.myEarning = myEarning;
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
            convertView = inflater.inflate(R.layout.raw_my_earning, null);
            holder.txt_amount = (CustomTextview_Bold) convertView.findViewById(R.id.txt_amount);
            holder.txt_order_name = (CustomTextview_Bold) convertView.findViewById(R.id.txt_order_name);
            holder.txt_start_time = (CustomTextview) convertView.findViewById(R.id.txt_start_time);
            holder.txt_end_time = (CustomTextview) convertView.findViewById(R.id.txt_end_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.txt_amount.setText(model.getAmount() + "/-");
        holder.txt_order_name.setText(model.getInquiry_no());
        holder.txt_start_time.setText(model.getS_time());
        holder.txt_end_time.setText(model.getE_time());

        return convertView;

    }

    private class ViewHolder {

        CustomTextview_Bold txt_amount;
        CustomTextview_Bold txt_order_name;
        CustomTextview txt_start_time;
        CustomTextview txt_end_time;
    }
}
