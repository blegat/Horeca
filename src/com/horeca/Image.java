package com.horeca;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Image {
	
	public Image(String p,String nf){
	this.path=p;
	this.namefile=nf;
	}
	
	public Image(Cursor c){
		this.path=c.getString(HorecaContract.HorecaPictures.PATH_PICTURE_INDEX);
		this.namefile=c.getString(HorecaContract.HorecaPictures.NAME_PICTURE_INDEX);
	}
	
	public String getPath()
	{return this.path;}
	public String getNameFile()
	{return this.namefile;}
	public String getCompletePath()
	{return this.path+this+namefile;}
	
	private String path;
	private String namefile;
}
