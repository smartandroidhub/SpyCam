package com.sdei.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sdei.SpyCam.R;
import com.sdei.menu.DashboardCLass;

public class AboutMeSection extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aboutmesection);
	}

	/*
	 * Handling BackPress
	 */

	@Override
	public void onBackPressed() {
		Intent in = new Intent(AboutMeSection.this, DashboardCLass.class);
		startActivity(in);
		finish();
	}

}
