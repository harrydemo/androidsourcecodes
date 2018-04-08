package my.android.karaoke.bean;

import java.util.List;

public class LyricSentence {
	
	private int sentence_id;
	private String type;
	private int sentence_offset;//一段歌词的起始音频位置
	private int sentence_duration;//一段歌词的起始音频位置
	private String sentence; //一段歌词文本
	
	private List<LyricWord> words;

	public int getSentence_id() {
		return sentence_id;
	}

	public void setSentence_id(int sentence_id) {
		this.sentence_id = sentence_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getSentence_offset() {
		return sentence_offset;
	}

	public void setSentence_offset(int sentence_offset) {
		this.sentence_offset = sentence_offset;
	}

	public int getSentence_duration() {
		return sentence_duration;
	}

	public void setSentence_duration(int sentence_duration) {
		this.sentence_duration = sentence_duration;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public List<LyricWord> getWords() {
		return words;
	}

	public void setWords(List<LyricWord> words) {
		this.words = words;
	}
}
