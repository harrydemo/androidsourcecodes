/**
 * TorProxy - Anonymous data communication for Android devices
 * Copyright (C) 2009 Connell Gauld
 * 
 * Thanks to University of Cambridge,
 * 		Alastair Beresford and Andrew Rice
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA
 */


package uk.ac.cam.cl.dtg.android.tor.TorProxy;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class TorNotifications {
	
	private NotificationManager mNotifications = null;
	private Context mContext = null;
	private Notification n = null;
	private static final int CONNECTING_NOTIFICATION_ID = 1;
	private static final int WORKING_NOTIFICATION_ID = 2;
	private static final int OFF_NOTIFICATION_ID = 3;
	private static final String TITLE = "Anonymous data connection";
	private int mCurrentNotification = 0;
	
	public TorNotifications(Context c) {
		mContext = c;
		this.mNotifications = (NotificationManager)c.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	
	public synchronized void setNotificationOff() {
		mCurrentNotification = OFF_NOTIFICATION_ID;
		mNotifications.cancel(CONNECTING_NOTIFICATION_ID);
		mNotifications.cancel(WORKING_NOTIFICATION_ID);
		n = new Notification(R.drawable.onion_off, "Not available", System.currentTimeMillis());
		updateNotification(WORKING_NOTIFICATION_ID);
	}
	
	public synchronized void setNotificationConnecting(int countdown) {
		mCurrentNotification = CONNECTING_NOTIFICATION_ID;
		mNotifications.cancel(OFF_NOTIFICATION_ID);
		mNotifications.cancel(WORKING_NOTIFICATION_ID);
		n = new Notification(R.drawable.onion_connecting, "Connecting...", System.currentTimeMillis());
		n.number = countdown;
		updateNotification(CONNECTING_NOTIFICATION_ID);
	}
	
	public synchronized void setNotificationOn() {
		mCurrentNotification = WORKING_NOTIFICATION_ID;
		mNotifications.cancel(CONNECTING_NOTIFICATION_ID);
		mNotifications.cancel(OFF_NOTIFICATION_ID);
		n = new Notification(R.drawable.onion_working, "Available for use", System.currentTimeMillis());
		updateNotification(WORKING_NOTIFICATION_ID);
	}
	
	private void updateNotification(int id) {
		n.flags |= Notification.FLAG_NO_CLEAR;
		n.flags |= Notification.FLAG_ONGOING_EVENT;
		PendingIntent i = PendingIntent.getActivity(mContext, 0, new Intent(mContext, TorProxySettings.class), 0);
		n.setLatestEventInfo(mContext, TITLE, n.tickerText, i);
		mNotifications.notify(id, n);
	}
	
	public synchronized void hideNotification() {
		mCurrentNotification = 0;
		mNotifications.cancel(CONNECTING_NOTIFICATION_ID);
		mNotifications.cancel(WORKING_NOTIFICATION_ID);
		mNotifications.cancel(OFF_NOTIFICATION_ID);
	}
	
	public synchronized void updateCountdown(int i) {
		if (mCurrentNotification != CONNECTING_NOTIFICATION_ID) return;
		if (n == null) return;
		n.number = i;
		updateNotification(CONNECTING_NOTIFICATION_ID);
		//mNotifications.notify(CONNECTING_NOTIFICATION_ID, n);
	}
}
