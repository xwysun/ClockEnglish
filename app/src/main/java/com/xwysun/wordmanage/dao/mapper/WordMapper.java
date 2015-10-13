package com.xwysun.wordmanage.dao.mapper;

import android.database.Cursor;

import com.xwysun.wordmanage.model.AlphabetEnum;
import com.xwysun.wordmanage.model.Word;


public class WordMapper implements RowMapper<Word>{

	@Override
	public Word mapRow(Cursor cursor) {
		Word word=new Word();
		word.setCount(cursor.getInt(cursor.getColumnIndex("count")));
		word.setId(cursor.getInt(cursor.getColumnIndex("_id")));
		word.setInitial(AlphabetEnum.of(cursor.getString(cursor.getColumnIndex("initial"))));
		word.setTranslate(cursor.getString(cursor.getColumnIndex("translate")));
		word.setWord(cursor.getString(cursor.getColumnIndex("word")));
		word.setIsCollection(cursor.getInt(cursor.getColumnIndex("is_collection")));
		return word;
	}
	
}
