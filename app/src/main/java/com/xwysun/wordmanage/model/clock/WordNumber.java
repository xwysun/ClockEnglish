package com.xwysun.wordmanage.model.clock;

public enum WordNumber {
	FIVE(5),TEN(10),FIFTEEN(15);
	private WordNumber(int n) {
		this.number=n;
	}
	private int number;
	
	@Override
	public String toString() { return String.valueOf(this.number); }
	
	public static WordNumber of(int n){
		for(WordNumber w:WordNumber.values()){
			if(w.number==n) return w;
		}
		return null;
	}
}
