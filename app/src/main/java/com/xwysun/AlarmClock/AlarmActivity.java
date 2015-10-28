package com.xwysun.AlarmClock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import com.xwysun.R;
import com.xwysun.englishwordtest.QuestionActivity;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;
import com.xwysun.wordmanage.model.clock.Ring;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by tornado on 2015/9/25.
 */
public class AlarmActivity extends Activity implements View.OnClickListener {

    private WordManage manage;
    private int size = 10;
    private List<Question> Questions;
    public static final String QuestionsKey = "questions";
    private MediaPlayer mediaPlayer;
    private Vibrator vibrator = null;

    private Ring ring =Ring.GOODMORNING;
    private boolean vibrate = false;
    private String remark = "时间到了";
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (remark ==null)
                remark = "时间到了";
            try {
//                manage.setWordSize(size);
                Questions = manage.getQuestions();
                new AlertDialog.Builder(AlarmActivity.this)
                        .setTitle("闹钟")
                        .setMessage(remark)
                        .setPositiveButton("测试开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                if (mediaPlayer != null) {
                                    try {
                                        mediaPlayer.stop();//停止播放
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    mediaPlayer.release();//释放资源
                                    mediaPlayer = null;
                                }
                                AlarmActivity.this.finish();
                                Intent intent = new Intent(AlarmActivity.this, QuestionActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable(QuestionsKey, (Serializable) Questions);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }).create().show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**
         * 获取数据
         */
        ring = (Ring)this.getIntent().getBundleExtra("data").get("ring");
        vibrate = this.getIntent().getBundleExtra("data").getBoolean("vibrate");
        remark = this.getIntent().getBundleExtra("data").getString("remark");
        Log.d("data", this.getIntent().getBundleExtra("data").toString()+"---");


        boolean createState = false;
        if(ring!=null){
            RIngVUtils.play(this,ring);
        }
//        if (mediaPlayer == null) {
//            mediaPlayer = createLocalMp3(ring);
//            createState = true;
//        }
        if(vibrate==true)
            RIngVUtils.vibrate(this);
//        if(vibrator == null&&vibrate == true){
//            vibrate();
//        }
        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
        //以便其他应用程序可以使用该资源:

//        try {
//            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
//            if (createState) mediaPlayer.prepare();
//            //开始播放音频
//            mediaPlayer.start();
//        } catch (IllegalStateException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(AlarmActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    public MediaPlayer createLocalMp3(Ring ring) {
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mp = MediaPlayer.create(this, R.raw.nature);
        mp.stop();
        return mp;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (mediaPlayer != null) {
                    try {
                        mediaPlayer.stop();//停止播放
                        mediaPlayer.release();//释放资源
                        mediaPlayer = null;
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
        }
      //  return super.onKeyDown(keyCode,event);
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
