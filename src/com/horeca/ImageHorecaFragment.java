package com.horeca;

import java.util.Date;
import java.util.Vector;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class ImageHorecaFragment extends Fragment implements ViewBinder {
	
	@SuppressWarnings("deprecation")
	private Gallery gallery;
	private ImageView imgView;
	private Vector<Picture> VectorImages;
	private Drawable mNoImage;
	private Horeca horeca;
	private ListView listview;
	
	public Vector<Picture> buildVectorImages(){
		Vector<Picture> VecImg = null;
		
		MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
		SQLiteDatabase db = sqliteHelper.getReadableDatabase();
		
		// On get l'horeca actuel
		Bundle b = getActivity().getIntent().getExtras();
		long horeca_id = b.getLong("horeca_id");
		horeca = new Horeca(horeca_id, db);
		Cursor cursor = Picture.getAllPicturesForHoreca(db, horeca);
		
		db.close();
		
		VecImg=horeca.getVectorImage();
		
		return VecImg;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
         Bundle savedInstanceState) {
		// Open the db
				MySqliteHelper sqliteHelper = new MySqliteHelper(getActivity());
				SQLiteDatabase db = sqliteHelper.getReadableDatabase();
				
				// Get the horeca from which to take the plats
				// specified by the HorecaListActivity calling this activity
				Bundle b = getActivity().getIntent().getExtras();
				long horeca_id = b.getLong("horeca_id");
				horeca = new Horeca(horeca_id, db);
				
				// Get the menu
				Cursor cursor = Picture.getAllPicturesForHoreca(db, horeca);
				
				View view = inflater.inflate(R.layout.image_horeca_view, container, false);
				
				// Display the menu in a list
				@SuppressWarnings("deprecation")
				SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), //this context
						android.R.layout.simple_list_item_1, //id of the item layout used by default for the individual rows (this id is pre-defined by Android)
						//android.R.id.list,
						//R.id.plats_list,
						cursor,
						new String[] { HorecaContract.Plat.NAME },
						new int[] { android.R.id.text1 }); // the list of objects to be adapted
				// to remove deprecation warning, I need to add ", 0" but it is only in API 11 and we need 2.3.3 which is API 10
				
				listview = (ListView) view.findViewById(R.id.plats_list);
				listview.setAdapter(adapter);
				
				db.close();

		        return view;
	}

	@Override
	public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
		// TODO Auto-generated method stub
		return false;
	}
	
	
	
}