package com.servicehub.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.servicehub.Custom_Compo.CustomTextview;
import com.servicehub.Model.Model_service_list;
import com.servicehub.R;

import java.util.ArrayList;

/**
 * Created by Nirav on 10/17/2016.
 */

public class Category_list_new_Adapter extends BaseAdapter {
    Context context;
    ArrayList<Model_service_list> Greet_list = new ArrayList<>();
    ViewHolder holder;

    public Category_list_new_Adapter(Context context, ArrayList<Model_service_list> Em_list) {
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
//
            holder.v_circle_1 = (RelativeLayout) convertView.findViewById(R.id.v_circle_1);
            holder.v_circle_2 = (RelativeLayout) convertView.findViewById(R.id.v_circle_2);
          //  holder.r_up = (RelativeLayout) convertView.findViewById(R.id.r_up);
           // holder.r_down = (RelativeLayout) convertView.findViewById(R.id.r_down);

            holder.img = (ImageView) convertView.findViewById(R.id.img);

            holder.img_1 = (ImageView) convertView.findViewById(R.id.img_1);
            holder.img_2 = (ImageView) convertView.findViewById(R.id.img_2);

     //       holder.tv1 = (CustomTextview) convertView.findViewById(R.id.tv1);
     //       holder.tv2 = (CustomTextview) convertView.findViewById(R.id.tv2);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //Glide.with(context).load("" + model.getService_image()).into(holder.img_1);
        //Glide.with(context).load("" + model.getService_image()).into(holder.img_2);
        if (position == 0) {
            holder.r_up.setVisibility(View.GONE);
            holder.r_down.setVisibility(View.VISIBLE);
           // Glide.with(context).load("" + model.getService_image()).into(holder.img_2);
            holder.img_2.setImageResource(model.getImg());
            holder.tv2.setText(model.getService_name());
            if (model.getStatus() == 1) {
                holder.v_circle_1.setVisibility(View.VISIBLE);
            } else {
                holder.v_circle_1.setVisibility(View.GONE);
            }
            holder.v_circle_2.setVisibility(View.GONE);
        } else if (position % 2 != 0) {
            holder.r_up.setVisibility(View.VISIBLE);
            holder.r_down.setVisibility(View.GONE);
            //Glide.with(context).load("" + model.getService_image()).into(holder.img_1);
            holder.img_1.setImageResource(model.getImg());
            holder.tv1.setText(model.getService_name());

            if (model.getStatus() == 1) {
                holder.v_circle_2.setVisibility(View.VISIBLE);
            } else {
                holder.v_circle_2.setVisibility(View.GONE);
            }
            holder.v_circle_1.setVisibility(View.GONE);

        } else if (position % 2 == 0) {
            holder.r_up.setVisibility(View.GONE);
            holder.r_down.setVisibility(View.VISIBLE);
            //Glide.with(context).load("" + model.getService_image()).into(holder.img_2);
            holder.img_2.setImageResource(model.getImg());
            holder.tv2.setText(model.getService_name());

            if (model.getStatus() == 1) {
                holder.v_circle_1.setVisibility(View.VISIBLE);
            } else {
                holder.v_circle_1.setVisibility(View.GONE);
            }
            holder.v_circle_2.setVisibility(View.GONE);
        }

//        holder.v_circle_2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i1 = new Intent(context, Add_Address.class);
//                // context.startActivity(i1);
//            }
//        });
//        holder.v_circle_1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i1 = new Intent(context, Add_Address.class);
//                // context.startActivity(i1);
//            }
//        });


        if (position == 0) {
            holder.img.setBackgroundResource(R.drawable.one);
        } else if (position == 1) {
            holder.img.setBackgroundResource(R.drawable.two);
        } else if (position == 2) {
            holder.img.setBackgroundResource(R.drawable.three);
        } else if (position == 3) {
            holder.img.setBackgroundResource(R.drawable.four);
        } else if (position == 4) {
            holder.img.setBackgroundResource(R.drawable.five);
        } else if (position == 5) {
            holder.img.setBackgroundResource(R.drawable.six);
        }


        return convertView;

    }

    class ViewHolder {
        ImageView img, img1;
        RelativeLayout v_circle_1, v_circle_2;
        RelativeLayout r_up, r_down;
        LinearLayout ll_1, ll_2;
        ImageView img_1, img_2;
        CustomTextview tv1, tv2;
    }
}
