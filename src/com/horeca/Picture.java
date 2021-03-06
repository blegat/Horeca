package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Picture {
	
	private String path;
	private String namefile;
	
	public Picture(String p,String nf){
		this.path = p;
		this.namefile = nf;
	}

	public static Cursor getAllPicturesForHoreca(SQLiteDatabase db, Horeca horeca) {
		return getCursor(db,
				HorecaContract.Picture.HORECA_ID + " = ?",
				new String[]{((Long) horeca.getId()).toString()});
	    
	}
	public static Cursor getAllPicturesForPlat(SQLiteDatabase db, Plat plat) {
		return getPlatCursor(db,
				HorecaContract.PlatPicture.PLAT_ID + " = ?",
				new String[]{((Long) plat.getId()).toString()});
	    
	}
	private static Cursor getPlatCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.PlatPicture.TABLE_NAME,
				HorecaContract.PlatPicture.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Picture.TABLE_NAME,
				HorecaContract.Picture.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	public Picture(Cursor c){
		this.path=c.getString(HorecaContract.Picture.PATH_INDEX);
		this.namefile=c.getString(HorecaContract.Picture.NAME_INDEX);
	}
	
	public String getPath()
	{return this.path;}
	public String getNameFile()
	{return this.namefile;}
	public String getCompletePath()
	{return this.path+this.namefile;}
}
