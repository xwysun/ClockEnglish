package com.xwysun.englishwordtest;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwysun.R;
import com.xwysun.wordmanage.model.Question;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class LearnActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.viewPager)
    ViewPager viewPager;
    private View pager1, pager2, pager3, pager4, pager5;
    private List<View> views;
    private List<Question> Questions;
    public static final String QuestionsKey = "questions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
        LayoutInflater inflater = getLayoutInflater();
        Questions = (List<Question>) getIntent().getExtras().getSerializable(QuestionsKey);
        pager1 = inflater.inflate(R.layout.viewpager1, null);
        pager2 = inflater.inflate(R.layout.viewpager1, null);
        pager3 = inflater.inflate(R.layout.viewpager1, null);
        pager4 = inflater.inflate(R.layout.viewpager1, null);
        pager5 = inflater.inflate(R.layout.viewpager1, null);
        views = new ArrayList<View>();
        views.add(pager1);
        views.add(pager2);
        views.add(pager3);
        views.add(pager4);
        views.add(pager5);
        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }
        );

        PagerAdapter pagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position,
                                    Object object) {
                // TODO Auto-generated method stub
                container.removeView(views.get(position));
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                // TODO Auto-generated method stub
                container.addView(views.get(position));
                TextView wordW = (TextView) views.get(position).findViewById(R.id.wordW);
                TextView wordO = (TextView) views.get(position).findViewById(R.id.wordO);
                TextView wordR = (TextView) views.get(position).findViewById(R.id.wordR);
                TextView wordD = (TextView) views.get(position).findViewById(R.id.wordD);
                TextView traW = (TextView) views.get(position).findViewById(R.id.traW);
                TextView traO = (TextView) views.get(position).findViewById(R.id.traO);
                TextView traR = (TextView) views.get(position).findViewById(R.id.traR);
                TextView traD = (TextView) views.get(position).findViewById(R.id.traD);
                wordW.setText(Questions.get(position).getWord().getWord());
                wordO.setText(Questions.get(position).getWrongs().get(0).getWord());
                wordR.setText(Questions.get(position).getWrongs().get(1).getWord());
                wordD.setText(Questions.get(position).getWrongs().get(2).getWord());
                traW.setText(Questions.get(position).getWord().getTranslate());
                traO.setText(Questions.get(position).getWrongs().get(0).getTranslate());
                traR.setText(Questions.get(position).getWrongs().get(1).getTranslate());
                traD.setText(Questions.get(position).getWrongs().get(2).getTranslate());

                return views.get(position);
            }

        };
        viewPager.setAdapter(pagerAdapter);

    }

}
