/* //device/samples/SampleCode/src/com/android/samples/app/RemoteServiceInterface.java
**
** Copyright 2007, The Android Open Source Project
**
** Licensed under the Apache License, Version 2.0 (the "License"); 
** you may not use this file except in compliance with the License. 
** You may obtain a copy of the License at 
**
**     http://www.apache.org/licenses/LICENSE-2.0 
**
** Unless required by applicable law or agreed to in writing, software 
** distributed under the License is distributed on an "AS IS" BASIS, 
** WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
** See the License for the specific language governing permissions and 
** limitations under the License.
*/

package com.jameschen.movie.service;
import android.graphics.Bitmap;
interface IMediaPlaybackService
{	
	void saveProgress(long pos);
    void setPlayPosition(int pos);
    void setMaxPosition(int max);
    void setPlayMode(int mode);
    void refreashList();
    void openFile(String path);
    void findTheSong(boolean reLoad,boolean showToast);
    boolean isPlaying();
    void stop();
    void pause();
    void play();
    boolean noMusic();
    void playByIndex(int index,boolean play,boolean seekToBegin);
    void prev();
    void next();
    int getCurrentPlayIndex();
    int getMaxMusicLen();
    long duration();
    long getAlbumId();
    long position();
    long seek(long pos);
    long[] getLyricTime();
    String getTrackName();
    String getSongInfo();
    String[] getLyric(long time);
    String getArtistName();
    void setQueuePosition(int index);
    String getPath();
    int getAudioSessionId();
}

