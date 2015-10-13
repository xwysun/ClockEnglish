package com.xwysun.AlarmClock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import com.xwysun.englishwordtest.QuestionActivity;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tornado on 2015/9/25.
 */
public class AlarmActivity extends Activity {

    private WordManage manage;
    private int size=10;
    private List<Question> Questions;
    public static final String QuestionsKey="questions";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                manage.setWordSize(size);
                Questions=manage.getQuestions();
                new AlertDialog.Builder(AlarmActivity.this)
                        .setTitle("闹钟")
                        .setMessage("时间到了")
                        .setPositiveButton("测试开启", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
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
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(AlarmActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

}
