package com.xwysun.englishwordtest;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.xwysun.R;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;

import java.io.Serializable;
import java.util.List;

public class MainTestActivity extends AppCompatActivity {
    private TextView textView;
    //123
    private String test="open";
    private WordManage manage;
    private List<Question> Questions;
    public static final String QuestionsKey="questions";
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            try {
                Questions=manage.getQuestions();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_test);
        textView = (TextView) findViewById(R.id.click);
        textView.setText(test);
        new Thread() {
            @Override
            public void run() {
                manage = new WordManage(MainTestActivity.this);
                handler.sendEmptyMessage(0);
            }
        }.start();
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainTestActivity.this, QuestionActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable(QuestionsKey, (Serializable) Questions);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
