package com.sdei.asynctasks;

import java.io.File;
import java.util.ArrayList;

import com.sdei.interfaces.VideoDataInterface;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

public class GetFilesFromSDCard extends AsyncTask<Void, Void, Void> {

	ProgressDialog mProgress;
	ArrayList<String> mVideoPaths = new ArrayList<String>();
	File[] mNormalRecordedFiles, mTimerRecordedFiles;
	ArrayList<String> arrayFiles;
	Context mContext;
	VideoDataInterface mInterface;

	public GetFilesFromSDCard(Context mContext) {
		this.mContext = mContext;
		mInterface = (VideoDataInterface) mContext;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		mProgress = new ProgressDialog(mContext);
		mProgress.setMessage("Please Wait.... ");

		mProgress.show();
	}

	@Override
	protected Void doInBackground(Void... params) {
		getFilePaths();

		return null;
	}

	private void getFilePaths() {

		GetNormalFiles(Environment.getExternalStorageDirectory() + "/"
				+ "TestSpyCam" + "/");

		for (int i = 0; i < mNormalRecordedFiles.length; i++) {

			Log.e("Path", "" + mNormalRecordedFiles[i]);
			mVideoPaths.add((mNormalRecordedFiles[i] + "").toString().trim());
		}

		GetTimerFiles(Environment.getExternalStorageDirectory() + "/"
				+ "TimerTestSpyCam" + "/");

		for (int i = 0; i < mTimerRecordedFiles.length; i++) {

			Log.e("Path", "" + mTimerRecordedFiles[i]);
			mVideoPaths.add((mTimerRecordedFiles[i] + "").toString().trim());
		}

	}

	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		mProgress.hide();
		mInterface.setDataInVideoGallery(mVideoPaths);
	}

	public File[] GetNormalFiles(String DirectoryPath) {
		File f = new File(DirectoryPath);
		f.mkdirs();
		mNormalRecordedFiles = f.listFiles();

		return mNormalRecordedFiles;
	}

	public File[] GetTimerFiles(String DirectoryPath) {
		File f = new File(DirectoryPath);
		f.mkdirs();
		mTimerRecordedFiles = f.listFiles();

		return mTimerRecordedFiles;
	}
}
