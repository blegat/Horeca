package com.horeca;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Reservation {
	/*
	 * @pre the user is signed in !
	 */
	public static Reservation getReservationForOuverture(SQLiteDatabase db, Ouverture ouverture) {
		Cursor cursor = getCursor(db, HorecaContract.Reservation.OUVERTURE_ID + " = ? AND " +
				HorecaContract.Reservation.USER_ID + " = ?", // TODO sort it
				new String[]{String.valueOf(ouverture.getId()), String.valueOf(User.getCurrentUser().getId())});
		if (cursor.getCount() == 0) {
			return null;
		}
		cursor.moveToFirst();
		long id = cursor.getLong(HorecaContract.Reservation._ID_INDEX);
		long user_id = cursor.getLong(HorecaContract.Reservation.USER_ID_INDEX);
		long ouverture_id = cursor.getLong(HorecaContract.Reservation.OUVERTURE_ID_INDEX);
		long places = cursor.getLong(HorecaContract.Reservation.PLACES_INDEX);
		return new Reservation(id, new User(user_id, db), new Ouverture(ouverture_id, db), places);
	}
	private static Cursor getCursor(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Reservation.TABLE_NAME,
				HorecaContract.Reservation.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	public static Reservation createReservation(SQLiteDatabase db, Ouverture ouverture, long places) {
		User user = User.getCurrentUser();
		ContentValues cv = new ContentValues();
		cv.put(HorecaContract.Reservation.USER_ID, user.getId());
		cv.put(HorecaContract.Reservation.OUVERTURE_ID, ouverture.getId());
		cv.put(HorecaContract.Reservation.PLACES, places);
		long id = db.insert(HorecaContract.Reservation.TABLE_NAME, null, cv);
		if (ouverture.hasPlaces()) {
			ouverture.setPlaces(db, ouverture.getPlaces() - places);
		}
		return new Reservation(id, user, ouverture, places);
	}
	
	private long id;
	private User user;
	private Ouverture ouverture;
	private long places;
	private Reservation (long id, User user, Ouverture ouverture, long places) {
		this.id = id;
		this.user = user;
		this.ouverture = ouverture;
		this.places = places;
	}
	public long getId() {
		return id;
	}
	public User getUser() {
		return user;
	}
	public Ouverture getOuverture() {
		return ouverture;
	}
	public long getPlaces() {
		return places;
	}
	public void setPlaces(SQLiteDatabase db, long places) {
		if (ouverture.hasPlaces()) {
			ouverture.setPlaces(db, ouverture.getPlaces() + this.places - places);
		}
		this.places = places;
	    ContentValues args = new ContentValues();
	    args.put(HorecaContract.Reservation.PLACES, this.places);
	    db.update(HorecaContract.Reservation.TABLE_NAME, args, HorecaContract.Reservation._ID + " = ?",
	    		new String[]{String.valueOf(this.id)});
	}
	public void destroy(SQLiteDatabase db) {
		db.delete(HorecaContract.Reservation.TABLE_NAME, HorecaContract.Reservation._ID + " = ?",
				new String[]{String.valueOf(id)});
		ouverture.setPlaces(db, ouverture.getPlaces() + this.places);
	}
}