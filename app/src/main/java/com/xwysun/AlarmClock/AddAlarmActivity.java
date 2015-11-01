package com.xwysun.AlarmClock;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.xwysun.R;
import com.xwysun.wordmanage.ClockManage;
import com.xwysun.wordmanage.model.clock.Clock;
import com.xwysun.wordmanage.model.clock.Repeat;
import com.xwysun.wordmanage.model.clock.Ring;
import com.xwysun.wordmanage.model.clock.WordNumber;


import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by tornado on 2015/10/6.
 */
public class AddAlarmActivity extends Activity implements View.OnClickListener{
    private AlarmManager alarmManager;
    private ClockManage clockManage;
    Calendar cal=Calendar.getInstance();
    Calendar c=Calendar.getInstance();//获取日期对象
    private Button sure;
    private Button cancel;
    private CheckSwitchButton checkSwitchButton;
    private TextView wordDetail;
    private TextView repeatAlarmDetail;
    private TextView RingDetail;
    private boolean vibrate=false;
    private TextView RemarkDetail;
    private TextView SetTime;

    private LinearLayout WordNumberLayout;
    private LinearLayout RepeatAlarmLayout;
    private LinearLayout MusicLayout;
    private LinearLayout VibrateLayout;
    private LinearLayout RemarkLayout;
    private  View textEntryView;

    private Repeat RepeatDetail = Repeat.ONLY_ONE;
    private WordNumber WordNumberDetail = WordNumber.FIVE;
    private Ring RingD = Ring.GOODMORNING;
    private String RemarkD = "时间到了";


    private boolean isset =  false;
    private  Clock dataclock = null;
    private boolean switchbuttonflag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            dataclock  =(Clock)this.getIntent().getBundleExtra("data").getSerializable("clock");
        }catch (NullPointerException e){
            dataclock = null;
        }

        setContentView(R.layout.activity_addalarm);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);




        sure = (Button)findViewById(R.id.sure);
        cancel = (Button)findViewById(R.id.cancel);
        checkSwitchButton = (CheckSwitchButton)findViewById(R.id.vibrate_CSBtn);
        wordDetail = (TextView)findViewById(R.id.word_number_detail);
        repeatAlarmDetail = (TextView)findViewById(R.id.repeat_alarm_detail);
        RemarkDetail = (TextView)findViewById(R.id.remark_detail);
        RingDetail = (TextView)findViewById(R.id.ring_detail);
        SetTime = (TextView)findViewById(R.id.settime);

        WordNumberLayout = (LinearLayout)findViewById(R.id.word_number);
        RepeatAlarmLayout = (LinearLayout)findViewById(R.id.repeat_alarm);
        MusicLayout = (LinearLayout)findViewById(R.id.ring);
        VibrateLayout = (LinearLayout)findViewById(R.id.vibrate);
        RemarkLayout = (LinearLayout)findViewById(R.id.remark);
        sure.setOnClickListener(this);
        cancel.setOnClickListener(this);
        SetTime.setOnClickListener(this);
        RepeatAlarmLayout.setOnClickListener(this);
        MusicLayout.setOnClickListener(this);
        VibrateLayout.setOnClickListener(this);
        RemarkLayout.setOnClickListener(this);
        WordNumberLayout.setOnClickListener(this);
        checkSwitchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(switchbuttonflag) {
                    if (isChecked) {
                        vibrate = true;
                    } else {
                        vibrate = false;
                    }
                }
            }
        });

        if(dataclock !=null){
            Log.d("dataclock",dataclock.toString());
            isset = true ;
            if(dataclock.getVibration()==1||dataclock.getVibration()==10){
                switchbuttonflag = false;
                checkSwitchButton.setChecked(true);
                switchbuttonflag = true;
            }

            c.setTimeInMillis(dataclock.getTime().getTime());        //设置Calendar对象
            c.set(Calendar.HOUR_OF_DAY, dataclock.getTime().getHours());        //设置闹钟小时数
            c.set(Calendar.MINUTE, dataclock.getTime().getMinutes());            //设置闹钟的分钟数
            c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
            c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数

          repeatAlarmDetail.setText(dataclock.getRepeat().toString());
           wordDetail.setText(dataclock.getWordNumber().toString());
        }
    }
    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.sure:
                Bundle bundle = new Bundle();
                bundle.putSerializable("wordnumber", WordNumberDetail);
                bundle.putBoolean("vibrate", vibrate);
                if(isset&&dataclock!=null) {
                    if (dataclock.getVibration() == 10 || dataclock.getVibration() == 20) {
                        bundle.putBoolean("isopen",false);
                    } else {
                        bundle.putBoolean("isopen",true);
                    }
                }
                if(RemarkD !=null)
                    bundle.putString("remark",RemarkD);
                bundle.putSerializable("ring", RingD);
                bundle.putSerializable("repeat",RepeatDetail);
                bundle.putSerializable("time",c.getTimeInMillis());

                Intent intent = new Intent();    //创建Intent对象
                intent.putExtra("data", bundle);

                clockManage = new ClockManage(AddAlarmActivity.this);
