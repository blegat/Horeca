package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Horeca {
	private long id;
	private String name;
	private String description;
	SQLiteDatabase db;
	public Horeca (long id, SQLiteDatabase db) {
		this.db = db;
		Cursor cursor = db.query(HorecaContract.Horeca.TABLE_NAME,
				HorecaContract.Horeca.COLUMN_NAMES,
				HorecaContract.Horeca._ID + " == ?",
				new String[]{((Long) id).toString()},
				null, null, null);
		cursor.moveToFirst();
		id = cursor.getLong(HorecaContract.Horeca._ID_INDEX);
		name = cursor.getString(HorecaContract.Horeca.NAME_INDEX);
		description = cursor.getString(HorecaContract.Horeca.DESCRIPTION_INDEX);
	}
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
	public boolean hasDescription() {
		return description != null;
	}
	public String getDescription() {
		return description;
	}
}
