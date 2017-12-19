package com.servicehub.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.servicehub.Activity.Add_Address;
import com.servicehub.Activity.MainActivity;
import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nirav on 10/17/2016.
 */

public class Category_list_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Model_service_list> Greet_list = new ArrayList<>();
    ViewHolder holder;

    public Category_list_Adapter(Context context, ArrayList<Model_service_list> Em_list) {
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

        Model_service_list model = Greet_list.get(position);
        LayoutInflater inflater = LayoutInflater.from(context);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.raw_cat_grid, null);

            holder.service_img = (ImageView) convertView.findViewById(R.id.service_img);
            holder.arrow = (ImageView) convertView.findViewById(R.id.arrow);
            holder.service_name=(TextView)convertView.findViewById(R.id.service_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        Log.e("ArrayList_size", ":" + Greet_list.size());
       if(model.getStatus()==0) {
           holder.arrow.setVisibility(View.INVISIBLE);
           Glide.with(context).load(model.getService_image()).into(holder.service_img);
       }
        else {
           holder.arrow.setVisibility(View.VISIBLE);
           Glide.with(context).load(model.getSelected_image()).into(holder.service_img);

       }
        holder.service_name.setText(model.getService_name());
        return convertView;

    }

    class ViewHolder {
        ImageView service_img;
        ImageView arrow;
        TextView service_name;
    }
}
