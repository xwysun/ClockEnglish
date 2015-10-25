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
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.xwysun.R;
import com.xwysun.englishwordtest.LearnActivity;
import com.xwysun.englishwordtest.QuestionActivity;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button SetAlarmBtn;
    private AlarmManager alarmManager;
    private ImageView addAlarm;
    private ListView alarmLV;
    Calendar cal=Calendar.getInstance();
    final int DIALOG_TIME = 0;



    private WordManage manage;
    private List<Question> Questions;
    public static final String QuestionsKey="questions";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                Questions=manage.getQuestions();
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

        alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        SetAlarmBtn = (Button)findViewById(R.id.set);
        alarmLV = (ListView)findViewById(R.id.alarmLV);

        ListViewAdapter adapter = new ListViewAdapter(this);
        alarmLV.setAdapter(adapter);





        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(MainActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();
        mToolbar=(Toolbar)findViewById(R.id.toolbar);
        initToolbar();

        SetAlarmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(DIALOG_TIME);
            }
        });
//        addAlarm.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent();
//                intent.setClass(MainActivity.this,AddAlarmActivity.class);
//                startActivity(intent);
//            }
//        });

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
    private void initToolbar(){
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setLogo(R.drawable.app);
        mToolbar.setTitle("单词闹钟");// 标题的文字需在setSupportActionBar之前，不然会无效
        mToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_settings:
                        Intent intent = new Intent(MainActivity.this, LearnActivity.class);
                        Bundle bundle=new Bundle();
                        Log.e("word",Questions.toString());
                        bundle.putSerializable(QuestionsKey, (Serializable) Questions);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    default:
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        Dialog dialog=null;
        switch (id) {
            case DIALOG_TIME:
                dialog=new TimePickerDialog(
                        this,
                        new TimePickerDialog.OnTimeSetListener(){
                            public void onTimeSet(TimePicker timePicker, int hourOfDay,int minute) {
                                Calendar c=Calendar.getInstance();//获取日期对象
                                c.setTimeInMillis(System.currentTimeMillis());        //设置Calendar对象
                                c.set(Calendar.HOUR, hourOfDay);        //设置闹钟小时数
                                c.set(Calendar.MINUTE, minute);            //设置闹钟的分钟数
                                c.set(Calendar.SECOND, 0);                //设置闹钟的秒数
                                c.set(Calendar.MILLISECOND, 0);            //设置闹钟的毫秒数
                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);    //创建Intent对象
                                PendingIntent pi = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);    //创建PendingIntent
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);        //设置闹钟
                                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                c.getTimeInMillis(), 10 * 1000,pi);
                                //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pi);        //设置闹钟，当前时间就唤醒
                                Toast.makeText(MainActivity.this, "闹钟设置成功", Toast.LENGTH_SHORT).show();//提示用户
                            }
                        },
                        cal.get(Calendar.HOUR_OF_DAY),
                        cal.get(Calendar.MINUTE),
                        false);

                break;
        }
        return dialog;
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

}
