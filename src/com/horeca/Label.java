package com.horeca;

import java.util.Vector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Label {
	
	private Picture image;
	private String Description;
	private static String DefaultDescription="Ce label ne contient pas de description. Contactez l'administrateur système car c'est ce qu'il faut dire quand on ne sais pas vous aider. A ne pas consomer sans avis médical.";
	private static String DefaultPath="https://encrypted-tbn1.gstatic.com/images?q=tbn:";
	private static String DefaultName="ANd9GcQnhVhJaenGcxmb3a47IZN_PVo6ajoIqJX_UDnRbo1vxDFjvaui4g";
	
	public Label(Cursor c){
		this.image= new Picture (c.getString(HorecaContract.Label.PATH_INDEX),c.getString(HorecaContract.Label.NAME_INDEX));
		this.Description=c.getString(HorecaContract.Label.DESCRIPTION_INDEX);
	}
	
	
	public Label(String p, String nf, String desc){
		this.image= new Picture (p,nf);
		this.Description=desc;
	}
	
	public Label(String p, String nf){
		this.image= new Picture (p,nf);
		this.Description=DefaultDescription;
	}
	
	String getDescription(){
		return Description;
	}
	
	public static String getDefaultPath(){
		return DefaultPath;
	}
	public static String getDefaultName(){
		return DefaultName;
	}
	public static String getDefaultDescription(){
		return DefaultDescription;
	}
	
	Picture getPicture(){
		return image;
	}
	
	public static Cursor getCursorIdForLabel(SQLiteDatabase db, Horeca horeca) {
		return getCursorLabelJoinHoreca(db,
				HorecaContract.LabelJoinHoreca.HORECA_ID + " = ?",
				new String[]{((Long) horeca.getId()).toString()});
	}
	
	public static Cursor getCursorForLabel(SQLiteDatabase db, long labelId) {
		return getCursorLabel(db,
				HorecaContract.Label._ID + " = ?",
				new String[]{((Long) labelId).toString()});
	}
	
	public static Vector<Label> getVectorLabelForHoreca(SQLiteDatabase db, Horeca horeca){
		
		Vector<Label> veclabel = new Vector<Label>(0);
		Cursor c=getCursorIdForLabel(db,horeca);
		Cursor Cursortemp;
		if(c.isAfterLast()){
			Label temp=new Label(getDefaultPath(),getDefaultName(),getDefaultDescription());
			veclabel.add(temp);
		}
		else{
			c.moveToFirst();
			while(!c.isAfterLast()){
				Cursortemp=getCursorForLabel(db,c.getLong(HorecaContract.LabelJoinHoreca.LABEL_ID_INDEX));
				if(Cursortemp.getCount()>0){
					Cursortemp.moveToFirst();
					Label temp=new Label(Cursortemp);
					veclabel.add(temp);
				}
				c.moveToNext();
			}
			if(veclabel.size()==0){
				Label temp=new Label(getDefaultPath(),getDefaultName(),getDefaultDescription());
				veclabel.add(temp);
			}
		}
		return veclabel;
	}
	private static Cursor getCursorLabelJoinHoreca(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.LabelJoinHoreca.TABLE_NAME,
				HorecaContract.LabelJoinHoreca.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
	private static Cursor getCursorLabel(SQLiteDatabase db, String selection, String[] selectionArgs) {
		return db.query(HorecaContract.Label.TABLE_NAME,
				HorecaContract.Label.COLUMN_NAMES, selection, selectionArgs, null, null, null);
	}
}
