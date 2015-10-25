package com.xwysun.englishwordtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xwysun.R;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QuestionActivity extends AppCompatActivity {

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.textA)
    TextView textA;
    @Bind(R.id.answerA)
    LinearLayout answerA;
    @Bind(R.id.textB)
    TextView textB;
    @Bind(R.id.answerB)
    LinearLayout answerB;
    @Bind(R.id.textC)
    TextView textC;
    @Bind(R.id.answerC)
    LinearLayout answerC;
    @Bind(R.id.textD)
    TextView textD;
    @Bind(R.id.answerD)
    LinearLayout answerD;

    @Bind(R.id.QueNum)
    TextView QueNum;
    @Bind(R.id.logtime)
    TextView logtime;
    @Bind(R.id.star)
    LinearLayout star;
    @Bind(R.id.question)
    LinearLayout question;
    @Bind(R.id.mainbg)
    LinearLayout mainbg;
    @Bind(R.id.staricon)
    ImageView staricon;
    @Bind(R.id.exit)
    LinearLayout exit;

    private List<Question> Questions;
    private Question questionList;
    private Integer[] random;
    int QuestionNum = 0;
    public static final String QuestionsKey = "questions";
    private WordManage wordManage;
    private Timer timer;
    public static final int TIMESIZE = 10;
    private int time;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        toast=new Toast(this);
        wordManage = new WordManage(QuestionActivity.this);
        star.setOnClickListener(new Listener());
        exit.setOnClickListener(new Listener());
        exit.setVisibility(View.INVISIBLE);
        Questions = (List<Question>) getIntent().getExtras().getSerializable(QuestionsKey);
        if (Questions != null) {
            initQustions();
        } else {
            Toast.makeText(getApplicationContext(), "获取数据失败", Toast.LENGTH_SHORT);
            finish();

        }


    }

    private Integer[] getRandomList(int n)

    {
        Integer[] arryRandom = new Integer[n];
        for (int i = 0; i < n; i++)
            arryRandom[i] = i;
        List list = Arrays.asList(arryRandom);
        Collections.shuffle(list);
        return arryRandom;
    }

    private class Listener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.answerA:
                    checkAnswer(0);
                    break;
                case R.id.answerB:
                    checkAnswer(1);
                    break;
                case R.id.answerC:
                    checkAnswer(2);
                    break;
                case R.id.answerD:
                    checkAnswer(3);
                    break;
                case R.id.exit:
                    timer=null;
                    toast.cancel();
                    finish();
                    break;
                case R.id.star:
                    if (!Questions.isEmpty()) {
                        wordManage.collectionWord(questionList.getWord().getId());
                        staricon.setBackgroundResource(R.drawable.stared);
                        questionList.getWord().setIsCollection(1);

                    } else {
                        toast.makeText(getApplicationContext(), "无可收藏单词", Toast.LENGTH_SHORT).show();
                    }
                default:
                    break;
            }
        }

    }

    private void checkAnswer(int ans) {
        timer.cancel();
        timer.purge();
        if (ans == random[0]&&!Questions.isEmpty()) {
            toast.makeText(getApplicationContext(), "回答正确", Toast.LENGTH_SHORT).show();
            Questions.remove(QuestionNum);
            if (Questions.isEmpty()) {
                question.setVisibility(View.GONE);
                mainbg.setBackgroundResource(R.drawable.finish);
                toast.cancel();
                exit.setVisibility(View.VISIBLE);
                timer.cancel();
                timer.purge();
            }
            if (QuestionNum < Questions.size()) {
                initQustions();
            } else {
                QuestionNum = 0;
                initQustions();
            }

        } else {
            toast.makeText(getApplicationContext(), "回答错误", Toast.LENGTH_SHORT).show();
            QuestionNum++;
            if (QuestionNum < Questions.size()) {
                initQustions();
            } else {
                QuestionNum = 0;
                initQustions();
            }
        }

    }

    private List<TextView> getrandomAnswer() {
        random = getRandomList(4);
        List<TextView> randomKey = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            switch (random[i]) {
                case 0:
                    randomKey.add(textA);
                    break;
                case 1:
                    randomKey.add(textB);
                    break;
                case 2:
                    randomKey.add(textC);
                    break;
                case 3:
                    randomKey.add(textD);
                    break;
                default:
                    break;

            }
        }
        return randomKey;
    }

    private void initQustions() {
        List<TextView> randomAnswer = getrandomAnswer();
        if (Questions.isEmpty()) {
            question.setVisibility(View.GONE);
            mainbg.setBackgroundResource(R.drawable.finish);
            exit.setVisibility(View.VISIBLE);
            timer.purge();
            timer.cancel();
        } else {
            questionList = Questions.get(QuestionNum);
            initTimer();
        }
        QueNum.setText(Questions.size() + "/5");
        title.setText(questionList.getWord().getWord());
        if (questionList.getWord().getIsCollection() == 1) {
            staricon.setBackgroundResource(R.drawable.stared);
        } else {
            staricon.setBackgroundResource(R.drawable.star);
        }
        randomAnswer.get(0).setText(questionList.getWord().getTranslate());
        randomAnswer.get(1).setText(questionList.getWrongs().get(0).getTranslate());
        randomAnswer.get(2).setText(questionList.getWrongs().get(1).getTranslate());
        randomAnswer.get(3).setText(questionList.getWrongs().get(2).getTranslate());
        answerA.setOnClickListener(new Listener());
        answerB.setOnClickListener(new Listener());
        answerC.setOnClickListener(new Listener());
        answerD.setOnClickListener(new Listener());
    }

    private void initTimer() {
        time = TIMESIZE;
        logtime.setText(time + "s");
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                time--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logtime.setText(time + "s");
                        if (time <= 0) {
                            timer.cancel();
                            timer.purge();
                            toast.makeText(getApplicationContext(), "回答超时", Toast.LENGTH_SHORT).show();
                            QuestionNum++;
                            if (QuestionNum < Questions.size()) {
                                initQustions();
                            } else {
                                QuestionNum = 0;
                                initQustions();
                            }
                        }
                    }
                });

            }
        };
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question, menu);
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
