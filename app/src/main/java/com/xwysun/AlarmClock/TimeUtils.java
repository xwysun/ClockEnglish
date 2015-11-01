package com.xwysun.AlarmClock;

import android.util.Log;

import java.sql.Time;
import java.util.Calendar;

/**
 * Created by tornado on 2015/10/28.
 */
public class TimeUtils {

    public static boolean exceedCurrentTime(long TimeMillis){
        long currentTime = System.currentTimeMillis();
        if(TimeMillis>currentTime)
        return true;
        else return false;
    }
    public static long changeTime(Time time){
        boolean exceed = exceedCurrentTime(time.getTime());
        long x = 24*60*60*1000;

        if(!exceed){
            Calendar calendar2 = Calendar.getInstance();
            calendar2.setTimeInMillis(System.currentTimeMillis());
            calendar2.set(Calendar.HOUR_OF_DAY, time.getHours());
            calendar2.set(Calendar.MINUTE,time.getMinutes());
            calendar2.set(Calendar.SECOND,0);
            calendar2.set(Calendar.MILLISECOND, 0);
            Log.d("TimeInMillis",calendar2.getTime()+"");
            if(!exceedCurrentTime(calendar2.getTimeInMillis()+60*1000)){
                Log.d("exceed",exceed+"");
                return calendar2.getTimeInMillis()+x;
            }else
                return calendar2.getTimeInMillis()+5*1000;
        }else
            return time.getTime();
    }
    public static String getWeekOfDay(){
        final Calendar c = Calendar.getInstance();
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return  mWay;
    }

}
