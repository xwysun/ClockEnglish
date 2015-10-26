package com.xwysun.wordmanage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.content.Context;

import com.xwysun.wordmanage.dao.DataBaseHelper;
import com.xwysun.wordmanage.model.Question;
import com.xwysun.wordmanage.model.Word;


public class WordManage {

	private static final int DEFALUT_SIZE = 5;
	private Context context;
	private DataBaseHelper dataBaseHelper;
	private int wordSize =0;

	public void setWordSize(int size){
		this.wordSize=size;
	}
	public int getWordSize(){
		if (wordSize==0){
			return DEFALUT_SIZE;
		}
		return wordSize;
	}

	public WordManage(Context context) {
		this.context = context;
		dataBaseHelper = DataBaseHelper.getInstance(context);
	}
	/**
	 * 得到5个题目
	 * @return
	 */
	public List<Question> getQuestions() {
		return getQuestion(getWordSize());
	}
	/**
	 * 得到一组题目
	 * @param num 题目数量
	 * @return
	 */
	public List<Question> getQuestions(int num) {
		return getQuestion(num);
	}
	/**
	 * 获取收藏单词，
	 * @param page 页数 从1开始
	 * @return
	 */
	public List<Word> getCollectionWord(int page){
		if(page<0) page=0;
		return dataBaseHelper.getCollectionWord(page, 10);
	} 
	/**
	 * 收藏单词
	 * @param id
	 */
	public void collectionWord(int id){
		dataBaseHelper.collectionWord(id);
	}

	public void cancelCollectionWord(int id){
		dataBaseHelper.cancelCollectionWord(id);
	}
	public List<Question> getLearnQuestion(){
		return getLearnQuestion(DEFALUT_SIZE);
	}


	private List<Question> getLearnQuestion(int num) {
		List<Question> qList = new ArrayList<>();
		int size = dataBaseHelper.getDictionarySize();
		List<Word> words = dataBaseHelper.getWordByCount(num);
		while (words.size() < num) {
			words.addAll(dataBaseHelper.getWordByCount(num - words.size()));
		}
		for (int i=0;i<words.size();i++) {
			int[] worngIds = randomId(3, 1, size, words.get(i).getId());
//			dataBaseHelper.increaseWordCount(words.get(i).getId());
			List<Word> worngs = dataBaseHelper.getWordsById(worngIds);
			qList.add(new Question(words.get(i), worngs));
		}
		return qList;
	}



	private List<Question> getQuestion(int num) {
		List<Question> qList = new ArrayList<>();
		int size = dataBaseHelper.getDictionarySize();
		List<Word> words = dataBaseHelper.getWordByCount(num);
		while (words.size() < num) {
			dataBaseHelper.increaseCounter();// 单词循环次数加1
			words.addAll(dataBaseHelper.getWordByCount(num - words.size()));
		}
		for (int i=0;i<words.size();i++) {
			int[] worngIds = randomId(3, 1, size, words.get(i).getId());
			dataBaseHelper.increaseWordCount(words.get(i).getId());
			List<Word> worngs = dataBaseHelper.getWordsById(worngIds);
			qList.add(new Question(words.get(i), worngs));
		}
		return qList;
	}

	private static int random(int min, int max) {
		return new Random().nextInt(max) % (max - min + 1) + min;
	}

	private static int[] randomId(int num, int begin, int end, int ingore) {
		if (end - begin + 1 < num){
			num = end - begin + 1;// check
		}
		System.out.println("num"+num);
		int[] arr = new int[num];
		byte[] flag = new byte[end - begin + 1];
		for (int i = 0; i < arr.length; i++) {
			int random = random(begin, end);
			while (ingore != 0&&random == ingore){
				System.out.println(random);
				random = random(begin, end);
			}
			System.out.println(random);
			while (flag[random - begin] != 0||random == ingore) {
				random =random+1>end?begin:random+1;
			}
			arr[i] = random;
			flag[random - begin] = 1;
		}
		return arr;
	}
}
