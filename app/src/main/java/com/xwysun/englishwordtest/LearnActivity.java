package com.xwysun.englishwordtest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xwysun.R;
import com.xwysun.wordmanage.WordManage;
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
    private WordManage wordManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn);
        ButterKnife.bind(this);
        wordManage = new WordManage(LearnActivity.this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
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
            public Object instantiateItem(ViewGroup container, final int position) {
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
                final ImageView collectW = (ImageView) views.get(position).findViewById(R.id.collectW);
                final ImageView collectO = (ImageView) views.get(position).findViewById(R.id.collectO);
                final ImageView collectR = (ImageView) views.get(position).findViewById(R.id.collectR);
                final ImageView collectD = (ImageView) views.get(position).findViewById(R.id.collectD);
                wordW.setText(Questions.get(position).getWord().getWord());
                wordO.setText(Questions.get(position).getWrongs().get(0).getWord());
                wordR.setText(Questions.get(position).getWrongs().get(1).getWord());
                wordD.setText(Questions.get(position).getWrongs().get(2).getWord());
                traW.setText(Questions.get(position).getWord().getTranslate());
                traO.setText(Questions.get(position).getWrongs().get(0).getTranslate());
                traR.setText(Questions.get(position).getWrongs().get(1).getTranslate());
                traD.setText(Questions.get(position).getWrongs().get(2).getTranslate());
                IsCollect(collectW, position, -1);
                IsCollect(collectO,position,0);
                IsCollect(collectR,position,1);
                IsCollect(collectD,position,2);
                collectW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordManage.collectionWord(Questions.get(position).getWord().getId());
                        collectW.setBackgroundResource(R.drawable.collected);
                        Questions.get(position).getWord().setIsCollection(1);
                    }
                });
                collectO.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordManage.collectionWord(Questions.get(position).getWord().getId());
                        collectO.setBackgroundResource(R.drawable.collected);
                        Questions.get(position).getWrongs().get(0).setIsCollection(1);
                    }
                });
                collectR.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordManage.collectionWord(Questions.get(position).getWord().getId());
                        collectR.setBackgroundResource(R.drawable.collected);
                        Questions.get(position).getWrongs().get(1).setIsCollection(1);
                    }
                });
                collectD.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        wordManage.collectionWord(Questions.get(position).getWord().getId());
                        collectD.setBackgroundResource(R.drawable.collected);
                        Questions.get(position).getWrongs().get(2).setIsCollection(1);
                    }
                });

                return views.get(position);
            }

        };
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if(keyCode==KeyEvent.KEYCODE_BACK){
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void IsCollect(ImageView imageView,int position,int id){
        if (id==-1)
        {
            if (Questions.get(position).getWord().getIsCollection() == 1) {
                imageView.setBackgroundResource(R.drawable.collected);
            } else {
                imageView.setBackgroundResource(R.drawable.uncollect);
            }
        }else {
            if (Questions.get(position).getWrongs().get(id).getIsCollection() == 1) {
                imageView.setBackgroundResource(R.drawable.collected);
            } else {
                imageView.setBackgroundResource(R.drawable.uncollect);
            }
        }

    }

//    private void collect(ImageView imageView,int position){
//        wordManage.collectionWord(Questions.get(position).getWord().getId());
//        imageView.setBackgroundResource(R.drawable.collected);
//        Questions.get(position).getWrongs().get(0).setIsCollection(1);
//
//    }
//    class ItemListener implements View.OnClickListener{
//
//        @Override
//        public void onClick(View v) {
//            switch (v.getId())
//            {
//                case R.id.collectW:
//                    wordManage.collectionWord(Questions.get(position).getWord().getId());
//                    collectW.setBackgroundResource(R.drawable.collected);
//                    Questions.get(position).getWrongs().get(0).setIsCollection(1);
//                    break;
//                case R.id.collectO:
//                    break;
//                case R.id.collectR:
//                    break;
//                case R.id.collectD:
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


}
