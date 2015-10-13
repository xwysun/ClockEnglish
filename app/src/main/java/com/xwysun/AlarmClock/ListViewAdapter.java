package com.xwysun.AlarmClock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.xwysun.R;


/**
 * Created by tornado on 2015/10/5.
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    public ListViewAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (convertView == null){
            convertView = inflater.inflate(R.layout.alarm_item,null);
            convertView.setTag(holder);
        }else
            holder = (ViewHolder)convertView.getTag();

        return convertView;
    }
    public final class ViewHolder{
        public TextView title;
        public Button viewBtn;
    }
}

