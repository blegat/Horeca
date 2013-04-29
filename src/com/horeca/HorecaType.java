package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class HorecaType {
	public static Cursor getAllHorecaTypes(SQLiteDatabase db) {
		return getCursor(db, null, null);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.HorecaType.TABLE_NAME,
				HorecaContract.HorecaType.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private long id;
	private String name;
	
	public HorecaType (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.HorecaType._ID + " = ?", new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.HorecaType._ID_INDEX);
		name = cursor.getString(HorecaContract.HorecaType.NAME_INDEX);
		cursor.close();
	}
	
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
}