package com.xwysun.AlarmClock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
    private List<Question> Questions;
    private WordManage manage;
    public static final String QuestionsKey = "questions";
    @Override
    public void onReceive(Context context, Intent intent) {
//获取数据
        Bundle bundle = intent.getExtras();
//初始化单词
        manage = new WordManage(context);
        manage.setWordSize(Integer.parseInt(bundle.getSerializable("wordnumber").toString()));
        Questions = manage.getQuestions();



        Intent intent2 = new Intent(context,QuestionActivity.class);
        intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("data",bundle.toString());
        bundle.putSerializable(QuestionsKey, (Serializable) Questions);
        intent2.putExtra("data",bundle);
        context.startActivity(intent2);
    }
}