//                PendingIntent pi = PendingIntent.getBroadcast(AddAlarmActivity.this,
//                        clockManage.getClocks().size()+1, intent,PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent
//                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
//                Log.d("alarmtime",c.getTime().toString());
//                if (RepeatDetail == Repeat.ONLY_ONE){
//                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
//                }else if (RepeatDetail == Repeat.EVERY_DAY) {
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
//                }else if(RepeatDetail == Repeat.MON2FIR){
//                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
//                }
                //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);        //设置闹钟，当前时间就唤醒

                Time time = new Time(c.getTimeInMillis());
                Log.d("sql time",time.toString()+"---hour="+time.getHours());
                Clock clock = new Clock();
                clock.setWordNumber(WordNumberDetail);
                if(RemarkD != null) {
                    clock.setRemark(RemarkD);
                }else
                    clock.setRemark("备注");
                clock.setTime(time);
                clock.setRing(RingD);
                clock.setRepeat(RepeatDetail);
                if(isset&&dataclock!=null) {
                    if(dataclock.getVibration()==10||dataclock.getVibration()==20){
                        if(vibrate)
                            clock.setVibration(10);//是否振动,1为振动，2为不震动
                        else clock.setVibration(20);
                    }else {
                        if(vibrate)
                            clock.setVibration(1);//是否振动,1为振动，2为不震动
                        else clock.setVibration(2);
                    }
                    clock.setId(dataclock.getId());
                    clockManage.setClock(clock);
                    Log.d("modifyclock",c.getTime()+clock.toString());
                    Toast.makeText(AddAlarmActivity.this, "闹钟修改成功", Toast.LENGTH_SHORT).show();//提示用户
                    setResult(RESULT_OK, intent);
                    AddAlarmActivity.this.finish();
                }else {
                    if(vibrate)
                        clock.setVibration(1);//是否振动,1为振动，2为不震动
                    else clock.setVibration(2);
                    clockManage.addClock(clock);
                    Toast.makeText(AddAlarmActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();//提示用户
                    setResult(RESULT_OK, intent);
                    AddAlarmActivity.this.finish();
                }
                break;
            case R.id.cancel:
                setResult(RESULT_CANCELED);
                AddAlarmActivity.this.finish();
                break;
            case R.id.settime:
                Dialog dialog=null;
                dialog=new TimePickerDialog(
                        this,
                        new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker timePicker, int hourOfDay,int minute) {
//                                if(isset) {
//                                    c.setTimeInMillis(dataclock.getTime().getTime());        //设置Calendar对象
//                                    c.set(Calendar.HOUR_OF_DAY,dataclock.getTime().getHours());
//                                    c.set(Calendar.MINUTE, dataclock.getTime().getMinutes());            //设置闹钟的分钟数
//                                    c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
//                                    c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
//                                }
//                                else {
                                    c.setTimeInMillis(System.currentTimeMillis());        //设置Calendar对象
                                    c.set(Calendar.HOUR_OF_DAY, hourOfDay);        //设置闹钟小时数
                                    c.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                                    c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                                    c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
                    //            }

                            }
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false);
                dialog.show();
                break;
            case R.id.repeat_alarm:
                final AlertDialog redialog = new AlertDialog.Builder(AddAlarmActivity.this).create();
//                CustomDialog redialog = new CustomDialog(AddAlarmActivity.this,R.style.customDialog,R.layout.dialog_repeat);
                redialog.show();
                WindowManager windowManager = getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                WindowManager.LayoutParams lp = redialog.getWindow().getAttributes();
                lp.width = (int)(display.getWidth()); //设置宽度
                redialog.getWindow().setAttributes(lp);
                Window window = redialog.getWindow();
                window.setContentView(R.layout.dialog_repeat);
                window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window.setWindowAnimations(R.style.dialog);  //添加动画
                redialog.show();
                TextView OnlyOne = (TextView)window.findViewById(R.id.onlyone);
                TextView EveryDay = (TextView)window.findViewById(R.id.everyday);
                TextView OneToFive = (TextView)window.findViewById(R.id.one2five);
                OnlyOne.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        RepeatDetail = Repeat.ONLY_ONE;
                        repeatAlarmDetail.setText("只响一次");
                        redialog.cancel();
                    }
                });
                EveryDay.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        RepeatDetail = Repeat.EVERY_DAY;
                        repeatAlarmDetail.setText("每天");
                        redialog.cancel();
                    }
                });
                OneToFive.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        RepeatDetail = Repeat.MON2FIR;
                        repeatAlarmDetail.setText("周一至周五");
                        redialog.cancel();
                    }
                });
                break;
            case R.id.word_number:
                final AlertDialog wordnumdialog = new AlertDialog.Builder(AddAlarmActivity.this).create();
