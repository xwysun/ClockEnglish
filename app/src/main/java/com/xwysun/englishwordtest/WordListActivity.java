package com.xwysun.englishwordtest;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.xwysun.R;
import com.xwysun.wordmanage.WordManage;
import com.xwysun.wordmanage.model.Word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WordListActivity extends AppCompatActivity implements SlideCutListView.RemoveListener, AbsListView.OnScrollListener {
    private static final String TAG = "MainActivity";
    @Bind(R.id.back)
    ImageView back;

    private View moreView; //加载更多页面

//    private ArrayList<HashMap<String, String>> listData;

    private int lastItem;
    private int count;
    private WordManage wordManage;
    private int page=1;

    private SlideCutListView slideCutListView;
    private WordAdapter adapter;
    private List<Word> Wordlist = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_word_list);
        ButterKnife.bind(this);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        moreView = getLayoutInflater().inflate(R.layout.load, null);
        slideCutListView = (SlideCutListView) findViewById(R.id.slideCutListView);
        wordManage = new WordManage(WordListActivity.this);


        new Thread(new mythread()).start();
        prepareData(); //准备数据
        count = Wordlist.size();
        slideCutListView.addFooterView(moreView); //添加底部view，一定要在setAdapter之前添加，否则会报错。
        slideCutListView.removeFooterView(moreView);
        slideCutListView.setOnScrollListener(this); //设置listview的滚动事件
        init();
    }
    public class mythread implements Runnable{

        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
        }
    }


    private void init() {

        slideCutListView.setRemoveListener(this);

        back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        finish();
                                    }
                                }
        );
        adapter = new WordAdapter(this,R.layout.listview_item,Wordlist);

        slideCutListView.setAdapter(adapter);

        slideCutListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
              Toast.makeText(WordListActivity.this, Wordlist.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void prepareData() {  //准备数据


    }

    private void loadMoreData() {
        count = Wordlist.size();
    }


    //滑动删除之后的回调方法
    @Override
    public void removeItem(SlideCutListView.RemoveDirection direction, int position) {

        switch (direction) {
            case RIGHT:
                Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                wordManage.cancelCollectionWord(adapter.getItem(position).getId());
                count--;
                break;
            case LEFT:
                Toast.makeText(this, "删除成功  " + position, Toast.LENGTH_SHORT).show();
                wordManage.cancelCollectionWord(adapter.getItem(position).getId());
                count--;
                break;


            default:
                break;
        }
        adapter.remove(adapter.getItem(position));

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        Log.i(TAG, "scrollState=" + scrollState);
        //下拉到空闲是，且最后一个item的数等于数据的总数时，进行更新
        if (lastItem == count && scrollState == this.SCROLL_STATE_IDLE) {
            Log.i(TAG, "拉到最底部");
            moreView.setVisibility(view.VISIBLE);

            mHandler.sendEmptyMessage(0);

        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        Log.i(TAG, "firstVisibleItem=" + firstVisibleItem + "\nvisibleItemCount=" +
                visibleItemCount + "\ntotalItemCount" + totalItemCount);

        lastItem = firstVisibleItem + visibleItemCount ;  //减1是因为上面加了个addFooterView
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
//
                    Wordlist.addAll(wordManage.getCollectionWord(page));
                    Log.d("wordlist", Wordlist.toString());



                    if(wordManage.getCollectionWord(page).isEmpty()) {
                        Toast.makeText(WordListActivity.this, "木有更多数据！", Toast.LENGTH_SHORT).show();
                        slideCutListView.removeFooterView(moreView); //移除底部视图
                    }else {
                        loadMoreData();  //加载更多数据，这里可以使用异步加载
                        adapter.notifyDataSetChanged();
                        moreView.setVisibility(View.GONE);
                        page++;
                    }
                    Log.i(TAG, "加载更多数据");
                    break;
                case 1:

                    break;
                default:
                    break;
            }
        }

        ;
    };

    class WordAdapter extends ArrayAdapter<Word>{
        private int mResourceId;
        public WordAdapter(Context context, int textViewResourceId, List<Word> objects) {
            super(context, textViewResourceId, objects);
            this.mResourceId=textViewResourceId;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Word word = getItem(position);
            LayoutInflater inflater = getLayoutInflater();
            View view = inflater.inflate(mResourceId, null);
            TextView wordtext = (TextView) view.findViewById(R.id.word);
            TextView trantext = (TextView) view.findViewById(R.id.translate);

            wordtext.setText(word.getWord());
            trantext.setText(word.getTranslate().trim().replace(' ','\n'));
            return view;
        }

    }
}
