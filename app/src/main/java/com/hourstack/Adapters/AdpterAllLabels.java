package com.hourstack.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hourstack.Activities.ActivityEditEntry;
import com.hourstack.Model.LabelsListResponse;
import com.hourstack.Model.ProjectListResponse;
import com.hourstack.R;
import com.hourstack.Utils.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 16-12-2016.
 */
public class AdpterAllLabels extends BaseAdapter {


    private Context context;

    LayoutInflater inflater;
    private List<LabelsListResponse> data = new ArrayList<>();
    private String edit="";

    public AdpterAllLabels(Context context, List<LabelsListResponse> listview,String edit) {
        // TODO Auto-generated constructor stub

        this.context = context;
        this.edit =edit;
        data = listview;


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


    public class Holder
    {


        TextView projectname;
        ImageView iv_folder,iv_current;



    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder=new Holder();
        View rowView;

        if (convertView == null){

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout_labels, null);


        }


        holder.projectname = (TextView)convertView.findViewById(R.id.labelnmae);
        holder.iv_folder = (ImageView) convertView.findViewById(R.id.iv_label);
        holder.iv_current = (ImageView)convertView.findViewById(R.id.iv_current);
        holder.projectname.setText(data.get(position).getName());
        if(data.get(position).getColor() !=null){

            try {
                String f = "#"+data.get(position).getColor();
                holder.iv_folder.setColorFilter(Color.parseColor(f));
            }
            catch (Exception e){

            }

        }

        if(edit.equalsIgnoreCase("yes")){
            if(ActivityEditEntry.label_selectedid !=null) {
                if (ActivityEditEntry.label_selectedid.equals("" + data.get(position).getId())) {

                    holder.iv_current.setVisibility(View.VISIBLE);
                } else {

                    holder.iv_current.setVisibility(View.GONE);
                }
            }
        }
        else{
            if(Constants.label_selectedid !=null) {
                if (Constants.label_selectedid.equals("" + data.get(position).getId())) {

                    holder.iv_current.setVisibility(View.VISIBLE);
                } else {

                    holder.iv_current.setVisibility(View.GONE);
                }
            }

        }


        return convertView;
    }

}
