package bbth.game;


/**
 * Each song is made up of a song and a beat track
 * @author jardini
 *
 */
public enum Song {
	RETRO(R.raw.retrobit, R.xml.retro, -2, R.string.song_retro),
	MISTAKE_THE_GETAWAY(R.raw.mistakethegetaway, R.xml.mistake_the_getaway, -3, R.string.song_mistakethegetaway),
	JAVLA_SLADDAR(R.raw.javlasladdar_short, R.xml.javlasladdar, -4, R.string.song_javlasladdar),
	ODINS_KRAFT(R.raw.odinskraft_short, R.xml.odins_kraft, -5, R.string.song_odinskraft),
	MIGHT_AND_MAGIC(R.raw.mightandmagic_short, R.xml.might_and_magic, -6, R.string.song_mightandmagic);
	
	private Song(int song, int track, int id, int nameResourceId) {
		songId = song;
		trackId = track;
		this.nameResourceId = nameResourceId;
		
		this.id = id;
	}

	public final int id;
	public final int songId;
	public final int trackId;
	public final int nameResourceId;
	
	public static Song fromInt(int i) {
		switch (i) {
		case -2:
			return Song.RETRO;
		case -3:
			return Song.MISTAKE_THE_GETAWAY;
		case -4:
			return Song.JAVLA_SLADDAR;
		case -5:
			return Song.ODINS_KRAFT;
		case -6: 
			return Song.MIGHT_AND_MAGIC;

		default:
			return null;
		}
	}

	public int getNameResourceId() {
		return nameResourceId;
	}
}