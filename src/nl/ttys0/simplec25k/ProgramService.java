/*
 * ProgramService.java
 * 
 * Copyright 2012 Roel Blaauwgeers <rs.blaauwgeers@gmail.com>
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
 */

package nl.ttys0.simplec25k;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.Vibrator;

public class ProgramService extends Service {
	private static final String TAG = "MyService";
	protected static final String MY_ACTION = "MY_ACTION";
	Context context = this;
	private String selectedProgram;
	Thread workoutThread;

	WakeLock mWakeLock;

	Boolean finished = false;

	static final String SCHEDULEFILE = "schedule";

	// these two are used to check if this service should pause or stop
	private boolean running, paused;

	// pi is used for setting the alarm. It should be known troughout this class
	// to enable us to cancell the alarm.
	PendingIntent pi;

	MyReceiver myReceiver;

	Intent thisIntent;
	private AlarmManager alarmManager;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

		// Register BroadcastReceiver
		// to receive event from our service
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MyAlarmService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		// Toast.makeText(this, "program Service Started", Toast.LENGTH_LONG)
		// .show();
		// Log.d(TAG, "onCreate");

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
		mWakeLock.acquire();

	}

	@Override
	public void onDestroy() {
		// Toast.makeText(this, "program Service destroyed", Toast.LENGTH_LONG)
		// .show();
		// Log.d(TAG, "onDestroy");

		mWakeLock.release();

		workoutThread.interrupt();
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startid) {
		// retrieve workout info
		selectedProgram = intent.getStringExtra("INIT_DATA");

		workoutThread = new Thread() {
			@Override
			public void run() {

				startWorkout();
			}
		};
		workoutThread.start();

	}

	// ============================================================
	// ROUTINES
	// ============================================================

	private void startWorkout() {

		Intent myIntent = new Intent();
		myIntent.setAction(MY_ACTION);
		myIntent.putExtra("DATA_TO_TA", "STARTED");
		sendBroadcast(myIntent);

		running = true;
		paused = false;

		// w1d1, w1d2, w1d3
		if (selectedProgram.contains("w1")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			// 60+90=2.5minutes, so do this 6 times for a total of 15
			for (int i = 0; i < 6; i++) {
				// jogging 60s
				if (running)
					countdown(60, 500, "Jogging", "Jog for ");
				// walking 90s
				if (running)
					countdown(90, 500, "Walking", "Walk for ");
			}
		}

		// w2
		else if (selectedProgram.contains("w2")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			// 90+120=3.5minutes, so do this 4 times
			for (int i = 0; i < 4; i++) {
				// jogging 90s
				if (running)
					countdown(90, 500, "Jogging", "Jog for ");
				// walking 120s
				if (running)
					countdown(120, 500, "Walking", "Walk for ");
			}
			// fill one more minute 'cause 4*3.5=14
			if (running)
				countdown(60, 500, "Jogging", "Jog for ");
		}

		// w3
		else if (selectedProgram.contains("w3")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			// 2 times
			for (int i = 0; i < 2; i++) {
				// jogging 90s
				if (running)
					countdown(90, 500, "Jogging", "Jog for ");
				// walking 90s
				if (running)
					countdown(90, 500, "Walking", "Walk for ");
				// jogging 3m
				if (running)
					countdown(180, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
			}
		}

		// w4
		else if (selectedProgram.contains("w4")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			// jogging 3m
			if (running)
				countdown(180, 500, "Jogging", "Jog for ");
			// walking 90s
			if (running)
				countdown(90, 500, "Walking", "Walk for ");
			// jogging 5m
			if (running)
				countdown(300, 500, "Jogging", "Jog for ");
			// walking 2.5m
			if (running)
				countdown(150, 500, "Walking", "Walk for ");
			// jogging 3m
			if (running)
				countdown(180, 500, "Jogging", "Jog for ");
			// walking 90s
			if (running)
				countdown(90, 500, "Walking", "Walk for ");
			// jogging 5m
			if (running)
				countdown(300, 500, "Jogging", "Jog for ");

		}

		// w5
		else if (selectedProgram.contains("w5")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			if (selectedProgram.equals("w5d1")) {
				// jogging 5m
				if (running)
					countdown(300, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
				// jogging 5m
				if (running)
					countdown(300, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
				// jogging 5m
				if (running)
					countdown(300, 500, "Jogging", "Jog for ");

			}

			if (selectedProgram.equals("w5d2")) {
				// jogging 8m
				if (running)
					countdown(480, 500, "Jogging", "Jog for ");
				// walking 5m
				if (running)
					countdown(300, 500, "Walking", "Walk for ");
				// jogging 8m
				if (running)
					countdown(480, 500, "Jogging", "Jog for ");
			}

			if (selectedProgram.equals("w5d3")) {
				// jogging 20m
				if (running)
					countdown(1200, 500, "Jogging", "Jog for ");

			}
		}

		// w6
		else if (selectedProgram.contains("w6")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");

			if (selectedProgram.equals("w6d1")) {
				// jogging 5m
				if (running)
					countdown(300, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
				// jogging 8m
				if (running)
					countdown(480, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
				// jogging 5m
				if (running)
					countdown(300, 500, "Jogging", "Jog for ");

			} else if (selectedProgram.equals("w6d2")) {
				// jogging 10m
				if (running)
					countdown(600, 500, "Jogging", "Jog for ");
				// walking 3m
				if (running)
					countdown(180, 500, "Walking", "Walk for ");
				// jogging 10m
				if (running)
					countdown(600, 500, "Jogging", "Jog for ");

			} else if (selectedProgram.equals("w6d3")) {
				// jogging 22m
				if (running)
					countdown(1320, 500, "Jogging", "Jog for ");

			}

		}

		else if (selectedProgram.contains("w7")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");
			// jogging 25m
			if (running)
				countdown(1500, 500, "Jogging", "Jog for ");

		}

		else if (selectedProgram.contains("w8")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");
			// jogging 28m
			if (running)
				countdown(1680, 500, "Jogging", "Jog for ");

		}

		// w9d3
		else if (selectedProgram.contains("w9")) {
			// warmup 5m
			if (running)
				countdown(5 * 60, 500, "Warmup", "Warmup: Brisk walk for ");
			// jogging 30m
			if (running)
				countdown(1800, 500, "Jogging", "Jog for ");
		}

		workoutFinished();
	}

	// ============================================================
	// END OF ROUTINES
	// ============================================================

	private void workoutFinished() {
		WorkoutFileEditor wfe = new WorkoutFileEditor(this);

		// if running is still true, we can safely assume the workout is
		// completed. If running == false, the workout is interrupted by the
		// user and should not be marked 'done'
		if (running) {

			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			MediaPlayer mp = MediaPlayer
					.create(ProgramService.this, R.raw.beep);
			mp.start();

			// let the user know we're done
			// setup vibrator
			Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
			long[] pat = { 0, 700, 400 };
			v.vibrate(pat, -1);

			// update file
			String s = wfe.ReadSettings(SCHEDULEFILE);
			s = s.replace(selectedProgram + ";false", selectedProgram + ";true");

			wfe.WriteSettings(SCHEDULEFILE, s, MODE_PRIVATE);

			// alert main to update its array
			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_MAIN", "UPDATED");
			sendBroadcast(myIntent);

			// inform the timerActivity that the workout is done
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_TA", "DONE");
			sendBroadcast(myIntent);

		}
		// stop this service
		this.stopSelf();

	}

	// counts down and sends message
	// 'time' in seconds, 'interval' in ms
	// make sure interval is never set on 0, this will
	// overload the statusbar
	// returns false if the user pressed stop
	private boolean countdown(int time, int interval, String workout,
			String message) {
		long syst = System.currentTimeMillis();
		long systFinished = syst + time * 1000;
		long msUntilFinished = +systFinished - syst;
		int secondsUntilFinished = (int) ((systFinished - syst) / 1000);

		int seconds = secondsUntilFinished % 60;
		int minutes = secondsUntilFinished / 60;

		// set alarm
		startTimer(time * 1000, 0, "");

		// Send data for the GUI
		Intent myIntent = new Intent();
		myIntent.setAction(MY_ACTION);
		myIntent.putExtra("DATA_TO_TA", "SET_CURRENT;" + workout + ";"
				+ (time * 1000));
		sendBroadcast(myIntent);

		// dialog.cancel();

		// send initial notification
		sendNotification(workout, message + String.format("%02d", minutes)
				+ ":" + String.format("%02d", seconds));

		while (systFinished - syst > 0) {
			if ((!paused) && (running)) {
				try {

					// get current systemtime
					syst = System.currentTimeMillis();

					// how many ms are left
					msUntilFinished = systFinished - syst;

					// how many seconds are left
					secondsUntilFinished = (int) (msUntilFinished / 1000);

					// parse to readable format (mm:ss)
					seconds = secondsUntilFinished % 60;
					minutes = secondsUntilFinished / 60;

					sendNotification(workout,
							message + String.format("%02d", minutes) + ":"
									+ String.format("%02d", seconds));

					Thread.sleep(interval);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (paused) {

				// cancel scheduled alarm
				stopTimer(pi);

				sendNotification("Paused", message + "-Paused-");

				// myIntent.putExtra("DATA_TO_TA", "SET_CURRENT;" + workout +
				// ";" + (msUntilFinished));

				// wait for resume
				while (paused) {
					try {
						// Get out of here if the user requests a STOP
						if (!running) {
							break;
						}

						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				// reset gui elements
				// Send data for the GUI
				myIntent.setAction(MY_ACTION);
				myIntent.putExtra("DATA_TO_TA", "SET_CURRENT;" + workout + ";"
						+ (msUntilFinished));
				sendBroadcast(myIntent);

				// set alarm again with the last known resulting time
				startTimer((int) msUntilFinished, 0, "");

				// update systFinished
				systFinished = System.currentTimeMillis() + msUntilFinished;

			}

			if (!running) {
				// cancel scheduled alarm
				stopTimer(pi);

				// make sure the thread is stopped before killing this service
				// systFinished = 0;
				return false;

			}

		}
		return true;
	}

	// start a timer with time in mili seconds
	public void startTimer(int time, int id, String message) {

		Intent mi = new Intent(ProgramService.this, MyAlarmService.class);
		mi.putExtra("MESSAGE", message);

		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(System.currentTimeMillis());
		calendar.add(Calendar.MILLISECOND, time);

		pi = PendingIntent.getService(ProgramService.this, id, mi, 0);

		alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				pi);

		// Toast.makeText(ProgramService.this, "Start Alarm", Toast.LENGTH_LONG)
		// .show();

	}

	public void stopTimer(PendingIntent timerpi) {
		pi.cancel();

	}

	// ===========================================================
	// Helper functions
	// ===========================================================
	// Get a place in the notification bar and send a string
	// For sending 'real' notifications (those which can be 'cleaned')
	// from the bar, I'd suggest using 'sendNotification2()'
	public void sendNotification(String tickerTxt, String s) {
		int icon = nl.ttys0.simplec25k.R.drawable.nbarlogo;
		CharSequence tickerText = tickerTxt;

		final Notification notification = new Notification(icon, tickerText,
				System.currentTimeMillis());
		CharSequence contentTitle = "Simple C25K";
		CharSequence contentText = s;

		// define the actions to perform when user touch the notification
		Intent launchApp = new Intent(this, TimerActivity.class);
		// launchApp.putExtra("com.xxxxxxx.xxxxxxxxx.bean.Item",
		// "anyObjectYouWant");

		launchApp.setAction("VIEW_DETAILS_PROPERTY");
		PendingIntent launchNotification = PendingIntent.getActivity(
				getApplicationContext(), 0, launchApp, 0);

		notification.setLatestEventInfo(getApplicationContext(), contentTitle,
				contentText, launchNotification);

		startForeground(1337, notification);
	}

	// This method can be used to send a 'real' notification. This notification
	// can be removed from the bar by the user by clicking them
	// There's a hardcoded id in here, so the can only be only one notification
	// at the time.
	public void sendNotification2() {

		// Get a reference to the NotificationManager:
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);

		// Instantiate the Notification:
		int icon = nl.ttys0.simplec25k.R.drawable.ic_launcher;
		CharSequence tickerText = "Workout completed";
		long when = System.currentTimeMillis();

		Notification notification = new Notification(icon, tickerText, when);

		// Define the notification's message and PendingIntent:
		Context context = getApplicationContext();
		CharSequence contentTitle = "Simple C25K";
		CharSequence contentText = "Workout completed.";
		Intent notificationIntent = new Intent(this, TimerActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);

		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);

		// Pass the Notification to the NotificationManager:
		final int HELLO_ID = 1;
		mNotificationManager.notify(HELLO_ID, notification);

	}

	// class for receiving broadcasts. In this case it's being used to receive
	// commands from the TimerActivity
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

			int datapassed = arg1.getIntExtra("DATAPASSED", 0);
			String orgData = arg1.getStringExtra("DATA_TO_PS");

			// here we can receive commands from timerActivity

			// Toast.makeText(
			// ProgramService.this,
			// "ProgramService: Triggered\n" + "Data passed: "
			// + String.valueOf(datapassed) + "\n"
			// + "original Data: " + orgData, Toast.LENGTH_LONG)
			// .show();

			if (orgData != null) {
				if (orgData.equals("PAUSE"))
					paused = true;
				if (orgData.equals("RESUME"))
					paused = false;
				if (orgData.equals("STOP"))
					running = false;
			}

			// sendNotification("", orgData);

		}

	}

}
