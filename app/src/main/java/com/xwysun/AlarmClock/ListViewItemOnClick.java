package com.xwysun.AlarmClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xwysun.wordmanage.model.clock.Clock;

import java.io.Serializable;

/**
 * Created by tornado on 2015/10/28.
 */
public class ListViewItemOnClick implements View.OnClickListener {
    private Context context;
    private Clock clock;
    private Activity activity;
    private int position ;
    public ListViewItemOnClick(Context context,Clock clock,Activity activity,int position){
        super();
        this.context = context;
        this.clock = clock;
        this.activity = activity;
        this.position = position;
    }
    @Override
    public void onClick(View v) {
        Intent cancel = new Intent(context, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context,position,cancel,PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        //取消闹钟
        alarmManager.cancel(pi);
        Intent intent = new Intent(context,AddAlarmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clock", clock);
        intent.putExtra("data",bundle);
        activity.startActivityForResult(intent,2);
    }
}
