/*
 *  TimerActivity.java
 * 
 *  Copyright 2012 Roel Blaauwgeers <roel@ttys0.nl>
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 *  info:
 *  Class to control the ProgramService from a GUI.
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
import android.content.res.Resources;
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
	private AlertDialog.Builder stopAlertbox;

	private AlertDialog.Builder skipAlertbox;

	// selected from main
	public static String selectedProgram;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.timer);

		// setup descriptions, there is probably an easier way that I don't know
		// of:)
		Resources res = getResources();
		hashMap.put("w1d1", res.getString(R.string.w1));
		hashMap.put("w1d2", res.getString(R.string.w1));
		hashMap.put("w1d3", res.getString(R.string.w1));

		hashMap.put("w2d1", res.getString(R.string.w2));
		hashMap.put("w2d2", res.getString(R.string.w2));
		hashMap.put("w2d3", res.getString(R.string.w2));

		hashMap.put("w3d1", res.getString(R.string.w3));
		hashMap.put("w3d2", res.getString(R.string.w3));
		hashMap.put("w3d3", res.getString(R.string.w3));

		hashMap.put("w4d1", res.getString(R.string.w4));
		hashMap.put("w4d2", res.getString(R.string.w4));
		hashMap.put("w4d3", res.getString(R.string.w4));

		hashMap.put("w5d1", res.getString(R.string.w5d1));
		hashMap.put("w5d2", res.getString(R.string.w5d2));
		hashMap.put("w5d3", res.getString(R.string.w5d3));

		hashMap.put("w6d1", res.getString(R.string.w6d1));
		hashMap.put("w6d2", res.getString(R.string.w6d2));
		hashMap.put("w6d3", res.getString(R.string.w6d3));

		hashMap.put("w7d1", res.getString(R.string.w7));
		hashMap.put("w7d2", res.getString(R.string.w7));
		hashMap.put("w7d3", res.getString(R.string.w7));

		hashMap.put("w8d1", res.getString(R.string.w8));
		hashMap.put("w8d2", res.getString(R.string.w8));
		hashMap.put("w8d3", res.getString(R.string.w8));

		hashMap.put("w9d1", res.getString(R.string.w9));
		hashMap.put("w9d2", res.getString(R.string.w9));
		hashMap.put("w9d3", res.getString(R.string.w9));

		TimerActivity.context = this;

		setupAlertboxes();

		// Register BroadcastReceiver
		// to receive event from our service
		myReceiver = new MyReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ProgramService.MY_ACTION);
		registerReceiver(myReceiver, intentFilter);

		// get the selected program. If selected program is null it probably
		// means the main activity is killed. Therefore we start a new main
		// activity and kill the current timerActivity. It woul probably be
		// better if this variable would be set the first time starting this
		// activity. But this works for now
		selectedProgram = Simplec25kMainActivity.selectedProgram;
		if (selectedProgram == null) {
			// restart app
			Intent i = getBaseContext().getPackageManager()
					.getLaunchIntentForPackage(
							getBaseContext().getPackageName());
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
		} else {

			countdown = (CountdownChronometer) findViewById(R.id.chronometer1);
			// countdown.setBase(System.currentTimeMillis() + 30000);

			mainTitle = (TextView) findViewById(R.id.name);
			mainTitle.setText(selectedProgram.replace("w", "Week ").replace(
					"d", ", Day "));

			// Watch for button clicks.
			button = (Button) findViewById(R.id.startButton);
			button.setOnClickListener(mStartListener);

			button = (Button) findViewById(R.id.stopButton);
			button.setOnClickListener(mStopListener);
			button.setClickable(false);

			button = (Button) findViewById(R.id.pauseButton);
			button.setOnClickListener(mPauseListener);
			button.setClickable(false);

			button = (Button) findViewById(R.id.resumeButton);
			button.setOnClickListener(mResumeListener);
			button.setClickable(false);

			button = (Button) findViewById(R.id.skipButton);
			button.setOnClickListener(mSkipListener);
			button.setClickable(false);

			// set text fields
			description = (TextView) findViewById(R.id.textView1);
			description.setText(hashMap.get(selectedProgram));

			current = (TextView) findViewById(R.id.textView2);
			current.setText("Press Start to begin.");

			infotxt = (TextView) findViewById(R.id.textView3);
			infotxt.setText("");

			countdown = (CountdownChronometer) findViewById(R.id.chronometer1);

		}

	}

	@Override
	protected void onResume() {

		super.onResume();

	}

	@Override
	protected void onDestroy() {
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

			// enable stopbutton
			button = (Button) findViewById(R.id.stopButton);
			button.setClickable(true);

			// enable skipbutton
			button = (Button) findViewById(R.id.skipButton);
			button.setClickable(true);

			// enable pausebutton
			button = (Button) findViewById(R.id.pauseButton);
			button.setClickable(true);

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

			// enable resumebutton
			button = (Button) findViewById(R.id.resumeButton);
			button.setClickable(true);

			// disable skipbutton
			button = (Button) findViewById(R.id.skipButton);
			button.setClickable(false);

			// disable pausebutton
			button = (Button) findViewById(R.id.pauseButton);
			button.setClickable(false);

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

			// disable resumebutton
			button = (Button) findViewById(R.id.resumeButton);
			button.setClickable(false);

			// enable pausebutton
			button = (Button) findViewById(R.id.pauseButton);
			button.setClickable(true);

			// enable skipbutton
			button = (Button) findViewById(R.id.skipButton);
			button.setClickable(true);

			Intent myIntent = new Intent();
			myIntent.setAction(MY_ACTION);
			myIntent.putExtra("DATA_TO_PS", "RESUME");
			sendBroadcast(myIntent);
		}
	};

	// skip button
	View.OnClickListener mSkipListener = new OnClickListener() {
		public void onClick(View v) {
			skipAlertbox.show();
		}
	};

	// stop button
	View.OnClickListener mStopListener = new OnClickListener() {
		public void onClick(View v) {
			stopAlertbox.show();
		}
	};

	// back button (hard button)
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {

			stopAlertbox.show();
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

		// prepare stopAlertbox, yeah I know this could go together with the
		// skipalertbox
		stopAlertbox = new AlertDialog.Builder(this);
		stopAlertbox
				.setMessage("Are you sure you want to exit?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// send STOP signal
								Intent myIntent = new Intent();
								myIntent.setAction(MY_ACTION);
								myIntent.putExtra("DATA_TO_PS", "STOP");
								sendBroadcast(myIntent);

								// stop this activity
								finish();
							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// prepare skipalertbox
		skipAlertbox = new AlertDialog.Builder(this);
		skipAlertbox
				.setMessage("Are you sure you want to skip?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// send STOP signal
								Intent myIntent = new Intent();
								myIntent.setAction(MY_ACTION);
								myIntent.putExtra("DATA_TO_PS", "SKIP");
								sendBroadcast(myIntent);

							}
						})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

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
