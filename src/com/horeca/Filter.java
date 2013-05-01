package com.horeca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Filter {
	private Ville ville;
	private HorecaType horecaType = null;
	private PlatType platType = null;
	private Ingredient ingredient = null;
	private boolean hasMinPrice = false;
	private double minPrice;
	private boolean hasMaxPrice = false;
	private double maxPrice;
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
		if (platType != null || ingredient != null) {
			// We need the plats
			tables = tables + ", " + HorecaContract.Plat.TABLE_NAME_Q;
			where = where + " AND " + HorecaContract.Horeca._ID_Q + " = " + HorecaContract.Plat.HORECA_ID_Q;
		}
		if (platType != null) {
			tables = tables + ", " +
					HorecaContract.PlatType.TABLE_NAME_Q + ", " + HorecaContract.PlatTypeJoin.TABLE_NAME_Q;
			where = where + " AND " + HorecaContract.Plat._ID_Q + " = " + HorecaContract.PlatTypeJoin.PLAT_ID_Q;
			where = where + " AND " + HorecaContract.PlatType._ID_Q + " = " + HorecaContract.PlatTypeJoin.PLATTYPE_ID_Q;
			where = where + " AND " + HorecaContract.PlatType._ID_Q + " = " + platType.getId();
		}
		if (ingredient != null) {
			tables = tables + ", " + HorecaContract.Contient.TABLE_NAME_Q + ", " +
					HorecaContract.Ingredient.TABLE_NAME_Q;
			where = where + " AND " + HorecaContract.Plat._ID_Q + " = " + HorecaContract.Contient.PLAT_ID_Q;
			where = where + " AND " + HorecaContract.Ingredient._ID_Q + " = " + HorecaContract.Contient.INGREDIENT_ID_Q;
			where = where + " AND " + HorecaContract.Ingredient.NAME_Q + " = \"" + ingredient.getName() + "\"";
		}
		if (hasMinPrice) {
			where = where + " AND " + HorecaContract.Horeca.MAX_PRICE_Q + " >= " + ((long) (minPrice * 100));
		}
		if (hasMaxPrice) {
			where = where + " AND " + HorecaContract.Horeca.MIN_PRICE_Q + " <= " + ((long) (maxPrice * 100));
		}
		if (hasMaxDistance) {
			where = where + " AND " + distSquared + " < " + String.valueOf(maxDistance*maxDistance);
		}
		Log.i("where", where);
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
	public void setIngredient(Ingredient ingredient) {
		this.ingredient = ingredient;
	}
	public void setMinPrice(double price) {
		hasMinPrice = true;
		minPrice = price;
	}
	public void setMaxPrice(double price) {
		hasMaxPrice = true;
		maxPrice = price;
	}
	public void setMaxDistance(double dist) {
		hasMaxDistance = true;
		maxDistance = dist;
	}
}