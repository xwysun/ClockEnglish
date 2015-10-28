package com.xwysun.wordmanage.model.clock;

import java.io.Serializable;
import java.sql.Time;

public class Clock implements Serializable {
	private int id;
	private Time time;
	private WordNumber wordNumber;
	private Repeat repeat;
	private Ring ring;
	private int vibration;
	private String remark;
	public WordNumber getWordNumber() {
		return wordNumber;
	}
	public void setWordNumber(WordNumber wordNumber) {
		this.wordNumber = wordNumber;
	}
	public Repeat getRepeat() {
		return repeat;
	}
	public void setRepeat(Repeat repeat) {
		this.repeat = repeat;
	}
	public Ring getRing() {
		return ring;
	}
	public void setRing(Ring ring) {
		this.ring = ring;
	}
	public int getVibration() {
		return vibration;
	}
	public void setVibration(int vibration) {
		this.vibration = vibration;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Time getTime() {
		return time;
	}
	public void setTime(Time time) {
		this.time = time;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((remark == null) ? 0 : remark.hashCode());
		result = prime * result + ((repeat == null) ? 0 : repeat.hashCode());
		result = prime * result + ((ring == null) ? 0 : ring.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + vibration;
		result = prime * result
				+ ((wordNumber == null) ? 0 : wordNumber.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Clock other = (Clock) obj;
		if (id != other.id)
			return false;
		if (remark == null) {
			if (other.remark != null)
				return false;
		} else if (!remark.equals(other.remark))
			return false;
		if (repeat != other.repeat)
			return false;
		if (ring != other.ring)
			return false;
		if (time == null) {
			if (other.time != null)
				return false;
		} else if (!time.equals(other.time))
			return false;
		if (vibration != other.vibration)
			return false;
		if (wordNumber != other.wordNumber)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Clock [id=" + id + ", time=" + time + ", wordNumber="
				+ wordNumber + ", repeat=" + repeat + ", ring=" + ring
				+ ", vibration=" + vibration + ", remark=" + remark + "]";
	}
	
}
