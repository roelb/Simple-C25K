/*
 * TimerActivity.java
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
 * info:
 * Class to control the ProgramService from a GUI.
 */


package nl.ttys0.simplec25k;

import java.util.HashMap;

import nl.ttys0.simplec25k.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class TimerActivity extends Activity {

	protected static final String MY_ACTION = "MY_ACTION";

	public static CountdownChronometer countdown;
	public static Context context;

	private TextView mainTitle;
	private TextView current;
	private TextView description;
	private TextView infotxt;
	private MyReceiver myReceiver;
	private HashMap<String, String> hashMap = new HashMap<String, String>();
	private Button button;
	private AlertDialog.Builder startedAlertbox;
	private AlertDialog.Builder completedAlertbox;

	// selected from main
	public static String selectedProgram;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timer);
		
		//Ask service if it's running, if so we'll receive a message with the right values for the gui.
	//	Intent myIntent = new Intent();
//		myIntent.setAction(MY_ACTION);
	//	myIntent.putExtra("DATA_TO_PS", "ARE_YOU_RUNNING");
		//sendBroadcast(myIntent);

		// setup descriptions
		hashMap.put(
				"w1d1",
				"Brisk five-minute warmup walk. Then alternate 60 seconds of jogging and 90 seconds of walking for a total of 20 minutes.");
		hashMap.put(
				"w1d2",
				"Brisk five-minute warmup walk. Then alternate 60 seconds of jogging and 90 seconds of walking for a total of 20 minutes.");
		hashMap.put(
				"w1d3",
				"Brisk five-minute warmup walk. Then alternate 60 seconds of jogging and 90 seconds of walking for a total of 20 minutes.");
		hashMap.put(
				"w2d1",
				"Brisk five-minute warmup walk. Then alternate 90 seconds of jogging and 2 minutes of walking for a total of 20 minutes.");
		hashMap.put(
				"w2d2",
				"Brisk five-minute warmup walk. Then alternate 90 seconds of jogging and 2 minutes of walking for a total of 20 minutes.");
		hashMap.put(
				"w2d3",
				"Brisk five-minute warmup walk. Then alternate 90 seconds of jogging and 2 minutes of walking for a total of 20 minutes.");
		hashMap.put(
				"w3d1",
				"Brisk five-minute warmup walk. Then do two repetitions of the following:\nJog 90 seconds\nWalk 90 seconds\nJog 3 minutes\nWalk 3 minutes");
		hashMap.put(
				"w3d2",
				"Brisk five-minute warmup walk. Then do two repetitions of the following:\nJog 90 seconds\nWalk 90 seconds\nJog 3 minutes\nWalk 3 minutes");
		hashMap.put(
				"w3d3",
				"Brisk five-minute warmup walk. Then do two repetitions of the following:\nJog 90 seconds\nWalk 90 seconds\nJog 3 minutes\nWalk 3 minutes");
		hashMap.put(
				"w4d1",
				"Brisk five-minute warmup walk. Then:\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes\nWalk 2-1/2 minutes\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes");
		hashMap.put(
				"w4d2",
				"Brisk five-minute warmup walk. Then:\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes\nWalk 2-1/2 minutes\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes");
		hashMap.put(
				"w4d3",
				"Brisk five-minute warmup walk. Then:\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes\nWalk 2-1/2 minutes\nJog 3 minutes\nWalk 90 seconds\nJog 5 minutes");
		hashMap.put(
				"w5d1",
				"Brisk five-minute warmup walk, then:\nJog 5 minutes\nWalk 3 minutes\nJog 5 minutes\nWalk 3 minutes\nJog 5 minutes");
		hashMap.put(
				"w5d2",
				"Brisk five-minute warmup walk, then:\nJog 8 minutes\nWalk 5 minutes\nJog 8 minutes");
		hashMap.put("w5d3",
				"Brisk five-minute warmup walk, then jog 20 minutes) with no walking.");
		hashMap.put(
				"w6d1",
				"Brisk five-minute warmup walk, then:\nJog 5 minutes\nWalk 3 minutes\nJog 8 minutes\nWalk 3 minutes\nJog 5 minutes");
		hashMap.put(
				"w6d2",
				"Brisk five-minute warmup walk, then:\nJog 10 minutes\nWalk 3 minutes\nJog 10 minutes");
		hashMap.put("w6d3",
				"Brisk five-minute warmup walk, then jog 25 minutes with no walking.");
		hashMap.put("w7d1",
				"Brisk five-minute warmup walk, then jog 25 minutes.");
		hashMap.put("w7d2",
				"Brisk five-minute warmup walk, then jog 25 minutes.");
		hashMap.put("w7d3",
				"Brisk five-minute warmup walk, then jog 25 minutes.");
		hashMap.put("w8d1",
				"Brisk five-minute warmup walk, then jog 28 minutes.");
		hashMap.put("w8d2",
				"Brisk five-minute warmup walk, then jog 28 minutes.");
		hashMap.put("w8d3",
				"Brisk five-minute warmup walk, then jog 28 minutes.");
		hashMap.put("w9d1",
				"Brisk five-minute warmup walk, then jog 30 minutes.");
		hashMap.put("w9d2",
				"Brisk five-minute warmup walk, then jog 30 minutes.");
		hashMap.put("w9d3",
				"Brisk five-minute warmup walk, then jog 30 minutes.");

		TimerActivity.context = this;

		setupAlertboxes();
		

		// Register BroadcastReceiver
		// to receive event from our service
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ProgramService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		selectedProgram = Simplec25kMainActivity.selectedProgram;

		countdown = (CountdownChronometer) findViewById(R.id.chronometer1);
		// countdown.setBase(System.currentTimeMillis() + 30000);

		mainTitle = (TextView) findViewById(R.id.name);
		mainTitle.setText(selectedProgram.replace("w", "Week ").replace("d",
				", Day "));

		// Watch for button clicks.
		button = (Button) findViewById(R.id.startButton);
		button.setOnClickListener(mStartListener);

		button = (Button) findViewById(R.id.stopButton);
		button.setOnClickListener(mStopListener);
		// button.setVisibility(View.GONE);

		button = (Button) findViewById(R.id.pauseButton);
		button.setOnClickListener(mPauseListener);

		button = (Button) findViewById(R.id.resumeButton);
		button.setOnClickListener(mResumeListener);

		// set text fields
		description = (TextView) findViewById(R.id.textView1);
		description.setText(hashMap.get(selectedProgram));

		current = (TextView) findViewById(R.id.textView2);
		current.setText("Press Start to begin.");

		infotxt = (TextView) findViewById(R.id.textView3);
		infotxt.setText("");

		countdown = (CountdownChronometer) findViewById(R.id.chronometer1);

	}

	@Override
	protected void onResume() {

		super.onResume();
		
	}
	
	@Override
	protected void onDestroy(){
		unregisterReceiver(myReceiver);
		super.onDestroy();
	}

	@Override
	protected void onPause() {

		// unregisterReceiver(myReceiver);

		super.onPause();
	}

	// ===========================================================
	// Buttons
	// ===========================================================
	// Start button
	View.OnClickListener mStartListener = new OnClickListener() {
		public void onClick(View v) {

			// disable startbutton
			button = (Button) findViewById(R.id.startButton);
			button.setClickable(false);

			// Start our own service
			Intent svcIntent = new Intent(TimerActivity.this,
					nl.ttys0.simplec25k.ProgramService.class);
			svcIntent.putExtra("INIT_DATA", selectedProgram);
			startService(svcIntent);

		}
	};

	// pause button
	View.OnClickListener mPauseListener = new OnClickListener() {
		public void onClick(View v) {
			// mChronometer.stop();

			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_PS", "PAUSE");
			sendBroadcast(myIntent);

			countdown.stop();

		}
	};

	// resume button
	View.OnClickListener mResumeListener = new OnClickListener() {
		public void onClick(View v) {
			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_PS", "RESUME");
			sendBroadcast(myIntent);
		}
	};

	// stop button
	View.OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_PS", "STOP");
			sendBroadcast(myIntent);

			finish();
		}
	};

	// back button (hard button)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			// send stop to ps
			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_PS", "STOP");
			sendBroadcast(myIntent);

			// stop the program service

			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void setupAlertboxes() {

		// prepare the startedAlertbox
		startedAlertbox = new AlertDialog.Builder(this);

		// set the message to display
		startedAlertbox
				.setMessage("Workout started. Warmup for five minutes.\nGet busy!");

		// add a neutral button to the alert box and assign a click listener
		startedAlertbox.setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {

					// click listener on the alert box
					public void onClick(DialogInterface arg0, int arg1) {
					}
				});

		// prepare the completedAlertbox, on click the activity will be closed!
		completedAlertbox = new AlertDialog.Builder(this);

		// set the message to display
		completedAlertbox.setMessage("Workout finished. Well done!");

		// add a neutral button to the alert box and assign a click listener
		completedAlertbox.setNeutralButton("Ok",
				new DialogInterface.OnClickListener() {

					// click listener on the alert box
					public void onClick(DialogInterface arg0, int arg1) {
						finish();
					}
				});
		// /

	}

	// class for receiving broadcasts
	private class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent arg1) {

			String orgData = arg1.getStringExtra("DATA_TO_TA");

			if (orgData != null) {
				if (orgData.equals("DONE")) {
					completedAlertbox.show();
				}
				if (orgData.equals("STARTED")) {
					startedAlertbox.show();

				}
				if (orgData.contains("SET_CURRENT")) {
					String[] s = orgData.split(";");

					current.setText(s[1]);

					countdown.setBase(System.currentTimeMillis()
							+ Integer.parseInt(s[2]));
					countdown.start();

				}
				
	
			}

		}

	}

}
