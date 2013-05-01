package com.horeca;

import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Horeca {
	
	private static String defaultpath="res/drawable/HorecaPict/Default";
	private static String defaultnamefile="defaultHorecaPicture.jpg";
	
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Horeca.TABLE_NAME,
				HorecaContract.Horeca.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}

	@SuppressWarnings("null")
	private static Vector<Picture> convertCursorToVectorImage(Cursor cursor){
		Vector<Picture> vecimg = new Vector<Picture>(0);
		if(cursor.getCount()==0){
			//Picture temp=new Picture(defaultpath,defaultnamefile);
			//vecimg.addElement(temp);
		}
		else{
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				Picture temp = new Picture(cursor);
				vecimg.addElement(temp);
				cursor.moveToNext();
			};
		}
		cursor.close();
		return vecimg;
	}
	
	private long id;
	private String name;
	private double minPrice;
	private double maxPrice;
	private Ville ville;
	private String numtel;
	private String description;
	private Vector<Picture> pictures;
	
	public Horeca (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db,
				HorecaContract.Horeca._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Horeca._ID_INDEX);
		name = cursor.getString(HorecaContract.Horeca.NAME_INDEX);
		minPrice = cursor.getLong(HorecaContract.Horeca.MIN_PRICE_INDEX) / 100.;
		maxPrice = cursor.getLong(HorecaContract.Horeca.MAX_PRICE_INDEX) / 100.;
		long ville_id = cursor.getLong(HorecaContract.Horeca.VILLE_ID_INDEX);
		numtel = cursor.getString(HorecaContract.Horeca.NUMTEL_INDEX);
		description = cursor.getString(HorecaContract.Horeca.DESCRIPTION_INDEX);
		cursor.close();
		ville = new Ville(ville_id, db);
		this.pictures=convertCursorToVectorImage(Picture.getAllPicturesForHoreca(db,this));
	}
	
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
	public double getMinPrice() {
		return minPrice;
	}
	public double getMaxPrice() {
		return maxPrice;
	}
	public Ville getVille() {
		return ville;
	}
	public String getNumtel() {
		return numtel;
	}
	public boolean hasDescription() {
		return description != null;
	}
	public String getDescription() {
		return description;
	}
	public Vector<Picture> getVectorImage() {
		return pictures;
	}
}
