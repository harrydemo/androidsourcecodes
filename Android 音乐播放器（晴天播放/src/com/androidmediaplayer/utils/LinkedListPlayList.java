package com.androidmediaplayer.utils;

import java.util.Collection;
import java.util.LinkedList;

import android.app.Application;

import com.androidmediaplayer.model.Mp3Info;

/**
 * 临时播放列表类
 * 更好的做法是继承application
 * @author Alex
 *
 */
public class LinkedListPlayList extends Application{
	
	public static int playListId = -1;
	public static int listPointer = 0;
	
	private static LinkedList<Mp3Info> playListMp3Infos = new LinkedList<Mp3Info>();
	
	public static void add(Mp3Info mp3Info){
		synchronized (playListMp3Infos) {
			playListMp3Infos.add(mp3Info);
		}
	}
	
	public static Mp3Info get(int index){
		synchronized (playListMp3Infos) {
			return playListMp3Infos.get(index);
		}
	}
	
	public static Mp3Info remove(int index){
		synchronized (playListMp3Infos) {
			return playListMp3Infos.remove(index);
		}
	}
	
	public static Mp3Info getFirst(){
		synchronized (playListMp3Infos) {
			return playListMp3Infos.getFirst();
		}
	}
	
	public static LinkedList<Mp3Info> getPlayListMp3Infos(){
		return playListMp3Infos;
	}
	
	public static void clear(){
		synchronized (playListMp3Infos) {
			playListMp3Infos.clear();
		}
	}
	
	public static void addAll(Collection<Mp3Info> c){
		synchronized (playListMp3Infos) {
			playListMp3Infos.addAll(c);
		}
	}
	
	public static int size(){
		synchronized (playListMp3Infos) {
			return playListMp3Infos.size();
		}
	}
	
	public static Mp3Info getLast(){
		synchronized (playListMp3Infos) {
			return playListMp3Infos.getLast();
		}
	}
	
}
