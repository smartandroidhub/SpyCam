package com.sdei.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import com.sdei.SpyCam.R;
import com.sdei.adapter.VideoAdapter;
import com.sdei.asynctasks.GetFilesFromSDCard;
import com.sdei.interfaces.VideoDataInterface;
import com.sdei.menu.DashboardCLass;

public class VideoGallery extends Activity implements VideoDataInterface {

	/*
	 * Declaring Gridview
	 */
	GridView mVideoGridview;

	
	/*
	 *  Header TextView 
	 */
	
	TextView mTotalNumberOfVideos;
	
	/*
	 * Declaring Adapter for Gridview
	 */
	VideoAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.videogallery);

		/*
		 * Calling Async Task for getting video thumbnails ...
		 */
		new GetFilesFromSDCard(VideoGallery.this).execute();

	}

	/*
	 * Method For setting up views
	 */

	private void setUpViews() {
		mVideoGridview = (GridView) findViewById(R.id.VideoGridview);
		mVideoGridview.setAdapter(mAdapter);
		
	}

	/*
	 * Interface Method overridden to set Adapter
	 */

	@Override
	public void setDataInVideoGallery(ArrayList<String> mVideoPaths) {
		mAdapter = new VideoAdapter(VideoGallery.this, mVideoPaths);
		mTotalNumberOfVideos=(TextView)findViewById(R.id.totalnumberofvideos);
		mTotalNumberOfVideos.setText("Total Video Recorded => " +mVideoPaths.size());
		setUpViews();
	}

	@Override
	public void onBackPressed() {
		Intent in = new Intent(VideoGallery.this, DashboardCLass.class);
		startActivity(in);
		finish();
	}
}
