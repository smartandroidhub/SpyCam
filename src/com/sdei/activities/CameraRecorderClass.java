package com.sdei.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdei.SpyCam.R;
import com.sdei.menu.DashboardCLass;
import com.sdei.services.RecorderService;

@SuppressWarnings("deprecation")
public class CameraRecorderClass extends Activity implements
		SurfaceHolder.Callback {

	/*
	 * Declaring Camera Surface And Camera Instance below
	 */
	public static SurfaceView mSurfaceView;
	public static SurfaceHolder mSurfaceHolder;
	public static Camera mCamera;
	
	public static boolean mPreviewRunning;
	
	/*
	 *	Declaring Widgets below  
	 */
	
	Button mStopRecordingBtn, mStartRecordingBtn;
	TextView mVideoPath,mHeaderText;
	ImageView mDownArrow;
	
	
	/*
	 *  Declaring SharePreferences below 
	 */
	SharedPreferences.Editor mEditor;
	SharedPreferences mPrefs;

	/** Called when the activity is first created. */

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera_recorder);

		/*
		 * Function to Initialize the views and widgets below
		 */
		setUpViews();

	}

	private void setUpViews() {
		
		/*
		 *    Initializing Camera Surface And Adding Camera Callback Below 
		 * 
		 */
		
		mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView1);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		
		/*
		 *   Initializing Start And Stop Recording Widgets(Buttons) Below
		 * 
		 */
		
		mStartRecordingBtn = (Button) findViewById(R.id.StartService);
		mStartRecordingBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			getPrefs();	
				if(mPrefs.getBoolean("timerrecording", false)){
					Toast.makeText(CameraRecorderClass.this,"Timer Recorder Already Set.Stop Timer First to start Instant recording", Toast.LENGTH_LONG).show();
				}else{
				AlertBox(CameraRecorderClass.this);
			}
			}
		});
		/*
		 * PackageManager p = getPackageManager();
		 * p.setComponentEnabledSetting(getComponentName(),
		 * PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
		 * PackageManager.DONT_KILL_APP);
		 */
		
		mStopRecordingBtn = (Button) findViewById(R.id.StopService);
		mStopRecordingBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
			getPrefs();
			getSharePrefsEditor();
			getSharePreference();
			setRecordingSharePreference(false);
				stopService(new Intent(CameraRecorderClass.this,
						RecorderService.class));
				mStartRecordingBtn.setVisibility(View.VISIBLE);
				mStopRecordingBtn.setVisibility(View.GONE);
				mVideoPath.setText("Find the recorded Video @ \n\n"+mPrefs.getString("videopath", ""));
				mVideoPath.setVisibility(View.VISIBLE);
				mHeaderText.setText("Start Instant Recording By Clickng Below");
			}
		});
		
		
		/*
		 *  Video Path TextView Initialization below
		 */

		mVideoPath=(TextView)findViewById(R.id.videopath);
	
	
	/*
	 *  Initializing Down Arrow Imageview
	 * 
	 */
		
		
		mDownArrow=(ImageView)findViewById(R.id.DownArrow);
		
		
		/*
		 *  Initializing Header Text
		 */
		
		mHeaderText=(TextView)findViewById(R.id.headertext);
		
	}

	
	/*
	 * 	Handling BackPress
	 */
	 
	@Override
	public void onBackPressed() {
		Intent in = new Intent(CameraRecorderClass.this, DashboardCLass.class);
		startActivity(in);
		finish();
	}

	/*
	 *  	Overriding On Resume 
	 */
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	 
		getPrefs();
		if (getSharePreference()) {
			mStopRecordingBtn.setVisibility(View.VISIBLE);
			mStartRecordingBtn.setVisibility(View.GONE);
			mHeaderText.setText("Stop Instant Recording By Clicking Below");
			mVideoPath.setVisibility(View.VISIBLE);
			//mVideoPath.setText("Find the recorded Video @ \n\n"+mPrefs.getString("videopath", ""));
		}
	}	
		 

	@Override
	public void surfaceCreated(SurfaceHolder holder) {

	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

	}

	
	/*
	 * 		Below Share Preferences functions to Save Values as desired
	 */
	
	void getSharePrefsEditor() {
		mEditor = getSharedPreferences("SPYCAM", MODE_PRIVATE).edit();
	}

	void setRecordingSharePreference(boolean mReset) {
		mEditor.putBoolean("recording", mReset);
		mEditor.commit();
	}

	void setPath() {
		mEditor.putString("videopath", Environment.getExternalStorageDirectory()+"/"+"TestSpyCam"+"/");
		mEditor.commit();
	}

	void getPrefs() {
		mPrefs = getSharedPreferences("SPYCAM", MODE_PRIVATE);
	}

	boolean getSharePreference() {

		return mPrefs.getBoolean("recording", false);

	}

	void AlertBox(Context mContext) {
		AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog2.setTitle("SpyCam");

		// Setting Dialog Message
		alertDialog2.setMessage("You Are About to Start Video Recording.Application will be sent to background.You Can Stop Recording By Coming Back on this screen.");


		// Setting Positive "Yes" Btn
		alertDialog2.setPositiveButton("YES",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						getSharePrefsEditor();
						setPath();
						setRecordingSharePreference(true);
						
						Intent intent = new Intent(CameraRecorderClass.this,
								RecorderService.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						startService(intent);
						finish();
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
	
}