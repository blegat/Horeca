package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Filter {
	private Ville ville;
	
	public Cursor getMatchingHorecas(SQLiteDatabase db) {
		return db.query(HorecaContract.Horeca.TABLE_NAME,
				HorecaContract.Horeca.COLUMN_NAMES,
				HorecaContract.Horeca.VILLE_ID + " = ?",
				new String[]{String.valueOf(ville.getId())}, null, null, null);
	}
	
	public void setVille(Ville ville) {
		this.ville = ville;
	}
}