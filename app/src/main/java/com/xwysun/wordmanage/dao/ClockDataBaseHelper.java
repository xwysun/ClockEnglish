package com.xwysun.wordmanage.dao;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.xwysun.wordmanage.dao.mapper.ClockMapper;
import com.xwysun.wordmanage.model.clock.Clock;
import com.xwysun.wordmanage.model.clock.Repeat;
import com.xwysun.wordmanage.model.clock.Ring;
import com.xwysun.wordmanage.model.clock.WordNumber;

public class ClockDataBaseHelper extends SQLiteOpenHelper{
	
    //SQLiteOpenHelper
	private static final int DATABASE_VERSION = 1;//数据库版本
    private static final String DATABASE_NAME = "clock.db";//数据库名
	private static final String CREATE_CLOCK_TABLE="CREATE TABLE IF NOT EXISTS clock(_id INTEGER PRIMARY KEY AUTOINCREMENT,time Integer,word_number INTEGER,repeat VARCHAR,ring VARCHAR,vibration INTEGER,remark VARCHAR)";
	private static ClockDataBaseHelper instance;//单例类
    private SQLiteDatabase db;
    private Context context;
	
    private ClockDataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    	this.db=getWritableDatabase();
    }
    
    //得到数据库帮助类
	public synchronized static ClockDataBaseHelper getInstance(Context context) {
        if (instance == null) {
        	instance = new ClockDataBaseHelper(context);
        }
        return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_CLOCK_TABLE);
		addDefaultClock(db);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

	public void addDefaultClock(){
		addDefaultClock(db);
	}

	public void addClock(Clock clock){
		String sql="INSERT INTO clock(time,word_number,repeat,ring,vibration,remark) VALUES(?,?,?,?,?,?)";

		db.execSQL(sql, new Object[]{clock.getTime().getTime(),clock.getWordNumber(),clock.getRepeat(),clock.getRing().toString(),clock.getVibration(),clock.getRemark()});
		Log.d("clock", getClocks().toString());
	}
	private void addDefaultClock(SQLiteDatabase db){
		String sql="INSERT INTO clock(time,word_number,repeat,ring,vibration,remark) VALUES(?,?,?,?,?,?)";
		db.execSQL(sql, new Object[]{System.currentTimeMillis(), WordNumber.FIVE.toString(), Repeat.EVERY_DAY.toString(), Ring.GETUP.toString(),1,""});
	}

	public List<Clock> getClocks(){
		String sql="SELECT * FROM clock";
		List<Clock> clocks=new ArrayList<>();
		ClockMapper mapper=new ClockMapper();
		Cursor cursor=db.rawQuery(sql,null);
		while(cursor.moveToNext()){
			clocks.add(mapper.mapRow(cursor));
		}
		cursor.close();
		return clocks;
	}
	
	public Clock getClockById(int id){
		String sql="SELECT * FROM clock WHERE _id=?";
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(id)});
		cursor.moveToFirst();
		Clock clock=new ClockMapper().mapRow(cursor);
		cursor.close();
		return clock;
	}
	
	public void updateClock(Clock clock){
		String sql="UPDATE clock SET time=?,word_number=?,repeat=?,ring=?,vibration=?,remark=? WHERE _id=?";
		db.execSQL(sql, new Object[]{clock.getTime().getTime(),clock.getWordNumber().toString(),clock.getRepeat().toString(),clock.getRing().toString(),clock.getVibration(),clock.getRemark(),clock.getId()});
	}
	public void deleteClock(int id){
		String sql="DELETE FROM clock WHERE _id=?";
		db.execSQL(sql,new Object[]{id});
	}
}
