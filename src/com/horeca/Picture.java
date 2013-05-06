package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Picture {
	
	public Picture(String p,String nf){
		this.path = p;
		this.namefile = nf;
	}

	public static Cursor getAllPicturesForHoreca(SQLiteDatabase db, Horeca horeca) {
		Log.e("Horeca",String.valueOf(horeca.getId()));
		return getCursor(db,
				HorecaContract.Picture.HORECA_ID + " = ?",
				new String[]{((Long) horeca.getId()).toString()});
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
	{return this.path+this+namefile;}
	
	private String path;
	private String namefile;
}
