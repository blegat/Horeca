package com.horeca;

import java.util.Date;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Ouverture {
	
	public static Cursor getAllOuverturesInHoreca(SQLiteDatabase db, Horeca horeca) {
		return getCursor(db,
				HorecaContract.Ouverture.HORECA_ID + " = ?",
				new String[]{((Long) horeca.getId()).toString()});
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Ouverture.TABLE_NAME,
				HorecaContract.Ouverture.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	
	private Horeca horeca;
	private long id;
	private long debut;
	private long fin;
	private boolean hasPlaces;
	private long places;
	
	public Ouverture (long id, SQLiteDatabase db) {
		Cursor cursor = getCursor(db,
				HorecaContract.Ouverture._ID + " == ?",
				new String[]{((Long) id).toString()});
		cursor.moveToFirst();
		this.id = cursor.getLong(HorecaContract.Ouverture._ID_INDEX);
		long horeca_id = cursor.getLong(HorecaContract.Ouverture.HORECA_ID_INDEX);
		debut = cursor.getLong(HorecaContract.Ouverture.DEBUT_INDEX);
		fin = cursor.getLong(HorecaContract.Ouverture.FIN_INDEX);
		hasPlaces = !cursor.isNull(HorecaContract.Ouverture.PLACES_INDEX);
		if (hasPlaces) {
			places = cursor.getLong(HorecaContract.Ouverture.PLACES_INDEX);
		}
		cursor.close();
		horeca = new Horeca(horeca_id, db);
	}
	
	private String timestampToString(long ts) {
    	Date d = new Date(ts);
    	return d.toString();
	}
	public long getId () {
		return id;
	}
	public Horeca getHoreca() {
		return horeca;
	}
	public String getDebut() {
		return timestampToString(debut);
	}
	public String getFin() {
		return timestampToString(fin);
	}
	public boolean hasPlaces() {
		return hasPlaces;
	}
	public long getPlaces() {
		return places;
	}
}