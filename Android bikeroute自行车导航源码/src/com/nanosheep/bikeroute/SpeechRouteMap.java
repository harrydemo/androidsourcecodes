/**
 * 
 */
package com.nanosheep.bikeroute;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.MenuItem;
import com.nanosheep.bikeroute.utility.Convert;
import com.nanosheep.bikeroute.utility.route.Segment;

import java.util.Iterator;

/**
 * Speaking route map.
 * 
 * This file is part of BikeRoute.
 * 
 * Copyright (C) 2011  Jonathan Gray
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 * 
 * @author jono@nanosheep.net
 * @version Oct 6, 2010
 */
public class SpeechRouteMap extends RouteMap implements OnInitListener {
	
	/** TTS enabled. **/
	protected boolean tts;
	/** TTS. **/
	protected TextToSpeech directionsTts;

	@Override
	public void onCreate(final Bundle savedState) {
		super.onCreate(savedState);
	}
	
	/**
	 * Handle jump intents from directionsview.
	 */
	
	@Override
	public void onNewIntent(final Intent intent) {
		super.onNewIntent(intent);
		if (intent.getBooleanExtra(getString(R.string.jump_intent), false)) {
			speak(app.getSegment());
		}
	}
	
	@Override
	public void onResume() {
		tts = mSettings.getBoolean("tts", false);
		//Initialize tts if in use.
        if (tts) {
        	directionsTts = new TextToSpeech(this, this);
        }
        super.onResume();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (directionsTts != null) {
			directionsTts.shutdown();
		}
	}
	
	/**
	 * Handle option selection.
	 * Speak first step of directions if turnbyturn selected.
	 * @return true if option selected.
	 */
	@Override
	public boolean onOptionsItemSelected(final MenuItem item) {
		if (tts && (item.getItemId() == R.id.turnbyturn)) {
			speak(app.getSegment());
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	public void nextStep() {
		super.nextStep();
		speak(app.getSegment());
	}
	
	@Override
	public void lastStep() {
		super.lastStep();
		speak(app.getSegment());
	}
	
	@Override
	public void hideStep() {
		super.hideStep();
		if (tts) {
			directionsTts.stop();
		}
	}
	
	/**
	 * Construct and speak a directions string for the segment.
	 * @param segment the segment to speak directions for.
	 */
	
	public void speak(final Segment segment) {
		if (tts && segment != null) {
			Iterator<Segment> it = app.getRoute().getSegments().listIterator(
					app.getRoute().getSegments().indexOf(segment) + 1);
			StringBuffer sb = new StringBuffer(segment.getInstruction().replaceAll("<(.*?)*>", ""));
			if (it.hasNext()) {
				sb.append(" then after ");
				if (unit.equals(getString(R.string.km))) {
					sb.append(segment.getLength());
					sb.append("meters ");
				} else {
					sb.append(Convert.asFeet(segment.getLength()));
					sb.append("feet ");
				}
				sb.append(it.next().getInstruction().replaceAll("<(.*?)*>", ""));
			}
			if (directionsTts.isSpeaking()) {
				sb.insert(0, " then");
			}	
			directionsTts.speak(sb.toString(), TextToSpeech.QUEUE_ADD, null);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.speech.tts.TextToSpeech.OnInitListener#onInit(int)
	 */
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
