package com.sdei.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;

import com.sdei.services.TimerRecordingService;

public class AlarmReceiver extends BroadcastReceiver {

	Context mContext;
    /*
	 *  Declaring SharePreferences below 
	 */
	SharedPreferences.Editor mEditor;
	SharedPreferences mPrefs;

    @Override
    public void onReceive(Context mContext, Intent k2) {
    	this.mContext=mContext;
		getSharePrefsEditor();
		setPath();
		setRecordingSharePreference(true);
    	Intent Timerintent = new Intent(mContext,
 				TimerRecordingService.class);
         Timerintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         mContext.startService(Timerintent);
         
    }
    
    /*
	 * 		Below Share Preferences functions to Save Values as desired
	 */
	
	void getSharePrefsEditor() {
		mEditor = mContext.getSharedPreferences("SPYCAM", Context.MODE_MULTI_PROCESS).edit();
	}

	void setRecordingSharePreference(boolean mReset) {
		mEditor.putBoolean("service", mReset);
		mEditor.commit();
	}

	void setPath() {
		mEditor.putString("timervideopath", Environment.getExternalStorageDirectory()+"/"+"TimerTestSpyCam"+"/");
		mEditor.commit();
	}

	void getPrefs() {
		mPrefs = mContext.getSharedPreferences("SPYCAM", Context.MODE_MULTI_PROCESS);
	}

	boolean getSharePreference() {

		return mPrefs.getBoolean("timerrecording", false);

	}
}