package com.xwysun.wordmanage.dao.mapper;

import java.sql.Time;

import android.database.Cursor;
import android.util.Log;

import com.xwysun.wordmanage.model.clock.Clock;
import com.xwysun.wordmanage.model.clock.Repeat;
import com.xwysun.wordmanage.model.clock.Ring;
import com.xwysun.wordmanage.model.clock.WordNumber;


public class ClockMapper implements RowMapper<Clock>{

	@Override
	public Clock mapRow(Cursor cursor) {
		Clock clock=new Clock();
		clock.setId(cursor.getInt(cursor.getColumnIndex("_id")));
		clock.setTime(new Time(cursor.getLong(cursor.getColumnIndex("time"))));
		clock.setWordNumber(WordNumber.of(cursor.getInt(cursor.getColumnIndex("word_number"))));
		clock.setRepeat(Repeat.of(cursor.getString(cursor.getColumnIndex("repeat"))));
		clock.setRing(Ring.of(cursor.getString(cursor.getColumnIndex("ring"))));
		clock.setVibration(cursor.getInt(cursor.getColumnIndex("vibration")));
		clock.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
		return clock;
	}
	
}	
