package com.horeca;

import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;

public class Horeca {
	
	private static String defaultpath="http://www.direct-signaletique.com/";
	private static String defaultnamefile="I-Grande-2995-panneaux-d-interdiction-pic-234.net.jpg";
	
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Horeca.TABLE_NAME,
				HorecaContract.Horeca.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	private static Cursor getFavoriteCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.UserFavoriteHoreca.TABLE_NAME,
				HorecaContract.UserFavoriteHoreca.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}

	@SuppressWarnings("null")
	private static Vector<Picture> convertCursorToVectorImage(Cursor cursor){
		Vector<Picture> vecimg = new Vector<Picture>(0);
		if(cursor.getCount()==0){
			Picture temp=new Picture(defaultpath,defaultnamefile);
			vecimg.addElement(temp);
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
	public Location location;
	private Ville ville;
	private String numtel;
	private String description;
	private Vector<Picture> pictures;
	private Vector<Label> labels;
	private boolean isFavorite;
	
	public Horeca (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db,
				HorecaContract.Horeca._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Horeca._ID_INDEX);
		name = cursor.getString(HorecaContract.Horeca.NAME_INDEX);
		minPrice = cursor.getLong(HorecaContract.Horeca.MIN_PRICE_INDEX) / 100.;
		maxPrice = cursor.getLong(HorecaContract.Horeca.MAX_PRICE_INDEX) / 100.;
		double longitude = cursor.getDouble(HorecaContract.Horeca.LONGITUDE_INDEX) / 1e6;
		double latitude = cursor.getDouble(HorecaContract.Horeca.LATITUDE_INDEX) / 1e6;
		this.location = new Location(longitude, latitude);
		long ville_id = cursor.getLong(HorecaContract.Horeca.VILLE_ID_INDEX);
		numtel = cursor.getString(HorecaContract.Horeca.NUMTEL_INDEX);
		description = cursor.getString(HorecaContract.Horeca.DESCRIPTION_INDEX);
		cursor.close();
		ville = new Ville(ville_id, db);
		this.pictures=convertCursorToVectorImage(Picture.getAllPicturesForHoreca(db,this));
		this.labels=Label.getVectorLabelForHoreca(db,this);
		Cursor cursorF = getFavoriteCursor(db, HorecaContract.UserFavoriteHoreca.USER_ID + "= ? AND "+
				HorecaContract.UserFavoriteHoreca.HORECA_ID + "= ?",
				new String[]{String.valueOf(User.getCurrentUser().getId()),String.valueOf(this.getId())});
		cursorF.moveToFirst();
		isFavorite=(cursorF.getCount()==1);
		cursorF.close();
	}
	public void setFavorite(SQLiteDatabase db){
		ContentValues cv = new ContentValues();
		cv.put(HorecaContract.UserFavoriteHoreca.USER_ID, String.valueOf(User.getCurrentUser().getId()));
		cv.put(HorecaContract.UserFavoriteHoreca.HORECA_ID, String.valueOf(this.getId()));
		db.insert(HorecaContract.UserFavoriteHoreca.TABLE_NAME, null, cv);
		isFavorite=true;
	}
	public void removeFavorite(SQLiteDatabase db){
		String where = HorecaContract.UserFavoriteHoreca.USER_ID + " = ? AND " + HorecaContract.UserFavoriteHoreca.HORECA_ID + " = ?";
		String whereargs[] = {String.valueOf(User.getCurrentUser().getId()),String.valueOf(this.getId())};
		db.delete(HorecaContract.UserFavoriteHoreca.TABLE_NAME,where,whereargs);
		isFavorite=false;
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
	public double getDistance(GPSTracker gps) {
		return location.getDistance(gps);
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
	public Vector<Label> getVectorLabel() {
		return labels;
	}
	public boolean isFavorite(){
		return isFavorite;
	}
}
