package com.sordid.particly;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;

public class Stats implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5291444254469386555L;
	public static final String PREFS_NAME = "ParticlyPref";
	public String FILENAME;
	public String mFileExtra;
	public int mCollected = 0;
	
	//private Activity mParent;
	private int mMaxLevel = 1;
	private final HashMap<Integer,StatsLevel> mLevels = new HashMap<Integer,StatsLevel>();
	
	public Stats(final Activity pParent, final String chapterFile, final String fileExtra) {
		FILENAME = chapterFile+fileExtra;
		mFileExtra = fileExtra;
	}
	
	public void setFile(final String chapterFile, final String fileExtra) {
		FILENAME = chapterFile+fileExtra;
		mFileExtra = fileExtra;
	}
	
	// Saves the current level to both local copy and storage
	public void saveLevel(final Integer pLevel, final int pAttempts, final long pTime, final HashMap<Integer,Boolean> pWayPoints) {
		final StatsLevel level;
		if(mLevels.containsKey(pLevel)) {
			level = mLevels.get(pLevel);
		} else {
			level = new StatsLevel();
		}

		level.setAttemps(pAttempts);
		level.setTime(pTime);
		level.setWayPoints(pWayPoints); 
		
		mLevels.put(pLevel, level);
	}
	
	public void saveObject(Stats obj, Context c) {
		FileOutputStream fos = null;
		ObjectOutputStream oos  = null;

        try {
        	fos = c.openFileOutput(obj.FILENAME, Context.MODE_PRIVATE);
        	oos = new ObjectOutputStream(fos);
        	oos.writeObject(obj);
        }
        catch (Exception e) { Slog.i("Physics", e.getMessage()+e.toString()+e.getStackTrace()); }
        finally {
            try {
                if (oos != null)   oos.close();
                if (fos != null)   fos.close();
            }
            catch (Exception e) { Slog.i("Physics", e.getMessage()+e.toString()+e.getStackTrace()); }
        }
    }
	
	public Stats readObject(Context c)
    {
        Stats sc = null;
        FileInputStream fis = null;
        ObjectInputStream is = null;
  
        try {
            fis = c.openFileInput(FILENAME);
            is = new ObjectInputStream(fis);
            sc = (Stats) is.readObject();
            for (Map.Entry<Integer, StatsLevel> entry : sc.mLevels.entrySet()) {
            	mCollected += entry.getValue().getWayPointsCollected();
			}
        }
        catch(Exception e){ Slog.i("Physics", e.getMessage()+e.toString()+e.getStackTrace()); }
        finally {
        	try {
               if (fis != null)   fis.close();
               if (is != null)   is.close();
           }
           catch (Exception e) { Slog.i("Physics", e.getMessage()+e.toString()+e.getStackTrace()); }
        }

        return sc;
    }
	
	// Gets the current level information if we have it
	public void getLevel(final Integer pLevel, final HashMap<Integer,Boolean> pWayPoints) {
		if(mLevels.containsKey(pLevel)) {
			final StatsLevel level = mLevels.get(pLevel);
			level.mapWayPoints(pWayPoints);
		}
	}
	
	// Retrieves the level
	public StatsLevel getLevel(final Integer pLevel) {
		if(mLevels.containsKey(pLevel)) {
			return mLevels.get(pLevel);
		}
		return null;
	}
	
	public boolean setMaxLevel(int level) {
		if(level > mMaxLevel) {
			mMaxLevel = level;
			return true;
		}
		return false;
	}
	
	public int getMaxLevel() {
		return mMaxLevel;
	}
	
	// Sub-class for storing level-specific items
	public class StatsLevel implements Serializable {
		private static final long serialVersionUID = 7935289013433320093L;
		private int mAttempts = -1;
		private long mTime = -1;
		private HashMap<Integer,Boolean> mWayPoints = new HashMap<Integer,Boolean>();
		
		public StatsLevel() {}
		public StatsLevel(final int pAttempts, final long pTime, final HashMap<Integer,Boolean> pWayPoints) {
			mAttempts = pAttempts;
			mTime = pTime;
			mWayPoints = pWayPoints;
		}
		
		public void setAttemps(int pAttempts) {
			if(mAttempts <= 0 || pAttempts <= mAttempts){
				mAttempts = pAttempts;
			};
		}
		public int getAttemps() {
			return this.mAttempts;
		}
		
		public void setTime(long pTime) {
			if(mAttempts == -1 || pTime < mTime) {
				mTime = pTime;
			}
		}
		public long GetTime() {
			return this.mTime;
		}
		
		public void setWayPoints(final HashMap<Integer,Boolean> pWayPoints) {
			for (Map.Entry<Integer, Boolean> entry : pWayPoints.entrySet()) {
				if(mWayPoints.containsKey(entry.getKey()) && !mWayPoints.get(entry.getKey()) && entry.getValue())
				{
					mCollected++;
				}
				mWayPoints.put(entry.getKey(), entry.getValue());
			}
		}
		public HashMap<Integer,Boolean> GetWayPoints() {
			return this.mWayPoints;
		}
		public void mapWayPoints(HashMap<Integer,Boolean> pWayPoints) {
			for (Map.Entry<Integer, Boolean> entry : mWayPoints.entrySet()) {
				pWayPoints.put(entry.getKey(), entry.getValue());
			}
		}
		public int getWayPointsCollected() {
			int c = 0;
			for (Map.Entry<Integer, Boolean> entry : mWayPoints.entrySet()) {
				if(entry.getValue()) {
					c++;
				}
			}
			return c;
		}
	}
}