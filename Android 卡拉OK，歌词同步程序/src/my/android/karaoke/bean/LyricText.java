package my.android.karaoke.bean;

import java.util.List;

public class LyricText {
	
	private int id;
	
	private String lyric_ti;
	private String lyric_al;
	private String lyric_ar;
	private String lyric_by;
	private String lyric_offset;
	private String sound_path; //声音文件所在路径
	private String sound_time; //LRC时间标签
	
	private List<LyricSentence> sentences;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSound_path() {
		return sound_path;
	}

	public void setSound_path(String sound_path) {
		this.sound_path = sound_path;
	}

	public String getSound_time() {
		return sound_time;
	}

	public void setSound_time(String sound_time) {
		this.sound_time = sound_time;
	}

	public String getLyric_ti() {
		return lyric_ti;
	}

	public void setLyric_ti(String lyric_ti) {
		this.lyric_ti = lyric_ti;
	}

	public String getLyric_al() {
		return lyric_al;
	}

	public void setLyric_al(String lyric_al) {
		this.lyric_al = lyric_al;
	}

	public String getLyric_ar() {
		return lyric_ar;
	}

	public void setLyric_ar(String lyric_ar) {
		this.lyric_ar = lyric_ar;
	}

	public String getLyric_by() {
		return lyric_by;
	}

	public void setLyric_by(String lyric_by) {
		this.lyric_by = lyric_by;
	}

	public String getLyric_offset() {
		return lyric_offset;
	}

	public void setLyric_offset(String lyric_offset) {
		this.lyric_offset = lyric_offset;
	}

	public List<LyricSentence> getSentences() {
		return sentences;
	}

	public void setSentences(List<LyricSentence> sentences) {
		this.sentences = sentences;
	}
}
