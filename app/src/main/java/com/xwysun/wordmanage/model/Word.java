package com.xwysun.wordmanage.model;

import java.io.Serializable;

/**
 * 单词类
 * @author MagicWolf
 *
 */
public class Word implements Serializable{
	private static final long serialVersionUID = 1L;
	private int id;
	private String word;
	private String translate;
	private AlphabetEnum initial;
	private int count;
	private int isCollection;
	
	public Word() { }
	public Word(int id, String word, String translate, AlphabetEnum initial,
			int count) {
		this.id=id;
		this.word = word;
		this.translate = translate;
		this.initial = initial;
		this.count = count;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getTranslate() {
		return translate;
	}
	public void setTranslate(String translate) {
		this.translate = translate;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public AlphabetEnum getInitial() {
		return initial;
	}

	public void setInitial(AlphabetEnum initial) {
		this.initial = initial;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(int isCollection) {
		this.isCollection = isCollection;
	}
	@Override
	public String toString() {
		return "Word [id=" + id + ", word=" + word + ", translate=" + translate
				+ ", initial=" + initial + ", count=" + count
				+ ", isCollection=" + isCollection + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + count;
		result = prime * result + id;
		result = prime * result + ((initial == null) ? 0 : initial.hashCode());
		result = prime * result + isCollection;
		result = prime * result
				+ ((translate == null) ? 0 : translate.hashCode());
		result = prime * result + ((word == null) ? 0 : word.hashCode());
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
		Word other = (Word) obj;
		if (count != other.count)
			return false;
		if (id != other.id)
			return false;
		if (initial != other.initial)
			return false;
		if (isCollection != other.isCollection)
			return false;
		if (translate == null) {
			if (other.translate != null)
				return false;
		} else if (!translate.equals(other.translate))
			return false;
		if (word == null) {
			if (other.word != null)
				return false;
		} else if (!word.equals(other.word))
			return false;
		return true;
	}
	
}
