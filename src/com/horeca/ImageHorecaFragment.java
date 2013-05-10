package com.horeca;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter.ViewBinder;

public class ImageHorecaFragment extends Fragment implements ViewBinder {

	View contentView ;
	ImageView image;
	Button button;
	static int pictureIndex=0;
	static int maxIndex;
	Horeca horeca;
	private Vector<Picture> vecPic;
	
	
	public String  getCompletePathForPicture(int index){
		return this.getVecPic().get(pictureIndex).getCompletePath();
	}
	
	public Vector<Picture> getVecPic(){
		return vecPic;
	}
	
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
	
	@SuppressLint("NewApi")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    this.contentView = inflater.inflate(R.layout.image_horeca_view, container, false);
	    
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build(); 
	    StrictMode.setThreadPolicy(policy);
	    
	    image = (ImageView) contentView.findViewById(R.id.imageView1);
	    buildVectorImages();
	    maxIndex=vecPic.size()-1;
	    
	    downloadImage(this.vecPic.get(0).getCompletePath());
	    
	    this.contentView.setOnTouchListener(
	            new View.OnTouchListener() {
	            	float x1, x2, y1, y2, t1nano, t2nano;
	            	
	                public boolean onTouch(View myView, MotionEvent event) {
	                    int action = event.getAction();
	                    boolean mustBeCHanged=false;
	                    if (action==MotionEvent.ACTION_DOWN)
	                    {
	                    	x1=(float) event.getX();
	                    	y1=(float) event.getY();
	                    	t1nano=(float) System.nanoTime();
	                    }
	                    if (action==MotionEvent.ACTION_UP)
	                    {
	                    	x2=(float) event.getX();
	                    	y2=(float) event.getY();
	                    	t2nano=(float) System.nanoTime();
	                    }
		                if(isMovementLeftToRight(x1,x2,y1,y2,t1nano,t2nano)){
		                	pictureIndex++;
		                    mustBeCHanged=true;
		                };
		                if(isMovementRightToLeft(x1,x2,y1,y2,t1nano,t2nano)){
		                	pictureIndex--;
		                	mustBeCHanged=true;
		                };
		                
		                if(pictureIndex<0){
		                	pictureIndex=0;
		        		}
		        		if(pictureIndex>maxIndex){
		        			pictureIndex=maxIndex;
		        		}
		        			
		        		if (mustBeCHanged){
	                	downloadImage(getCompletePathForPicture(pictureIndex));
	                	mustBeCHanged=false;
		        		}
	                    return true;
	                }
	            }    
	            );
	    
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
	
	public boolean isMovementLeftToRight(float x1, float x2, float y1, float y2, float t1nano, float t2nano) {
		
		final int SWIPE_MIN_DIFF_X = 100;
		final int SWIPE_MAX_DIFF_Y = 100;
		final int SWIPE_MIN_VELOCITY = 500;
		
		if(Math.abs(y1 - y2) > SWIPE_MAX_DIFF_Y) {
		return false;
		}
		float diffX = x2 - x1 ;
		float velocity = Math.abs(diffX*1000000000)/(t2nano-t1nano);
		if (Math.abs(diffX) > SWIPE_MIN_DIFF_X && velocity > SWIPE_MIN_VELOCITY) {
			if(diffX<0){
				return true;
			}
		}
		return false;
	}
	public boolean isMovementRightToLeft(float x1, float x2, float y1, float y2, float t1nano, float t2nano) {
		
		final int SWIPE_MIN_DIFF_X = 100;
		final int SWIPE_MAX_DIFF_Y = 100;
		final int SWIPE_MIN_VELOCITY = 500;
		
		if(Math.abs(y1 - y2) > SWIPE_MAX_DIFF_Y) {
		return false;
		}
		float diffX = x2 - x1 ;
		float velocity = Math.abs(diffX*1000000000)/(t2nano-t1nano);
		if (Math.abs(diffX) > SWIPE_MIN_DIFF_X && velocity > SWIPE_MIN_VELOCITY) {
			if(diffX>0){
				return true;
			}
		}
		return false;
	}
}