package com.xwysun.wordmanage.model;

import java.io.Serializable;
import java.util.List;

/**
 * 题目类
 * @author MagicWolf
 *
 */
public class Question implements Serializable{
	private Word word;
	private List<Word> wrongs;
	public Word getWord() {
		return word;
	}
	public void setWord(Word word) {
		this.word = word;
	}
	public List<Word> getWrongs() {
		return wrongs;
	}
	public void setWrongs(List<Word> wrongs) {
		this.wrongs = wrongs;
	}
	public Question(Word word, List<Word> wrongs) {
		this.word = word;
		this.wrongs = wrongs;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((word == null) ? 0 : word.hashCode());
		result = prime * result + ((wrongs == null) ? 0 : wrongs.hashCode());
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
		Question other = (Question) obj;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		if (wrongs == null) {
			if (other.wrongs != null)
				return false;
		} else if (!wrongs.equals(other.wrongs))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Question [word=" + word + ", wrongs=" + wrongs + "]";
	}
	
	
}
