package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Ville {
	
	public static Cursor getAllVilles(SQLiteDatabase db) {
		return getCursor(db, null, null);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Ville.TABLE_NAME,
				HorecaContract.Ville.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private long id;
	private String name;
	private long codepostal;
	
	public Ville (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.Ville._ID + " = ?", new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Ville._ID_INDEX);
		name = cursor.getString(HorecaContract.Ville.NAME_INDEX);
		codepostal = cursor.getLong(HorecaContract.Ville.CODEPOSTAL_INDEX);
		cursor.close();
	}
	
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
	public long getCodepostal() {
		return codepostal;
	}
}
