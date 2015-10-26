package com.xwysun.wordmanage.model.clock;

public enum Repeat {
	ONLY_ONE("只响一次"),EVERY_DAY("每天"),MON2FIR("周一至周五");
	private Repeat(String value) {
		this.value=value;
	}
	private String value;
	@Override
	public String toString() {
		return this.value;
	}
	public static Repeat of(String r){
		for(Repeat repeat:Repeat.values()){
			if(repeat.value.equals(r)) return repeat;
		}
		return null;
	}
}
