package com.stickycoding.rokon.audio;

import com.stickycoding.rokon.Debug;

/**
 * SoundFile.java
 * SoundFile contains sounds which have been loaded into the RokonAudio class.
 * 
 * @author Richard
 */
public class SoundFile {
        
        private int id;
        private int result;
        private AudioStream audioStream;
        private boolean res;
        
        public SoundFile(int streamId) {
                id = streamId;
        }
        
        public int getId() {
                return id;
        }
        
        /**
         * @return the AudioStream through which the sound is playing
         */
        public AudioStream play() {
        	if(RokonAudio.mute)
                        return null;
                result = RokonAudio.singleton.getSoundPool().play(id, RokonAudio.singleton.getMasterVolume(), RokonAudio.singleton.getMasterVolume(), 1, 0, 1f);
                if(result != 0) {
                        audioStream = new AudioStream(result, false, RokonAudio.singleton.getMasterVolume());
                        return audioStream;
                } else
                        return null;
        }
        
        /**
         * @return the AudioStream through which the sound is playing
         */
        public AudioStream playContinuous() {
                if(RokonAudio.mute)
                        return null;
                int result = RokonAudio.singleton.getSoundPool().play(id, RokonAudio.singleton.getMasterVolume(), RokonAudio.singleton.getMasterVolume(), 0, -1, 1);
                if(result != 0) {
                        audioStream = new AudioStream(result, true, RokonAudio.singleton.getMasterVolume());
                        return audioStream;
                } else
                        return null;
        }
        
        /**
         * Removes this file from the memory. Should be used when this file is no longer needed.
         */
        public void unload() {
                res = RokonAudio.singleton.getSoundPool().unload(id);
                Debug.print("Unloading " + id + " " + res);
        }
}