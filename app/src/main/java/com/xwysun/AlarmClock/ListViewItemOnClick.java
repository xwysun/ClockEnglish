package com.xwysun.AlarmClock;

import android.app.Activity;
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
    public ListViewItemOnClick(Context context,Clock clock,Activity activity){
        super();
        this.context = context;
        this.clock = clock;
        this.activity = activity;
    }
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context,AddAlarmActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("clock", clock);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent,1);
    }
}
