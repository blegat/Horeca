package com.horeca;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.View.OnClickListener;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;
import android.widget.Toast;

public class LabelFragment extends Fragment implements ViewBinder {
	
	private Vector<Label> vecLabel;
	private Horeca horeca;
	ImageView image;
	TextView descriptionView;
	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@SuppressLint("NewApi")
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.label_horeca_view, container, false);
		
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// On get l'horeca actuel
		Bundle b = getActivity().getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		horeca = new Horeca(horeca_id, db);
		db.close();
		
		this.vecLabel=horeca.getVectorLabel();
		
		this.image = (ImageView) contentView.findViewById(R.id.imageView1);
		this.descriptionView = (TextView) contentView.findViewById(R.id.descriptionLabel);
		descriptionView.setText(vecLabel.get(0).getDescription());
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
	    StrictMode.setThreadPolicy(policy);
	    Log.e("LabelFragment",vecLabel.get(0).getPicture().getCompletePath());
		downloadImage(vecLabel.get(0).getPicture().getCompletePath());
		
	    return contentView;
	}
	
	private void downloadImage(String imageURL) {

		Bitmap bitmap = null;

		try {

		URL urlImage = new URL(imageURL);
		HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
		InputStream inputStream = connection.getInputStream();
		bitmap = BitmapFactory.decodeStream(inputStream);
		image.setImageBitmap(bitmap);

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		}

}