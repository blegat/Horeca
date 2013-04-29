package com.horeca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Filter {
	private Ville ville;
	private HorecaType horecaType = null;
	private PlatType platType = null;
	private boolean hasMaxDistance = false;
	private double maxDistance;
	
	public Cursor getMatchingHorecas(SQLiteDatabase db, Context context) {
		double longitude = Location.getCurrentLongitude(context);
		double latitude = Location.getCurrentLatitude(context);
		String distSquared = "(" +
				HorecaContract.Horeca.LONGITUDE_Q + " - " + longitude + ")*(" +
				HorecaContract.Horeca.LONGITUDE_Q + " - " + longitude + ")+(" +
				HorecaContract.Horeca.LATITUDE_Q + " - " + latitude + ")*(" +
				HorecaContract.Horeca.LATITUDE_Q + " - " + latitude + ")";
		String tables = HorecaContract.Horeca.TABLE_NAME_Q;
		String where = HorecaContract.Horeca.VILLE_ID_Q + " = " + String.valueOf(ville.getId());
		if (horecaType != null) {
			tables = tables + ", " + HorecaContract.HorecaType.TABLE_NAME_Q + ", " + HorecaContract.HorecaTypeJoin.TABLE_NAME_Q;
			where = where + " AND " + HorecaContract.Horeca._ID_Q + " = " + HorecaContract.HorecaTypeJoin.HORECA_ID_Q;
			where = where + " AND " + HorecaContract.HorecaType._ID_Q + " = " + HorecaContract.HorecaTypeJoin.HORECATYPE_ID_Q;
			where = where + " AND " + HorecaContract.HorecaType._ID_Q + " = " + horecaType.getId();
		}
		if (platType != null) {
			tables = tables + ", " + HorecaContract.Plat.TABLE_NAME_Q + ", " +
					HorecaContract.PlatType.TABLE_NAME_Q + ", " + HorecaContract.PlatTypeJoin.TABLE_NAME_Q;
			where = where + " AND " + HorecaContract.Horeca._ID_Q + " = " + HorecaContract.Plat.HORECA_ID_Q;
			where = where + " AND " + HorecaContract.Plat._ID_Q + " = " + HorecaContract.PlatTypeJoin.PLAT_ID_Q;
			where = where + " AND " + HorecaContract.PlatType._ID_Q + " = " + HorecaContract.PlatTypeJoin.PLATTYPE_ID_Q;
			where = where + " AND " + HorecaContract.PlatType._ID_Q + " = " + platType.getId();
		}
		if (hasMaxDistance) {
			where = where + " AND " + distSquared + " < " + String.valueOf(maxDistance*maxDistance);
		}
		// We need to add UNIQ because the restaurant could have
		// several plats with the good type
		return db.query(true, tables,
				HorecaContract.Horeca.COLUMN_NAMES_Q,
				where, null, null, null, distSquared + " ASC", null);
	}
	
	public void setVille(Ville ville) {
		this.ville = ville;
	}
	public void setHorecaType(HorecaType horecaType) {
		this.horecaType = horecaType;
	}
	public void setPlatType(PlatType platType) {
		this.platType = platType;
	}
	public void setMaxDistance(double dist) {
		hasMaxDistance = true;
		maxDistance = dist;
	}
}