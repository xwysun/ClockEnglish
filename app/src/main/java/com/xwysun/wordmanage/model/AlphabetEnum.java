package com.xwysun.wordmanage.model;
/**
 * 字母表枚举
 * @author MagicWolf
 *
 */
public enum AlphabetEnum {
	A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z;
	
	public static AlphabetEnum of(String s){
		for(AlphabetEnum a:AlphabetEnum.values()){
			if(a.toString().equalsIgnoreCase(s)) return a;
		}
		return null;
	}
}
