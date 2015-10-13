package com.xwysun.wordmanage.dao.mapper;

import android.database.Cursor;

public interface RowMapper<T> {
	public T mapRow(Cursor cursor);
}
