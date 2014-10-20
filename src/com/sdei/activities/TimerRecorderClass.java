package com.sdei.activities;

import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import com.sdei.SpyCam.R;
import com.sdei.menu.DashboardCLass;
import com.sdei.services.RecorderService;
import com.sdei.services.TimerRecordingService;

public class TimerRecorderClass extends Activity {

	
	/*
	 * 	Declaring Request Variable.
	 */
	final static int RQS_1 = 1;
	
	/*
	 *	 Declaring Pending Intent
	 */
	
	PendingIntent mPendingIntent;

	/*
	 * Declaring SharePreferences below
	 */
	
	SharedPreferences.Editor mEditor;
	SharedPreferences mPrefs;
	
	/*
	 * Declaring Widgets below
	 */
	
	TextView mVideoPath, mHeaderText, mAlramTimeView;
	Button mStartRecordingBtn, mStopRecordingBtn;
	TimePickerDialog mTimePickerDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recorderlayout);
		setUpViews();
	}

	private void setUpViews() {
		mAlramTimeView = (TextView) findViewById(R.id.recordingtimeprompt);
		mStartRecordingBtn = (Button) findViewById(R.id.startrecordingtime);
		mStartRecordingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (mStartRecordingBtn
						.getText()
						.toString()
						.equalsIgnoreCase("Recording Time is Set (Click to Cancel)")) {
					mStartRecordingBtn.setText("Set Recording Time");
					mHeaderText
							.setText("Start Timer Recording By Clickng Below");
					CancelAlram();
					mAlramTimeView.setVisibility(View.GONE);
					mVideoPath.setVisibility(View.GONE);
				} else
					AlertBox(TimerRecorderClass.this);
			}
		});
		
		mStopRecordingBtn = (Button) findViewById(R.id.stoprecordingtime);
		mStopRecordingBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getPrefs();
				getSharePrefsEditor();
				getSharePreference();
				setRecordingSharePreference(false);
				stopService(new Intent(TimerRecorderClass.this,
						RecorderService.class));
				mStartRecordingBtn.setVisibility(View.VISIBLE);
				mStartRecordingBtn.setText("Set Recording Time");
				mStopRecordingBtn.setVisibility(View.GONE);
				stopService(new Intent(TimerRecorderClass.this,
						TimerRecordingService.class));
				mVideoPath.setText("Find the recorded Video @ \n\n"
						+ mPrefs.getString("timervideopath", ""));
				mVideoPath.setVisibility(View.VISIBLE);
				mHeaderText.setText("Start Timer Recording By Clickng Below");
			}
		});

		/*
		 * Video Path TextView Initialization below
		 */

		mVideoPath = (TextView) findViewById(R.id.videopath);

		/*
		 * Initializing Header Text
		 */

		mHeaderText = (TextView) findViewById(R.id.headertext);

	}

	@Override
	protected void onResume() {
		super.onResume();
		getPrefs();
		if (getSharePreference()) {
			
			if(mPrefs.getBoolean("service", false)){
				mStopRecordingBtn.setVisibility(View.VISIBLE);
				mStartRecordingBtn.setVisibility(View.GONE);
				mHeaderText.setText("Recording Time Set By Clickng Below");
				mStopRecordingBtn.setText("Recording In Progress (Click to Stop)");
			}else{
				mHeaderText.setText("Cancel recording Time Set By Clickng Below");
				mStartRecordingBtn.setText("Recording Time is Set (Click to Cancel)");
				mAlramTimeView.setVisibility(View.VISIBLE);
				mAlramTimeView.setText(mPrefs.getString("RecordingTime", ""));
			}
		}else{
			mHeaderText.setText("Start Timer Recording By Clickng Below");
			mStartRecordingBtn.setText("Set Recording Time");
		}
	}

	private void openTimePickerDialog(boolean is24r) {
		Calendar mCalendar = Calendar.getInstance();
		mTimePickerDialog = new TimePickerDialog(TimerRecorderClass.this,
				onTimeSetListener, mCalendar.get(Calendar.HOUR_OF_DAY),
				mCalendar.get(Calendar.MINUTE), is24r);
		mTimePickerDialog.setTitle("Set Recording Time");
		mTimePickerDialog.show();
	}

	OnTimeSetListener onTimeSetListener = new OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			Calendar calNow = Calendar.getInstance();
			Calendar calSet = (Calendar) calNow.clone();

			calSet.set(Calendar.HOUR_OF_DAY, hourOfDay);
			calSet.set(Calendar.MINUTE, minute);
			calSet.set(Calendar.SECOND, 0);
			calSet.set(Calendar.MILLISECOND, 0);

			if (calSet.compareTo(calNow) <= 0) {
				calSet.add(Calendar.DATE, 1);
			}

			setAlarm(calSet);
		}
	};

	private void setAlarm(Calendar targetCal) {
		mAlramTimeView.setVisibility(View.VISIBLE);
		mAlramTimeView.setText("\n\n***\n" + "Recording is set "
				+ targetCal.getTime() + "\n" + "***\n");

		getSharePrefsEditor();
		
		setRecordingTime("\n\n***\n" + "Recording is set "
				+ targetCal.getTime() + "\n" + "***\n");
		
		Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
		mPendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1,
				intent, 0);
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),
				mPendingIntent);

		mStartRecordingBtn.setText("Recording Time is Set (Click to Cancel)");
		mHeaderText.setText("Stop recording Timer By Clickng Below");
		mVideoPath.setVisibility(View.GONE);

		getSharePrefsEditor();
		setPath();
		setRecordingSharePreference(true);
		
		getSharePrefsEditor();
		mEditor.putBoolean("service", false);
		mEditor.commit();
	}

	/*
	 * Handling BackPress
	 */

	@Override
	public void onBackPressed() {
		Intent in = new Intent(TimerRecorderClass.this, DashboardCLass.class);
		startActivity(in);
		finish();
	}

	void AlertBox(Context mContext) {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog2.setTitle("SpyCam");

		// Setting Dialog Message
		alertDialog2
				.setMessage("You Are About to Start Timer Video Recording.You Can Stop Recording By Coming Back on this screen.");

		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						mAlramTimeView.setText("");
						openTimePickerDialog(false);

						dialog.cancel();

					}
				});
		// Setting Negative "NO" Btn
		alertDialog2.setNegativeButton("NO",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {

						dialog.cancel();
					}
				});

		// Showing Alert Dialog
		alertDialog2.show();

	}

	/*
	 * Below Share Preferences functions to Save Values as desired
	 */

	void getSharePrefsEditor() {
		mEditor = getSharedPreferences("SPYCAM", MODE_MULTI_PROCESS).edit();
	}

	void setRecordingSharePreference(boolean mReset) {
		mEditor.putBoolean("timerrecording", mReset);
		mEditor.commit();
	}

	void setPath() {
		mEditor.putString("timervideopath",
				Environment.getExternalStorageDirectory() + "/"
						+ "TimerTestSpyCam" + "/");
		mEditor.commit();
	}
	
	void setRecordingTime(String mRecordingTime) {
		mEditor.putString("RecordingTime",mRecordingTime);
		mEditor.commit();
	}

	void getPrefs() {
		mPrefs = getSharedPreferences("SPYCAM", MODE_MULTI_PROCESS);
	}

	boolean getSharePreference() {

		return mPrefs.getBoolean("timerrecording", false);

	}

	/*
	 * Canceling Set timer For recording Below 
	 * 
	 */
	void CancelAlram() {

		if (mPrefs.getBoolean("service", false)) {
			getPrefs();
			getSharePrefsEditor();
			getSharePreference();
			setRecordingSharePreference(false);
			stopService(new Intent(TimerRecorderClass.this,
					RecorderService.class));
			mStartRecordingBtn.setVisibility(View.VISIBLE);
			mStopRecordingBtn.setVisibility(View.GONE);
			stopService(new Intent(TimerRecorderClass.this,
					TimerRecordingService.class));
			mVideoPath.setText("Find the recorded Video @ \n\n"
					+ mPrefs.getString("timervideopath", ""));
			mVideoPath.setVisibility(View.VISIBLE);
			mHeaderText.setText("Start Timer Recording By Clickng Below");
		} else {
			Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
			mPendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1,
					intent, 0);
				AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
				alarmManager.cancel(mPendingIntent);
			
			getSharePrefsEditor();
			setRecordingSharePreference(false);
			 
		}
	}
}
