package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Ingredient {
	private long id;
	private String name;
	public Ingredient (long id, SQLiteDatabase db) {
		Cursor cursor = db.query(HorecaContract.Ingredient.TABLE_NAME,
				HorecaContract.Ingredient.COLUMN_NAMES,
				HorecaContract.Ingredient._ID + " == ?",
				new String[]{((Long) id).toString()},
				null, null, null);
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Ingredient._ID_INDEX);
		name = cursor.getString(HorecaContract.Ingredient.NAME_INDEX);
	}
	public long getId () {
		return id;
	}
	public String getName() {
		return name;
	}
}