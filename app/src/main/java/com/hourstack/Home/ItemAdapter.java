/*
 * Copyright 2014 Magnus Woxblom
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hourstack.Home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hourstack.Activities.ActivityCreateEntry;
import com.hourstack.Activities.ActivityEditEntry;
import com.hourstack.Activities.ActivitySplashScreen;
import com.hourstack.R;
import com.hourstack.Utils.Constants;
import com.hourstack.Utils.SessionManager;
import com.hourstack.Utils.UIUtil;
import com.hourstack.service.service;
import com.woxthebox.draglistview.DragItemAdapter;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

class ItemAdapter extends DragItemAdapter<Pair<Long, String>, ItemAdapter.ViewHolder> {

    private int mLayoutId;
    private int mGrabHandleId;
    private boolean mDragOnLongPress;


    Context context;
    int mColumns;
    public static CountDownTimer timer;
    SessionManager sessionManager;

    ItemAdapter(ArrayList<Pair<Long, String>> list, int layoutId, int grabHandleId, boolean dragOnLongPress, Context context, int mColumns) {
        mLayoutId = layoutId;
        mGrabHandleId = grabHandleId;
        mDragOnLongPress = dragOnLongPress;

        this.context = context;
        this.mColumns = mColumns;
        setHasStableIds(true);
        setItemList(list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        String text = mItemList.get(position).second;
        String s = text;


        String[] parts = s.split("@@"); // escape .
        String eventname = parts[0];
        String eventdate = parts[1];
        String eventid = parts[2];
        String allocated_seconds = parts[3];
        String allocated_hours = parts[4];
        String actual_seconds = parts[5];
        String actual_hours = parts[6];
        String complete = parts[7];
        String project_id = parts[8];
        String label_id = parts[9];
        String order = parts[10];
        String notes = parts[11];
        String timing = parts[12];
        String date_last_timed = parts[13];
        String task_source = parts[14];
        String user_id = parts[15];
        String project_name = parts[16];
        String project_color = parts[17];
        String project_archived = parts[18];
        String label_name = parts[19];
        String label_color = parts[20];
        String label_archived = parts[21];
        String user_name = parts[22];
        String team_name = parts[23];
        String updatedat = parts[24];
        holder.txt_all.setText(text);

        sessionManager = new SessionManager(context);
        if (!eventname.equalsIgnoreCase("eventname")) {
            holder.taskname.setText(eventname.trim());

        }
        if (!eventid.equalsIgnoreCase("eventid")) {
            holder.taskid.setText(eventid);

        }
        if (!project_name.equalsIgnoreCase("project_name")) {
            holder.txt_project_name.setText(project_name.trim());
        }

        if (!actual_seconds.equalsIgnoreCase("actual_seconds")) {
//            double int_actual_seconds = Double.parseDouble((actual_seconds));
//            double actual = int_actual_seconds/3600;
//
//
//            double d = actual;
//            DecimalFormat df = new DecimalFormat("#.00");
//            System.out.print(df.format(d));

            String actual="";
            actual = UIUtil.convertDateTime("ss", "HH:mm", actual_seconds);
            holder.txt_actual.setText("" + actual);
            if (!allocated_hours.equalsIgnoreCase("allocated_hours")) {
                double int_actual_seconds = Double.parseDouble((actual_hours));
                double int_allocated_seconds = Double.parseDouble(allocated_hours);

                Log.e("int_actual_seconds", "" + int_actual_seconds);
                Log.e("int_allocated_seconds", "" + int_allocated_seconds);

                if (int_actual_seconds > int_allocated_seconds) {
                    holder.txt_actual.setTypeface(null, Typeface.BOLD);
                    holder.txt_actual.setTextColor(context.getResources().getColor(R.color.red));
                    try {

                        holder.ivclock_actual.setColorFilter(Color.parseColor("#ff0000"));

                    } catch (Exception e) {

                    }
                } else {
                    holder.txt_actual.setTextColor(context.getResources().getColor(R.color.green));
                    holder.txt_actual.setTypeface(null, Typeface.BOLD);
                    try {

                        holder.ivclock_actual.setColorFilter(Color.parseColor("#168924"));
                    } catch (Exception e) {

                    }
                }

            }

        }
        if (!allocated_seconds.equalsIgnoreCase("allocated_seconds")) {


            String allocated="";
            allocated = UIUtil.convertDateTime("ss", "HH:mm", allocated_seconds);
            holder.txt_allocated.setText("" + allocated);

        }
        if (actual_hours.equalsIgnoreCase("actual_hours")) {

            holder.ivclock_actual.setVisibility(View.VISIBLE);
            holder.divider.setVisibility(View.GONE);
            holder.ivclock_allocated.setVisibility(View.GONE);
            holder.txt_actual.setTextColor(context.getResources().getColor(R.color.black));
        }
        if (!notes.equalsIgnoreCase("notes")) {
            holder.iv_note.setVisibility(View.VISIBLE);

        } else {
            holder.iv_note.setVisibility(View.GONE);
        }

        if (project_color != null && !project_color.equalsIgnoreCase("project_color")) {
            int color = 0;


            if (project_color != null) {

                try {
                    String f = "#" + project_color;
                    holder.iv_project.setColorFilter(Color.parseColor(f));

                } catch (Exception e) {

                }
            }


        }

        if (label_color != null && !label_color.equalsIgnoreCase("label_color")) {
            int color = 0;


            if (label_color != null) {

                try {
                    String f = "#" + label_color;

                    holder.view.setBackgroundColor(Color.parseColor(f));
                } catch (Exception e) {

                }
            }
        }


        String secondsapi = "";

        if (!actual_seconds.equalsIgnoreCase("actual_seconds")){

            secondsapi =actual_seconds;
        }
        else {
            secondsapi = "00";
        }
        if (!timing.equalsIgnoreCase("timing")) {


            if (timing.equalsIgnoreCase("1")) {
                if(sessionManager.getKeyRunningeventid().length()==0){

                    sessionManager.setKeyRunningeventid(eventid);
                    sessionManager.setKeyStartedseconds(updatedat);
                    sessionManager.setKeyCurrentseconds(secondsapi);

                    sessionManager.setKeyAllocatedseconds(allocated_seconds);
                    context.startService(new Intent(context, service.class));
                }
                else{
                    Log.e("old",""+sessionManager.getKeyRunningeventid());
                    Log.e("new",""+eventid);

                    Log.e("startred",""+sessionManager.getKeyStartedseconds());
                    Log.e("uopdated",""+updatedat);
                    Log.e("getKeyRunningeventid()",""+sessionManager.getKeyRunningeventid());
                    Log.e("eventid",""+eventid);


                    if(sessionManager.getKeyRunningeventid().equalsIgnoreCase(eventid))
                    {

                        if(sessionManager.getKeyStartedseconds().equalsIgnoreCase(updatedat)){



                            secondsapi = sessionManager.getKeyCurrentseconds();
                            sessionManager.setKeyRunningeventid(eventid);
                            sessionManager.setKeyStartedseconds(updatedat);
                            sessionManager.setKeyCurrentseconds(secondsapi);

                            sessionManager.setKeyAllocatedseconds(allocated_seconds);
                            context.startService(new Intent(context, service.class));

                        }
                        else{

                            sessionManager.setKeyCurrentseconds("");
                            sessionManager.setKeyRunningeventid("");
                            sessionManager.setKeyStartedseconds("");
                            sessionManager.setKeyAllocatedseconds("");

                            Intent myService = new Intent(context, service.class);
                            context.stopService(myService);

                            sessionManager.setKeyRunningeventid(eventid);
                            sessionManager.setKeyStartedseconds(updatedat);
                            sessionManager.setKeyCurrentseconds(secondsapi);



                           sessionManager.setKeyAllocatedseconds(allocated_seconds);
                            context.startService(new Intent(context, service.class));

                        }




                    }
                    else {

                        sessionManager.setKeyRunningeventid("");
                        sessionManager.setKeyStartedseconds("");
                        sessionManager.setKeyCurrentseconds("");


                        Intent myService = new Intent(context, service.class);
                        context.stopService(myService);

                        sessionManager.setKeyRunningeventid(eventid);
                        sessionManager.setKeyStartedseconds(updatedat);
                        sessionManager.setKeyCurrentseconds(secondsapi);
                        sessionManager.setKeyAllocatedseconds(allocated_seconds);

//
//                        sessionManager.setKeyAllocatedseconds(allocated_seconds);
//                        context.startService(new Intent(context, service.class));
                    }

                }




                context.startService(new Intent(context, service.class));
                Log.e("sp1",""+secondsapi);
                secondsapi = UIUtil.convertDateTime("ss", "HH:mm:ss", secondsapi);

                String split = secondsapi;
                Log.e("sp2",""+split);


                String[] partssplit = split.split(":"); // escape .

                try {
                    final String hours = partssplit[0];
                    final String minutes = partssplit[1];
                    final String seconds = partssplit[2];
                    if(hours !=null){
                        holder.txt_timerhours.setText(hours);
                    }
                    if(minutes !=null){
                        holder.txt_timerminutes.setText(minutes);
                    }
                    if(seconds !=null){
                        holder.txt_timerseconds.setText(seconds);
                    }


                } catch (Exception e) {

                }


                holder.ll_timer.setVisibility(View.VISIBLE);
                holder.rl_main.setAlpha((float) 0.2);
                final Boolean first =true;

                timer = new CountDownTimer(1000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished)
                    {

                    }

                    @Override
                    public void onFinish() {
                        try{
                            Animation startRotateAnimation;
                            startRotateAnimation = AnimationUtils.loadAnimation(context, R.anim.android_rotate_animation);
                            holder.iv_timer.startAnimation(startRotateAnimation);


                            int tep=0,tepmin=0,tephours=0;
                            tep = Integer.parseInt(holder.txt_timerseconds.getText().toString());
                            tepmin = Integer.parseInt(holder.txt_timerminutes.getText().toString());
                            tephours = Integer.parseInt(holder.txt_timerhours.getText().toString());
                            tep++;

                            if(tepmin<=9){
                                holder.txt_timerminutes.setText("0"+tepmin);
                            }
                            else{
                                holder.txt_timerminutes.setText(""+tepmin);
                            }

                            if(tep<=9){
                                holder.txt_timerseconds.setText("0"+tep);
                            }
                            else{
                                holder.txt_timerseconds.setText(""+tep);
                            }

                            if(tephours<=9){
                                holder.txt_timerhours.setText("0"+tephours);
                            }
                            else{
                                holder.txt_timerhours.setText(""+tephours);
                            }



                            if(tep>60){

                                tepmin++;
                                tep = tep-60;
                                tep++;

                                if(tepmin>=60){
                                    tepmin = tepmin -60;
                                    tephours++;

                                }
                                holder.txt_timerseconds.setText("00");
                                if(tepmin<=9){
                                    holder.txt_timerminutes.setText("0"+tepmin);
                                }
                                else{
                                    holder.txt_timerminutes.setText(""+tepmin);
                                }

                                if(tep<=9){
                                    holder.txt_timerseconds.setText("0"+tep);
                                }
                                else{
                                    holder.txt_timerseconds.setText(""+tep);
                                }

                                if(tephours<=9){
                                    holder.txt_timerhours.setText("0"+tephours);
                                }
                                else{
                                    holder.txt_timerhours.setText(""+tephours);
                                }


                            }


                            timer.start();
                        }catch(Exception e){
                            Log.e("Error", "Error: " + e.toString());
                        }
                    }
                }.start();

            } else {
//                sessionManager.setKeyRunningeventid("");
//                sessionManager.setKeyStartedseconds("");
//                sessionManager.setKeyCurrentseconds("");
//
//
//                Intent myService = new Intent(context, service.class);
//                context.stopService(myService);

                holder.ll_timer.setVisibility(View.GONE);
                holder.rl_main.setAlpha(8888);
            }

        }


        holder.itemView.setTag(eventid);
    }



    @Override
    public long getItemId(int position) {
        return mItemList.get(position).first;
    }


    class ViewHolder extends DragItemAdapter.ViewHolder {
        TextView taskid, taskname, txt_project_name, txt_actual, txt_allocated, divider, txt_all, txt_timerminutes, txt_timerseconds,txt_timerhours;
        ImageView iv_project, ivclock_actual, ivclock_allocated, iv_note,iv_timer;
        View view;
        LinearLayout ll_timer;
        RelativeLayout rl_main;

        ViewHolder(final View itemView) {
            super(itemView, mGrabHandleId, mDragOnLongPress);
            taskid = (TextView) itemView.findViewById(R.id.text);
            taskname = (TextView) itemView.findViewById(R.id.text2);
            txt_project_name = (TextView) itemView.findViewById(R.id.txt_project_name);
            iv_project = (ImageView) itemView.findViewById(R.id.iv_project);
            ivclock_actual = (ImageView) itemView.findViewById(R.id.ivclock_actual);
            ivclock_allocated = (ImageView) itemView.findViewById(R.id.ivclock_allocated);
            iv_note = (ImageView) itemView.findViewById(R.id.iv_note);
            txt_actual = (TextView) itemView.findViewById(R.id.txt_actual);
            txt_allocated = (TextView) itemView.findViewById(R.id.txt_allocated);
            txt_all = (TextView) itemView.findViewById(R.id.txt_all);
            divider = (TextView) itemView.findViewById(R.id.divider);
            txt_timerminutes = (TextView) itemView.findViewById(R.id.txt_timerminutes);
            txt_timerseconds = (TextView) itemView.findViewById(R.id.txt_timerseconds);
            txt_timerhours = (TextView) itemView.findViewById(R.id.txt_timerhours);
            iv_timer= (ImageView) itemView.findViewById(R.id.iv_timer);
            view = (View) itemView.findViewById(R.id.view);
            ll_timer = (LinearLayout) itemView.findViewById(R.id.ll_timer);
            rl_main = (RelativeLayout) itemView.findViewById(R.id.rl_main);
        }

        @Override
        public void onItemClicked(View view) {

            ActivityEditEntry.edit = "yes";
            Intent i = new Intent(context, ActivityEditEntry.class);
            i.putExtra("event", "" + txt_all.getText().toString());
            if(txt_timerhours.isShown()){
                i.putExtra("hours", "" + txt_timerhours.getText().toString());
                i.putExtra("mins", "" + txt_timerminutes.getText().toString());
                i.putExtra("seconds", "" + txt_timerseconds.getText().toString());

            }
            context.startActivity(i);
            // Toast.makeText(view.getContext(), "Item clicked "+taskname.getText().toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public boolean onItemLongClicked(View view) {
            //  Toast.makeText(view.getContext(), "Item long clicked", Toast.LENGTH_SHORT).show();
            return true;
        }
    }
}
