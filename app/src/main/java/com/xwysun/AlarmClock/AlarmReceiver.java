package com.xwysun.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.xwysun.englishwordtest.QuestionActivity;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;
import com.xwysun.wordmanage.model.clock.WordNumber;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tornado on 2015/9/25.
 */
public class AlarmReceiver extends BroadcastReceiver {
    private List<Question> Questions=null;
    private WordManage manage;
    public static final String QuestionsKey = "questions";
    private Handler wordhandler=new Handler(){
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
    public void onReceive(final Context context, Intent intent) {
//获取数据
        Bundle bundle = intent.getExtras();
//初始化单词
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(context);
                wordhandler.sendEmptyMessage(0);
            }
        }.start();

        Intent intent2 = new Intent(context,AlarmActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("data",bundle.toString());
        bundle.putSerializable(QuestionsKey, (Serializable) Questions);
        intent2.putExtra("data", bundle);
        context.startActivity(intent2);
    }
}
