package com.horeca;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Filter {
	private String LIMIT = "30"; // maximum number of restaurant to be selected
	
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
	
	private static String addSort(String sort, String newSort) {
		if (sort == null) {
			return newSort;
		} else {
			return sort + ", " + newSort;
		}
	}
	
	private String op (String a, String b, String op) {
		return "(" + a + " " + op + " " + b + ")";
	}
	private String times (String a, String b) {
		return op(a, b, "*");
	}
	private String plus (String a, String b) {
		return op(a, b, "+");
	}
	private String minus (String a, String b) {
		return op(a, b, "-");
	}
	private String div (String a, String b) {
		return op(a, b, "/");
	}
	private String taylor (double[] cs, double a, String x) {
		String out = "0";
		String xma = "";
		long fact = 1;
		for (int i = 0; i < cs.length; i++) {
			if (i == 0) {
				xma = minus(x, String.valueOf(a));
			} else {
				fact *= i;
				xma = times(xma, minus(x, String.valueOf(a)));
			}
			out = plus(out, times(String.valueOf(cs[i]), div(xma, String.valueOf(fact))));
		}
		return out;
	}
	private String sin (String x, double a) {
		return taylor(new double[] {Math.sin(a), Math.cos(a)}, a, x);
	}
	private String cos (String x, double a) {
		return taylor(new double[] {Math.cos(a), -Math.sin(a)}, a, x);
	}
	private String acos (String x, double a) {
		return taylor(new double[] {Math.acos(a), -1/Math.sqrt(1-a*a)}, a, x);
	}
	
	private String degToRad (String deg) {
		return times(deg, "0.01745327");
	}
	private double degToRad (double deg) {
		return deg * 0.01745327;
	}
	
	private String dbToRad (String db) {
		return degToRad(div(db, "1000000."));
	}
	
	public Horeca[] getMatchingHorecas(SQLiteDatabase db, Context context, GPSTracker gps) {
		String tables = HorecaContract.Horeca.TABLE_NAME_Q + ", " + HorecaContract.UserFavoriteHoreca.TABLE_NAME_Q;
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
		String sort = null;
		if (User.isSignedIn()) {
			sort = addSort(sort, "(SELECT COUNT(*) FROM " + HorecaContract.UserFavoriteHoreca.TABLE_NAME_Q 
				+ " WHERE " + HorecaContract.UserFavoriteHoreca.HORECA_ID_Q + " = " 
				+ HorecaContract.Horeca._ID_Q +" AND " + HorecaContract.UserFavoriteHoreca.USER_ID_Q 
				+ " = " + User.getCurrentUser().getId()  + ") DESC");
		}
		if (gps != null) {
			Log.i("gps","fetched");
			double longitude = gps.getLongitude();
			double latitude = gps.getLatitude();
			/*String lat1 = dbToRad(HorecaContract.Horeca.LONGITUDE_Q);
			String lon1 = dbToRad(HorecaContract.Horeca.LATITUDE_Q);
			double lat2 = degToRad(latitude);
			double lon2 = degToRad(longitude);*/
			String distSquared = "(" +
					HorecaContract.Horeca.LONGITUDE_Q + " / 1000000. - " + longitude + ")*(" +
					HorecaContract.Horeca.LONGITUDE_Q + " / 1000000. - " + longitude + ")+(" +
					HorecaContract.Horeca.LATITUDE_Q + " / 1000000. - " + latitude + ")*(" +
					HorecaContract.Horeca.LATITUDE_Q + " / 1000000. - " + latitude + ")";
			/*String dist = times(acos(plus(times(sin(lat1, lat2), String.valueOf(Math.sin(lat2))), times(times(cos(lat1, lat2),
					String.valueOf(Math.cos(lat2))),
					cos(minus(String.valueOf(lon2), lon1), 0))),
					Math.sin(lat2) * Math.sin(lat2) + Math.cos(lat2) * Math.cos(lat2)),
					"6378100.");*/
			if (hasMaxDistance) {
				where = where + " AND " + distSquared + " < " + String.valueOf(maxDistance*maxDistance);
			}
			sort = addSort(sort, distSquared + " ASC");
		}
		Log.i("where", where);
		// We need to add UNIQ because the restaurant could have
		// several plats with the good type
		Cursor cursor = db.query(true, tables,
				HorecaContract.Horeca.COLUMN_NAMES_Q,
				where, null, null, null, sort, LIMIT);
		cursor.moveToFirst();
		Horeca[] horecas = new Horeca[cursor.getCount()];
		int n = 0;
		while (!cursor.isAfterLast()) {
			horecas[n] = new Horeca(cursor);
			if (gps != null && hasMaxDistance) {
				if (horecas[n].getDistance(gps) > maxDistance) {
					n--;
				}
			}
			n++;
			cursor.moveToNext();
		}
		if (n < horecas.length) {
			Horeca[] tmp = horecas;
			horecas = new Horeca[n];
			for (int i = 0; i < n; i++) {
				horecas[i] = tmp[i];
			}
		}
		return horecas;
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
