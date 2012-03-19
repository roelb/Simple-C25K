/* 
 * This Class is used for setting an alarm. When the alarm goes of a defined message
 * (MESSAGE) will be send to the notification bar.
 * 
 * Author: Roel Blaauwgeers
 * 13-03-2012
 * 
 * Changelog:
 * 
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
		message = intent.getStringExtra("MESSAGE"); //not needed... for now

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
