package com.xwysun.AlarmClock;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.xwysun.R;
import com.xwysun.englishwordtest.LearnActivity;
import com.xwysun.englishwordtest.WordListActivity;
import com.xwysun.wordmanage.ClockManage;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;
import com.xwysun.wordmanage.model.clock.Clock;
import com.xwysun.wordmanage.model.clock.Repeat;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Clock> deleteclockList = new ArrayList();
    private Toolbar mToolbar;

    private AlarmManager alarmManager;
    private TextView Time;
    private TextView Weekofday;
    private TextView Date;
    private ImageView addAlarm;
    private ListView alarmLV;
    private LinearLayout deleteAlarm;
    private ListViewAdapter adapter;
    private ImageView collect;
    private ImageView learn;
    Calendar cal=Calendar.getInstance();
    private int State = 1;
    final int DIALOG_TIME = 0;
    ClockManage clockManage;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1) {
                deleteAlarm.setVisibility(View.VISIBLE);
                State = 0;//删除闹钟界面
            }else if (msg.what == 3){
//                Time.setText(time);
//                Date.setText(date);
//                Weekofday.setText("星期" + getWeekOfDay());
            }

        }
    };
    private WordManage manage;
    private List<Question> Questions;
    public static final String QuestionsKey="questions";
    private Handler wordhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                Questions=manage.getLearnQuestion();
                Log.d("QUE",Questions.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(MainActivity.this);
                wordhandler.sendEmptyMessage(0);
            }
        }.start();
     //   this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏


        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);

        SimpleDateFormat sDatedf=new SimpleDateFormat("yyyy.MM.dd");
        String date=sDatedf.format(new java.util.Date());
        SimpleDateFormat TSdf=new SimpleDateFormat("hh:mm");
        String time=TSdf.format(new java.util.Date());

        collect=(ImageView)findViewById(R.id.collect);
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WordListActivity.class);
                startActivity(intent);
            }
        });
        learn=(ImageView)findViewById(R.id.learn);
        learn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                Bundle bundle=new Bundle();
                Log.e("word",Questions.toString());
                bundle.putSerializable(QuestionsKey, (Serializable) Questions);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        Time = (TextView)findViewById(R.id.time);
        Time.setText(time);
        Weekofday  = (TextView)findViewById(R.id.week_of_day);
        Weekofday.setText("星期"+getWeekOfDay());
        Date = (TextView)findViewById(R.id.date);
        Date.setText(date);

        alarmLV = (ListView)findViewById(R.id.alarmLV);
        deleteAlarm = (LinearLayout)findViewById(R.id.delete_alarm);
         adapter = new ListViewAdapter(this,handler,MainActivity.this);
        addAlarm = (ImageView)findViewById(R.id.add_alarm);
        alarmLV.setAdapter(adapter);
//        mToolbar=(Toolbar)findViewById(R.id.toolbar);
//        initToolbar();
        deleteAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clockManage = new ClockManage(MainActivity.this);

                    clockManage.deleteClocks(deleteclockList);

                adapter.TYPE = 1;
                adapter.refleshData();
                adapter.notifyDataSetChanged();
                deleteAlarm.setVisibility(View.GONE);
                State = 1;//显示闹钟界面
            }
        });
        addAlarm.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent();
                intent.setClass(MainActivity.this,AddAlarmActivity.class);
                startActivityForResult(intent, 1);
            }
        });

//        Intent intent = new Intent(MainActivity.this,
//                RepeatingAlarm.class);
//        PendingIntent sender = PendingIntent.getBroadcast(
//                MainActivity.this, 0, intent, 0);
//
//        // We want the alarm to go off 10 seconds from now.
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(System.currentTimeMillis());
//        calendar.add(Calendar.SECOND, 10);
//        // Schedule the alarm!
//        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
//        am.setRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(), 10 * 1000, sender);
    }
//    private void initToolbar(){
//        mToolbar = (Toolbar) findViewById(R.id.toolbar);
//        mToolbar.setLogo(R.drawable.app);
//        mToolbar.setTitle("单词闹钟");// 标题的文字需在setSupportActionBar之前，不然会无效
//        mToolbar.setTitleTextColor(Color.WHITE);
//        setSupportActionBar(mToolbar);
//        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.action_settings:
//                        Intent intent = new Intent();
//                        intent.setClass(MainActivity.this,AddAlarmActivity.class);
//                        startActivityForResult(intent,1);
//                    default:
//                        break;
//                }
//                return true;
//            }
//        });
//
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(MainActivity.this);
                wordhandler.sendEmptyMessage(0);
            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode ==1&&resultCode==RESULT_OK){
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis((long)data.getBundleExtra("data").getSerializable("time"));
            Repeat RepeatDetail =(Repeat)data.getBundleExtra("data").getSerializable("repeat");
            Intent intent= new Intent(MainActivity.this,AlarmReceiver.class);
            intent.putExtras(data.getBundleExtra("data"));
            if(clockManage==null){
                clockManage = new ClockManage(MainActivity.this);
            }
            PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,
                    clockManage.getClocks().size(), intent,PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent
            if (RepeatDetail == Repeat.ONLY_ONE){
                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
            }else if (RepeatDetail == Repeat.EVERY_DAY) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
            }else if(RepeatDetail == Repeat.MON2FIR){
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
            }
            //刷新界面
            adapter.TYPE = 1;
            adapter.refleshData();
            adapter.notifyDataSetChanged();
        }
        if(requestCode==2&&resultCode == RESULT_OK){
            boolean isopen = (boolean)data.getBundleExtra("data").getSerializable("isopen");
            if(isopen) {//是否开启
                Calendar c = Calendar.getInstance();
                c.setTimeInMillis((long) data.getBundleExtra("data").getSerializable("time"));
                Repeat RepeatDetail = (Repeat) data.getBundleExtra("data").getSerializable("repeat");

                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                intent.putExtras(data.getBundleExtra("data"));
                if (clockManage == null) {
                    clockManage = new ClockManage(MainActivity.this);
                }
                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this,
                        clockManage.getClocks().size(), intent, PendingIntent.FLAG_UPDATE_CURRENT);    //创建PendingIntent
                if (RepeatDetail == Repeat.ONLY_ONE) {
                    alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
                } else if (RepeatDetail == Repeat.EVERY_DAY) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
                } else if (RepeatDetail == Repeat.MON2FIR) {
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 10 * 1000, pi);
                }
            }
            //刷新界面
            adapter.TYPE = 1;
            adapter.refleshData();
            adapter.notifyDataSetChanged();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK ){
            {
                if(State == 1) {
                    // 创建退出对话框
                    AlertDialog isExit = new AlertDialog.Builder(this).create();
                    // 设置对话框标题
                    isExit.setTitle("系统提示");
                    // 设置对话框消息
                    isExit.setMessage("确定要退出吗");
                    // 添加选择按钮并注册监听
                    isExit.setButton("确定", listener);
                    isExit.setButton2("取消", listener);
                    // 显示对话框
                    isExit.show();
                }else if(State == 0){
                    State = 1;
                    deleteAlarm.setVisibility(View.GONE);
                    adapter.TYPE = 1;
                    adapter.notifyDataSetChanged();
                }

            }

            return false;
        }


        return super.onKeyDown(keyCode, event);

    }
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    finish();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };
    public String getWeekOfDay(){
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
