/*
 * MyAlarmService.java
 * 
 * Copyright 2012 Blaauwgeers <rs.blaauwgeers@gmail.com>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 *
 * info:
 * This Class is used for setting an alarm. When the alarm goes of a defined message
 * (MESSAGE) will be send to the notification bar.
 */

package nl.ttys0.simplec25k;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Vibrator;

public class MyAlarmService extends Service {

	protected static final String MY_ACTION = "MY_ACTION";
	public static String message;

	@Override
	public void onCreate() {
		// Stop the service
		// to prevent random alerts after this service has run.
		this.stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;

	}

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	@Override
	public void onStart(Intent intent, int startId) {

		super.onStart(intent, startId);

		// retrieve workout info
		// This needs to be in onStart to be able to retrieve the data
		message = intent.getStringExtra("MESSAGE"); // not needed... for now

		MediaPlayer mp = MediaPlayer.create(MyAlarmService.this, R.raw.beep);
		mp.start();

		// setup vibrator
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pat = { 0, 700, 700 };
		v.vibrate(pat, -1);

	}

	@Override
	public boolean onUnbind(Intent intent) {

		return super.onUnbind(intent);

	}

}
