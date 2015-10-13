package com.xwysun.wordmanage.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.xwysun.wordmanage.dao.mapper.WordMapper;
import com.xwysun.wordmanage.exception.DataBaseInitException;
import com.xwysun.wordmanage.model.Counter;
import com.xwysun.wordmanage.model.Word;


public class DataBaseHelper extends SQLiteOpenHelper{
    //SQLiteOpenHelper
	private static final int DATABASE_VERSION = 1;//数据库版本
    private static final String DATABASE_NAME = "dictionary.db";//数据库名
    private static final String CTRATE_DICTIONARY_TABLE="CREATE TABLE IF NOT EXISTS dictionary(_id INTEGER PRIMARY KEY AUTOINCREMENT,word VARCHAR,translate VARCHAR,initial VARCHAR,count INTEGER,is_collection INTEGER)";
	private static final String CREATE_COUNTER_TABLE="CREATE TABLE IF NOT EXISTS counter(_id INTEGER PRIMARY KEY AUTOINCREMENT,count INTEGER) ";
	private static DataBaseHelper instance;//单例类
    private SQLiteDatabase db;
    private Context context;
    
    //DataBaseHelper

	
    private DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    	this.db=getWritableDatabase();
    }

    //得到数据库帮助类
	public synchronized static DataBaseHelper getInstance(Context context) {
        if (instance == null) {
        	instance = new DataBaseHelper(context);
        }
        return instance;
	}
	//=======================SQLiteOpenHelper=========================
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CTRATE_DICTIONARY_TABLE);
		db.execSQL(CREATE_COUNTER_TABLE);
		try {
			init(db);
		} catch (IOException e) {
			throw new DataBaseInitException("数据库初始化失败");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }
	
	private void init(SQLiteDatabase db) throws IOException{
		String sql="INSERT INTO dictionary(word,translate,initial,count,is_collection) VALUES(?,?,?,?,?)";
		BufferedReader br=new BufferedReader(new InputStreamReader(context.getAssets().open("cet4.txt")));
		for(String line=br.readLine();line!=null;){
			String word=line.substring(0,line.indexOf(" "));
			String translate=line.substring(line.indexOf(" ")+1);
			String initial=(word.substring(0, 1));
			db.execSQL(sql, new Object[]{word,translate,initial,0,0});
			line=br.readLine();
		}
		db.execSQL("INSERT INTO counter(count) VALUES(0)");
	}
	//===========================DataBaseHelper================================
	public List<Word> getWordByCount(final int num){
		String sql="SELECT * FROM dictionary WHERE count=? LIMIT ?";
		List<Word> words=new ArrayList<>();
		WordMapper wm=new WordMapper();
		Counter counter=getCounter();
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(counter.getCount()),String.valueOf(num)});
		while(cursor.moveToNext()){
			words.add(wm.mapRow(cursor));
		}
		cursor.close();
		return words;
	}
	public List<Word> getWordsById(int[] ids){
		String sql="SELECT * FROM dictionary WHERE _id=?";
		List<Word> words=new ArrayList<>();
		WordMapper wm=new WordMapper();
		for(int id:ids){
			Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(id)});
			cursor.moveToFirst();
			words.add(wm.mapRow(cursor));
			cursor.close();
		}
		return words;
	}
	public Counter getCounter(){
		Cursor cursor=db.rawQuery("SELECT * FROM counter", null);
		while(cursor.moveToFirst()){
			Counter counter=new Counter();
			counter.setCount(cursor.getInt(cursor.getColumnIndex("count")));
			cursor.close();
			return counter;
		}
		cursor.close();
		return new Counter(0);
	}
	public void increaseCounter(){
		String sql="UPDATE counter SET count=count+1";
		db.execSQL(sql);
	}
	public void increaseWordCount(int wordId){
		String sql="UPDATE dictionary SET count=count+1 WHERE _id=?";
		db.execSQL(sql, new String[]{String.valueOf(wordId)});
	}
	public int getDictionarySize(){
		Cursor cursor=db.rawQuery("SELECT COUNT(*) FROM dictionary", null);
		cursor.moveToFirst();
		int size=cursor.getInt(cursor.getColumnIndex("COUNT(*)"));
		cursor.close();
		return size;
	}
	public List<Word> getCollectionWord(int page,int size){
		String sql="SELECT * FROM dictionary WHERE is_collection=1 limit ?,?";
		List<Word> words=new ArrayList<>();
		WordMapper wm=new WordMapper();
		Cursor cursor=db.rawQuery(sql, new String[]{String.valueOf(size*(page-1)),String.valueOf(size)});
		while(cursor.moveToNext()){
			words.add(wm.mapRow(cursor));
		}
		cursor.close();
		return words;
	}
	
	public void collectionWord(int id){
		String sql="UPDATE dictionary SET is_collection=1 WHERE _id=?";
		db.execSQL(sql,new String[]{String.valueOf(id)});
	}
	
	public void cancelCollectionWord(int id){
		String sql="UPDATE dictionary SET is_collection=0 WHERE _id=?";
		db.execSQL(sql,new String[]{String.valueOf(id)});
	}
	
}