//                CustomDialog redialog = new CustomDialog(AddAlarmActivity.this,R.style.customDialog,R.layout.dialog_repeat);
                wordnumdialog.show();
                WindowManager windowManager2 = getWindowManager();
                Display display2 = windowManager2.getDefaultDisplay();
                WindowManager.LayoutParams lp2 = wordnumdialog.getWindow().getAttributes();
                lp2.width = (int)(display2.getWidth()); //设置宽度
                wordnumdialog.getWindow().setAttributes(lp2);
                Window window2 = wordnumdialog.getWindow();
                window2.setContentView(R.layout.dialog_wordnumber);
                window2.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window2.setWindowAnimations(R.style.dialog);  //添加动画
                wordnumdialog.show();
                TextView Five = (TextView)window2.findViewById(R.id.five);
                TextView Ten = (TextView)window2.findViewById(R.id.ten);
                TextView Fifteen = (TextView)window2.findViewById(R.id.fifteen);
                Five.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        WordNumberDetail = WordNumber.FIVE;
                        wordDetail.setText("5个");
                        wordnumdialog.cancel();
                    }
                });
                Ten.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        WordNumberDetail = WordNumber.TEN;
                        wordDetail.setText("10个");
                        wordnumdialog.cancel();
                    }
                });
                Fifteen.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        WordNumberDetail = WordNumber.FIFTEEN;
                        wordDetail.setText("15个");
                        wordnumdialog.cancel();
                    }
                });
                break;
            case R.id.ring:
                final AlertDialog ringdialog = new AlertDialog.Builder(AddAlarmActivity.this).create();
//                CustomDialog redialog = new CustomDialog(AddAlarmActivity.this,R.style.customDialog,R.layout.dialog_repeat);
                ringdialog.show();
                WindowManager windowManager3 = getWindowManager();
                Display display3 = windowManager3.getDefaultDisplay();
                WindowManager.LayoutParams lp3 = ringdialog.getWindow().getAttributes();
                lp3.width = (int)(display3.getWidth()); //设置宽度
                ringdialog.getWindow().setAttributes(lp3);
                Window window3 = ringdialog.getWindow();
                window3.setContentView(R.layout.dialog_ring);
                window3.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window3.setWindowAnimations(R.style.dialog);  //添加动画
                ringdialog.show();
                TextView GetUp = (TextView)window3.findViewById(R.id.getup);
                TextView GoodLuck = (TextView)window3.findViewById(R.id.goodluck);
                TextView GoodMorning = (TextView)window3.findViewById(R.id.goodmorning);
                GetUp.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        RingD = Ring.GETUP;
                        RingDetail.setText("cry on my shoulder");
                        ringdialog.cancel();
                    }
                });
                GoodLuck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RingD = Ring.GOODLUCK;
                        RingDetail.setText("滴答");
                        ringdialog.cancel();
                    }
                });
                GoodMorning.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        RingD = Ring.GOODMORNING;
                        RingDetail.setText("good morning");
                        ringdialog.cancel();
                    }
                });
                break;
            case R.id.remark:

//                LayoutInflater factory = LayoutInflater.from(this);
//                textEntryView = factory.inflate(R.layout.dialog_remark, null);
//                textEntryView.findFocus();

                final AlertDialog remarkdialog = new AlertDialog.Builder(AddAlarmActivity.this).create();
                remarkdialog.setView(new EditText(AddAlarmActivity.this));
//                CustomDialog redialog = new CustomDialog(AddAlarmActivity.this,R.style.customDialog,R.layout.dialog_repeat);
                remarkdialog.show();
                WindowManager windowManager4 = getWindowManager();
                Display display4 = windowManager4.getDefaultDisplay();
                WindowManager.LayoutParams lp4 = remarkdialog.getWindow().getAttributes();
                lp4.width = (int)(display4.getWidth()); //设置宽度
                remarkdialog.getWindow().setAttributes(lp4);
                final Window window4 = remarkdialog.getWindow();
                window4.setContentView(R.layout.dialog_remark);
                window4.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置
                window4.setWindowAnimations(R.style.dialog);  //添加动画
                remarkdialog.show();
                Button CancelRemark = (Button)window4.findViewById(R.id.cancel_remark);
                Button SureRemark = (Button)window4.findViewById(R.id.sure_remark);
                final EditText remarkD = (EditText)window4.findViewById(R.id.remarkEt);
                CancelRemark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        remarkdialog.cancel();
                    }
                });
                SureRemark.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RemarkD = remarkD.getText().toString();
                        remarkdialog.cancel();
                    }
                });


//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
//                InputMethodManager imm = (InputMethodManager)
//                        getSystemService(INPUT_METHOD_SERVICE);
//                imm.showSoftInput(remarkD, 0); //显示软键盘
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); //显示软键盘


//                InputMethodManager inputManager =
//
//                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                inputManager.showSoftInput(remarkD, 0);
//                Timer timer = new Timer();
//
//                timer.schedule(new TimerTask()
//
//                {
//
//                    public void run()
//
//                    {
//
//                        ;
//
//                    }
//
//                });
                break;
            default:
                break;
        }
    }
    public void showKeyboard(){
        if(textEntryView!=null) {
            textEntryView.setFocusable(true);
            textEntryView.setFocusableInTouchMode(true);
            //请求获得焦点
            textEntryView.requestFocus();
            //调用系统输入法
            InputMethodManager inputManager = (InputMethodManager) textEntryView
                    .getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(textEntryView, 0);
        }
    }

}
