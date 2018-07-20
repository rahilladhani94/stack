package com.hourstack.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.hourstack.Activities.ActivityCreateNewLabel;
import com.hourstack.Activities.ActivityCreateNewProjects;
import com.hourstack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 16-12-2016.
 */
public class AdpterChooseColor extends BaseAdapter {


    Context context;

    LayoutInflater inflater;
    List<String> data = new ArrayList<>();

    boolean checkState[];
    String activity;


    public AdpterChooseColor(Context context, List<String> listview, String activity) {
        // TODO Auto-generated constructor stub


        this.context = context;
        data = listview;
        this.activity = activity;
        checkState = new boolean[listview.size()];

    }

    @Override
    public int getCount() {
        return data.size();
    }


    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    public class Holder {


        TextView  txtround;
        ImageView iv_main;

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder = new Holder();
        View rowView;

        if (convertView == null) {

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout_color, null);


        }


        holder.iv_main = (ImageView) convertView.findViewById(R.id.iv_main);
        holder.txtround = (TextView) convertView.findViewById(R.id.txtround);
        int color = 0;

        if (position == 0) {

            color = Color.parseColor("#74b427");
            holder.iv_main .setColorFilter(color);
            
        } else if (position == 1) {


            color = Color.parseColor("#ffae00");
            holder.iv_main .setColorFilter(color);
        } else if (position == 2) {

            color = Color.parseColor("#c12602");
            holder.iv_main .setColorFilter(color);
        } else if (position == 3) {

            color = Color.parseColor("#ce2a83");
            holder.iv_main .setColorFilter(color);
        } else if (position == 4) {

            color = Color.parseColor("#7f00d0");
            holder.iv_main .setColorFilter(color);
        } else if (position == 5) {

            color = Color.parseColor("#3424b1");
            holder.iv_main .setColorFilter(color);
        } else if (position == 6) {


            color = Color.parseColor("#0074c5");
            holder.iv_main .setColorFilter(color);
        } else if (position == 7) {


            color = Color.parseColor("#28c8c2");
            holder.iv_main .setColorFilter(color);
        } else if (position == 8) {

            color = Color.parseColor("#ffea00");
            holder.iv_main .setColorFilter(color);
        } else if (position == 9) {

            color = Color.parseColor("#bad138");
            holder.iv_main .setColorFilter(color);
        } else if (position == 10) {

            color = Color.parseColor("#b7b7b7");
            holder.iv_main .setColorFilter(color);
        } else if (position == 11) {


            color = Color.parseColor("#434343");
            holder.iv_main .setColorFilter(color);
        }


        if (checkState[position]) {
            holder.txtround.setVisibility(View.VISIBLE);

        } else {
            holder.txtround.setVisibility(View.GONE);

        }


        holder.iv_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("posit", "" + position);


                for (int i = 0; i < checkState.length; i++) {
                    if (i == position) {

                        checkState[i] = true;
                        holder.txtround.setVisibility(View.VISIBLE);

                        if (activity.equalsIgnoreCase("project")) {
                            ActivityCreateNewProjects.selected_color = data.get(i);
                        } else if (activity.equalsIgnoreCase("label")) {
                            ActivityCreateNewLabel.selected_color = data.get(i);
                        }
                    } else {

                        checkState[i] = false;
                        holder.txtround.setVisibility(View.GONE);

                    }
                }
                notifyDataSetChanged();
            }
        });


        return convertView;
    }

}
