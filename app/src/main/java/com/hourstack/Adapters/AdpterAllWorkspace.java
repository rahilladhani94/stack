package com.hourstack.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hourstack.Model.LabelsListResponse;
import com.hourstack.Model.WorkspaceListResponse;
import com.hourstack.R;
import com.hourstack.Utils.SessionManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abc on 16-12-2016.
 */
public class AdpterAllWorkspace extends BaseAdapter {


    Context context;

    LayoutInflater inflater;
    List<WorkspaceListResponse> data = new ArrayList<>();

    public AdpterAllWorkspace(Context context, List<WorkspaceListResponse> listview) {
        // TODO Auto-generated constructor stub

        this.context = context;
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


        TextView wsname;
        ImageView iv_current;



    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        final Holder holder=new Holder();
        View rowView;

        if (convertView == null){

            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout_workspace, null);


        }


        SessionManager sessionManager;
        sessionManager = new SessionManager(context);
        holder.wsname = (TextView)convertView.findViewById(R.id.wsname);
        holder.iv_current = (ImageView)convertView.findViewById(R.id.iv_current);
        Log.e("curr",""+sessionManager.getKeyCurrentWorkspace());

        if(sessionManager.getKeyCurrentWorkspace().equals(""+data.get(position).getId())){

            holder.iv_current.setVisibility(View.VISIBLE);
        }
        else {

            holder.iv_current.setVisibility(View.GONE);
        }

        holder.wsname.setText(data.get(position).getName());


        return convertView;
    }

}
