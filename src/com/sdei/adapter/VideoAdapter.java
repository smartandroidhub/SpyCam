package com.sdei.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.sdei.SpyCam.R;

public class VideoAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<String> mPaths;
	Bitmap mBitmap;

	// Constructor
	public VideoAdapter(Context mContext, ArrayList<String> mPaths) {
		this.mContext = mContext;
		this.mPaths = mPaths;
	}

	public int getCount() {
		return mPaths.size();
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView picturesView;
		if (convertView == null) {
			picturesView = new ImageView(mContext);

			mBitmap = ThumbnailUtils.createVideoThumbnail(mPaths.get(position),
					0); // Creation of Thumbnail of video

			picturesView.setScaleType(ImageView.ScaleType.FIT_XY);
			picturesView.setPadding(4, 4, 4, 4);
			picturesView.setLayoutParams(new GridView.LayoutParams(100, 100));
			picturesView.setImageBitmap(mBitmap);
			picturesView.setBackgroundResource(R.drawable.user_img_brder);

			picturesView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					startVideo(mPaths.get(position));
				}
			});
		} else {
			picturesView = (ImageView) convertView;
		}
		return picturesView;
	}

	void startVideo(String mVideoPath) {
		Uri name = Uri.parse(mVideoPath);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		String type = "video/mp4";
		intent.setDataAndType(name, type);
		mContext.startActivity(intent);
	}

}