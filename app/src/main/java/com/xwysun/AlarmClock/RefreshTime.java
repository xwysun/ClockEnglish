package com.xwysun.AlarmClock;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tornado on 2015/11/2.
 */
public class RefreshTime extends Thread {
    public static boolean runFlag = true;
    public static final  int UPDATE_TIME = 3;
    private Handler handler;
    Message message ;

    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
    public RefreshTime(Handler handler){
        this.handler = handler;
    }
    @Override
    public void run() {
        //Time Controller
        //Modify Time After 3000 ms
        long startTime = System.currentTimeMillis();
        while (true) {
            while (runFlag) {
                long endTime = System.currentTimeMillis();
                if (endTime - startTime > 5000) {
                    startTime = endTime;
                    Date curDate = new Date(System.currentTimeMillis());
                    String currentTime = formatter.format(curDate);
                    message = handler.obtainMessage(UPDATE_TIME, currentTime);
                    handler.sendMessage(message);
                    Log.d("timeupdate", currentTime);
                }
            }
        }
    }
}
