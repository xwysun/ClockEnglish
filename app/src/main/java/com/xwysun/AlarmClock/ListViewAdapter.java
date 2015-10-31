package com.xwysun.AlarmClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xwysun.R;
import com.xwysun.wordmanage.ClockManage;
import com.xwysun.wordmanage.model.clock.Clock;
import com.xwysun.wordmanage.model.clock.Repeat;

import java.util.Arrays;
import java.util.List;


/**
 * Created by tornado on 2015/10/5.
 */
public class ListViewAdapter extends BaseAdapter {
    boolean flag=false;
    private LayoutInflater inflater;
    private Handler handler;
    private Activity activity;
    public static Clock clock = null;
    private static ClockManage clockManage;
    private static Context context;
    Message msg2 = new Message();
    public static int TYPE  = 1;
    public ListViewAdapter(Context context,Handler handler,Activity activity) {
        this.inflater = LayoutInflater.from(context);
        this.clockManage = new ClockManage(context);
        this.context = context;
        this.handler = handler;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return clockManage.getClocks().size();
    }
    public static void refleshData(){
        clockManage = new ClockManage(context);
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }



    @Override
    public View getView(final int position, View convertView, final ViewGroup viewGroup) {

        ViewHolder holder = null;

        OnCheck onCheck= null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.alarm_item, null);
                holder.alarm_item = (RelativeLayout) convertView.findViewById(R.id.alarm_item);
                holder.time = (TextView) convertView.findViewById(R.id.time);

                holder.switchButton = (CheckSwitchButton) convertView.findViewById(R.id.switchbtn);
                holder.deleteCB = (CheckBox) convertView.findViewById(R.id.delete);
                if (TYPE == 1) {//正常界面
                    holder.deleteCB.setVisibility(View.GONE);
                    holder.switchButton.setVisibility(View.VISIBLE);
                } else if (TYPE == 0) {//删除界面
                    holder.deleteCB.setVisibility(View.VISIBLE);
                    holder.switchButton.setVisibility(View.GONE);
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
                if (TYPE == 1) {//正常界面
                    holder.deleteCB.setVisibility(View.GONE);
                    holder.switchButton.setVisibility(View.VISIBLE);
                } else if (TYPE == 0) {//删除界面
                    holder.deleteCB.setVisibility(View.VISIBLE);
                    holder.switchButton.setVisibility(View.GONE);
                }
            }
            clock=clockManage.getClocks().get(position);
           onCheck = new OnCheck(clock,position);
            holder.time.setText(clock.getTime().toString());
            flag = false;
            if(clock.getVibration()==1||clock.getVibration()==2) {

                holder.switchButton.setChecked(true);
            }
            else  holder.switchButton.setChecked(false);
            flag = true;
            holder.alarm_item.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    setItemViewType(0);
                    MainActivity.deleteclockList.clear();
                    return false;
                }
            });
        holder.alarm_item.setOnClickListener(new ListViewItemOnClick(context,clock,activity,position));


            holder.switchButton.setOnCheckedChangeListener(onCheck);//是否开启闹钟

            holder.deleteCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //
                    if (isChecked) {
                        MainActivity.deleteclockList.add(clock);
                    } else {
                        MainActivity.deleteclockList.remove(clock);
                    }
                }
            });

        return convertView;
    }
    public void  setItemViewType(int type){
        TYPE = type;
        this.notifyDataSetChanged();
        Message msg = new Message();
        msg.what = 1;
        handler.sendMessage(msg);
    }
    public  class ViewHolder{
        public RelativeLayout alarm_item;
        public TextView time;
        public CheckSwitchButton switchButton;
        public CheckBox deleteCB;
    }
    class  OnCheck implements CompoundButton.OnCheckedChangeListener{
        Clock clockx;
        int position;
        public OnCheck(Clock clock1,int position){
            super();
            clockx = clock1;
            this.position = position;
        }
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            if(flag) {
                Intent intent = new Intent(context, AlarmReceiver.class);


                if (isChecked) {
                    //改变数据库
                    if (clockx.getVibration() == 10) {
                        clockx.setVibration(1);
                        clockManage.setClock(clockx);
                    } else if (clockx.getVibration() == 20) {
                        clockx.setVibration(2);
                        clockManage.setClock(clockx);
                    }
                    //设置系统闹钟
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("wordnumber", clockx.getWordNumber());
                    if(clockx.getVibration()==1)
                        bundle.putBoolean("vibrate", true);
                    else bundle.putBoolean("vibrate", false);
                    bundle.putString("remark",clockx.getRemark());
                    bundle.putSerializable("ring", clockx.getRing());
                    Log.d("clockx", clockx.toString() + "---" + clockx.getRing().toString());
                    intent.putExtras(bundle);
                    PendingIntent pi = PendingIntent.getBroadcast(context,position,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    if (clockx.getRepeat() == Repeat.ONLY_ONE){
                        alarmManager.set(AlarmManager.RTC_WAKEUP, TimeUtils.changeTime(clockx.getTime()), pi);        //设置闹钟
                    }else if (clockx.getRepeat() == Repeat.EVERY_DAY) {
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,TimeUtils.changeTime(clockx.getTime()), 10 * 1000, pi);
                    }else if(clockx.getRepeat() == Repeat.MON2FIR){
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, TimeUtils.changeTime(clockx.getTime()), 10 * 1000, pi);
                    }

                } else {
                    Log.d("vibrate", isChecked + "123 ");
                    if (clockx.getVibration() == 1) {//振动
                        clockx.setVibration(10);
                        clockManage.setClock(clockx);
                    } else if (clockx.getVibration() == 2) {//不震动
                        clockx.setVibration(20);
                        clockManage.setClock(clockx);
                    }
                    PendingIntent pi = PendingIntent.getBroadcast(context,position,intent,PendingIntent.FLAG_UPDATE_CURRENT);
                    AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

                    //取消闹钟
                    alarmManager.cancel(pi);
                }
            }
        }
    }
}

