package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PlatType {
	public static Cursor getAllPlatTypes(SQLiteDatabase db) {
		return getCursor(db, null, null);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.PlatType.TABLE_NAME,
				HorecaContract.PlatType.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private long id;
	private String name;
	
	public PlatType (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db, HorecaContract.PlatType._ID + " = ?", new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.PlatType._ID_INDEX);
		name = cursor.getString(HorecaContract.PlatType.NAME_INDEX);
		cursor.close();
	}
	
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
}