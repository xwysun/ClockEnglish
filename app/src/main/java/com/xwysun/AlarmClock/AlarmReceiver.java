package com.xwysun.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by tornado on 2015/9/25.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent2 = new Intent(context,AlarmActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Bundle bundle = intent.getExtras();
        Log.d("data",bundle.toString());
        intent2.putExtra("data",bundle);
        context.startActivity(intent2);
    }
}
