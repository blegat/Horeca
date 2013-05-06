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
import android.widget.Toast;

public class ImageHorecaFragment extends Fragment implements ViewBinder {

	ImageView image;
	Button button;
	static int pictureIndex=0;
	int maxIndex;
	Horeca horeca;
	protected Vector<Picture> vecPic;
	
	public void buildVectorImages(){
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// On get l'horeca actuel
		Bundle b = getActivity().getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		horeca = new Horeca(horeca_id, db);
		db.close();
		
		this.vecPic=horeca.getVectorImage();
	}
	

	public void Retrieve(String pathAndName)
	   {
	    
	}
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View contentView = inflater.inflate(R.layout.image_horeca_view, container, false);
	    
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
	    StrictMode.setThreadPolicy(policy);

	    image = (ImageView) contentView.findViewById(R.id.imageView1);
	    buildVectorImages();
	    maxIndex=vecPic.size();
	    
		
	    //"http://www.reklampub.com/wp-content/uploads/2012/10/quick.jpg"
	    downloadImage(this.vecPic.get(0).getCompletePath());
	    
	    button = (Button) contentView.findViewById(R.id.btnChangeImage);
		button.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View arg0) {
				pictureIndex++;
				downloadImage(vecPic.get(pictureIndex%maxIndex).getCompletePath());
			}
 
		});
	    
	    return contentView;
	}
	
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
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