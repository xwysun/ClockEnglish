package com.xwysun.wordmanage;

import java.util.List;

import android.content.Context;

import com.xwysun.wordmanage.dao.ClockDataBaseHelper;
import com.xwysun.wordmanage.model.clock.Clock;

public class ClockManage {
	private Context context;
	private ClockDataBaseHelper dataBaseHelper;
	
	public ClockManage(Context context) {
		this.context = context;
		dataBaseHelper = ClockDataBaseHelper.getInstance(context);
	}
	/**
	 * 得到所有闹钟
	 * @return
	 */
	public List<Clock> getClocks(){
		return dataBaseHelper.getClocks();
	}
	/**
	 * 通过Id得到闹钟
	 * @param id
	 * @return
	 */
	public Clock getClockById(int id){
		return dataBaseHelper.getClockById(id);
	}
	/**
	 * 设置闹钟
	 * @param clock
	 */
	public void setClock(Clock clock){
		dataBaseHelper.updateClock(clock);
	}
	
	/**
	 * 添加闹钟
	 */
	public void addClock(){
		dataBaseHelper.addDefaultClock();
	}
	
	public void addClock(Clock clock){
		dataBaseHelper.addClock(clock);
	}
	public void deleteClock(int id){
		dataBaseHelper.deleteClock(id);
	}
	public void deleteClocks(List<Clock> clockList){
		int i ;
		for(i=0;i<clockList.size();i++){
			deleteClock(clockList.get(i).getId());
		}
	}
}
