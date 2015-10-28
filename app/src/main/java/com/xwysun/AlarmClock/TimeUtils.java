package com.xwysun.AlarmClock;

import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by tornado on 2015/10/28.
 */
public class TimeUtils {
     private static Calendar calendar = Calendar.getInstance();

    public static boolean exceedCurrentTime(long TimeMillis){
        long currentTime = System.currentTimeMillis();
        if(TimeMillis-currentTime>0)
        return true;
        else return false;
    }
    public static long changeTime(Time time){
        boolean exceed = exceedCurrentTime(time.getTime());
        Log.d("exceed",exceed+"");
        if(!exceed){
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(System.currentTimeMillis());
            calendar2.set(Calendar.HOUR_OF_DAY, time.getHours());
            calendar2.set(Calendar.MINUTE,time.getMinutes());
            calendar2.set(Calendar.SECOND,0);
            calendar2.set(Calendar.MILLISECOND,0);
            return calendar2.getTimeInMillis();
        }else
            return time.getTime();
    }

}
