package my.android.karaoke.bean;

public class LyricWord {
	
	private int sentence_id;
	private int word_id;
	private int word_offset;
	private int word_duration;
	private String word;
	
	public int getSentence_id() {
		return sentence_id;
	}
	public void setSentence_id(int sentence_id) {
		this.sentence_id = sentence_id;
	}
	public int getWord_id() {
		return word_id;
	}
	public void setWord_id(int word_id) {
		this.word_id = word_id;
	}
	public int getWord_offset() {
		return word_offset;
	}
	public void setWord_offset(int word_offset) {
		this.word_offset = word_offset;
	}
	public int getWord_duration() {
		return word_duration;
	}
	public void setWord_duration(int word_duration) {
		this.word_duration = word_duration;
	}
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
}
