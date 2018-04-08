package bbth.engine.sound;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.res.XmlResourceParser;
import bbth.engine.core.GameActivity;

/**
 * Parse an XML beat pattern
 * 
 * @author jardini
 * 
 */
public class BeatPatternParser {

	// parse an entire pattern into an array of beats
	public static Beat[] parse(int resourceId) {
		XmlResourceParser parser = GameActivity.instance.getResources().getXml(
				resourceId);
		Map<String, List<Beat>> patterns = new HashMap<String, List<Beat>>();
		List<Beat> song = new ArrayList<Beat>();
		float millisPerBeat = 0;

		try {
			int eventType = parser.next(); // skip start of document

			while (eventType != XmlPullParser.END_DOCUMENT) {
				if (eventType == XmlPullParser.START_TAG) {
					String name = parser.getName();
					if (name.equals("pattern")) { //$NON-NLS-1$
						// Log.d("BBTH", "Starting pattern"); //$NON-NLS-1$ //$NON-NLS-2$
						String id = parser.getAttributeValue(null, "id"); //$NON-NLS-1$
						patterns.put(id, parseSubpattern(parser, millisPerBeat));
						eventType = parser.getEventType();
					} else if (name.equals("song")) { //$NON-NLS-1$
						// Log.d("BBTH", "Starting song"); //$NON-NLS-1$ //$NON-NLS-2$
						song = parseSong(parser, patterns);
						eventType = parser.getEventType();
					} else if (name.equals("root")) { //$NON-NLS-1$
						String mpb = parser.getAttributeValue(null, "mpb"); //$NON-NLS-1$
						millisPerBeat = Float.parseFloat(mpb);
						eventType = parser.next();
					} else {
						eventType = parser.next();
					}
				} else {
					eventType = parser.next();
				}
			}

		} catch (XmlPullParserException e) {
			// Log.e("BBTH", "Error parsing beat track"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			// Log.e("BBTH", "Error parsing beat track"); //$NON-NLS-1$ //$NON-NLS-2$
		}

		Beat[] songArray = new Beat[song.size()];
		song.toArray(songArray);
		return songArray;
	}

	// parse a subpattern into a List of beats
	private static List<Beat> parseSubpattern(XmlResourceParser parser,
			float millisPerBeat) throws IOException, XmlPullParserException {
		ArrayList<Beat> beats = new ArrayList<Beat>();

		parser.next();
		String name = parser.getName();
		boolean patternOver = false;

		while (!patternOver) {
			if (parser.getEventType() == XmlPullParser.START_TAG) {

				String type = parser.getAttributeValue(null, "type"); //$NON-NLS-1$
				float duration;
				if (type != null) {
					duration = parseNoteType(type, millisPerBeat);
				} else {
					// check for manual duration entry
					String durationStr = parser.getAttributeValue(null,
							"duration"); //$NON-NLS-1$
					if (durationStr == null) {
						throw new XmlPullParserException("no type or duration"); //$NON-NLS-1$
					}
					duration = Float.parseFloat(durationStr);
				}

				parser.next();

				name = parser.getName();
				if (name.equals("rest")) { //$NON-NLS-1$
					beats.add(Beat.rest(duration));
				} else if (name.equals("hold")) { //$NON-NLS-1$
					beats.add(Beat.hold(duration));
				} else if (name.equals("beat")) { //$NON-NLS-1$
					beats.add(Beat.tap(duration));
				} else {
					patternOver = true;
				}
			} else if (parser.getEventType() == XmlPullParser.END_TAG) {
				// end of pattern tag
				patternOver = true;
			}

			// skip the end tag
			parser.next();
		}

		return beats;
	}

	// parse a note type
	private static float parseNoteType(String type, float millisPerBeat) {
		if (type.equals("sixteenth")) { //$NON-NLS-1$
			return millisPerBeat * 0.25f;
		}
		if (type.equals("eighth")) { //$NON-NLS-1$
			return millisPerBeat * 0.5f;
		}
		if (type.equals("quarter")) { //$NON-NLS-1$
			return millisPerBeat;
		}
		if (type.equals("half")) { //$NON-NLS-1$
			return millisPerBeat * 2.f;
		}
		if (type.equals("whole")) { //$NON-NLS-1$
			return millisPerBeat * 4.f;
		}
		if (type.equals("eighth_triplet")) { //$NON-NLS-1$
			return millisPerBeat / 3.f;
		}
		if (type.equals("quarter_triplet")) { //$NON-NLS-1$
			return millisPerBeat * 2.f / 3.f;
		}

		return millisPerBeat;
	}

	// parse the song
	private static List<Beat> parseSong(XmlResourceParser parser,
			Map<String, List<Beat>> patterns) throws IOException,
			XmlPullParserException {
		List<Beat> song = new ArrayList<Beat>();

		while (true) {
			parser.next();

			if (parser.getEventType() == XmlPullParser.START_TAG) {
				String name = parser.getName();
				if (name.equals("load-pattern")) { //$NON-NLS-1$
					String id = parser.getAttributeValue(null, "id"); //$NON-NLS-1$
					if (!patterns.containsKey(id)) {
						throw new XmlPullParserException("bad load-pattern tag"); //$NON-NLS-1$
					}

					// copy the pattern
					List<Beat> subpattern = patterns.get(id);
					for (int i = 0; i < subpattern.size(); ++i) {
						song.add(new Beat(subpattern.get(i).type, subpattern
								.get(i).duration));
					}

					// skip the end tag
					parser.next();
				} else {
					break;
				}
			} else if (parser.getEventType() == XmlPullParser.END_TAG) {
				// skip end of song tag
				parser.next();
				break;
			}
		}

		// set starting times
		float currTime = 0;
		for (int i = 0; i < song.size(); ++i) {
			song.get(i)._startTime = currTime;
			currTime += song.get(i).duration;
		}

		return song;
	}
}
