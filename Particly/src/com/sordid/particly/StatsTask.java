package com.sordid.particly;
import android.content.Context;
import android.os.AsyncTask;

public class StatsTask extends AsyncTask<Object, Void, Void> {
	@Override
	protected Void doInBackground(Object... arg0) {
        ParticlyActivity.sStats.saveObject((Stats)arg0[0], (Context)arg0[1]);
		return null;
	}
}