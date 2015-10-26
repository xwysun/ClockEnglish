package com.xwysun.wordmanage.model.clock;

public enum Ring {
	GETUP("GetUp"),GOODLUCK("GoodLuck"),GOODMORNING("GoodMorning");
	private Ring(String name) {
		this.name=name;
	}
	private String name;
	@Override
	public String toString() { return this.name; }
	
	public static Ring of(String n){
		for(Ring ring:Ring.values()){
			if(ring.name.equals(n)) return ring;
		}
		return null;
	}
}
