package com.sdei.menu;

import java.io.IOException;
import java.io.InputStream;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sdei.SpyCam.R;

public class CameraViewFragment extends Fragment {

	/*
	 * Declaring SharePreferences below
	 */
	SharedPreferences.Editor mEditor;
	SharedPreferences mPrefs;

	/*
	 *  Declaring Widgets
	 */

	TextView mAboutSpyCam;

	public CameraViewFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_home, container,
				false);

		mAboutSpyCam = (TextView) rootView.findViewById(R.id.txtData);
		getStreamTextByLine();
		return rootView;
	}

	void getStreamTextByLine() {
		try {
			InputStream is = getActivity().getAssets().open("spycam.txt");

			int size = is.available();

			// Read the entire asset into a local byte buffer.
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();

			// Convert the buffer into a string.
			String text = new String(buffer);

			// Finally stick the string into the text view.

			mAboutSpyCam.setText(text);
		} catch (IOException e) {
			// Should never happen!
			throw new RuntimeException(e);
		}
	}
}
