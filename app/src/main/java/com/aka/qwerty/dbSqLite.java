package com.aka.qwerty;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class dbSqLite extends SQLiteOpenHelper {

	public static String DB_NAME = "db_depo";
	private String TB_ISTATISTIK = "TB_ISTATISTIK";

	public dbSqLite(Context context) {

		super(context, DB_NAME, null, 1);

	}

	private dbSqLite(Context context, String name, CursorFactory factory,
			int version) {
		super(context, DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql1 = "CREATE TABLE " + TB_ISTATISTIK
				+ "(ID INTEGER PRIMARY KEY AUTOINCREMENT," + "ist_ID INTEGER ,"
				+ "ist_konu TEXT," + "ist_yazi TEXT," + "ist_tag TEXT,"
				+ "ist_tarih TEXT," + "ist_okunmasayisi INTEGER)";

		Log.d("dbSqLite", "SQL : " + sql1);
		db.execSQL(sql1);
		// ////////

		Log.i("dbSqLite", "onCreate");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		Log.d("dbSqLite", "onUpgrade");
		db.execSQL("DROP TABLE IF EXISTS " + TB_ISTATISTIK);
		onCreate(db);
	}

	// ///////////////
	public void insertIstatistik(obj_IstatistikPaylasim ist) {
		ContentValues values = new ContentValues();
		values.put("ist_konu", ist.getIst_konu());
		values.put("ist_tag", ist.getIst_tag());
		values.put("ist_tarih", ist.getIst_tarih());
		values.put("ist_yazi", ist.getIst_yazi());
		values.put("ist_ID", ist.getIst_ID());
		values.put("ist_okunmasayisi", ist.getIst_okunmasayisi());

		SQLiteDatabase qq = this.getWritableDatabase();
		qq.insertOrThrow(this.TB_ISTATISTIK, null, values);
		qq.close();
	}

	public List<obj_IstatistikPaylasim> getIstatistikList() {
		SQLiteDatabase qq = this.getReadableDatabase();
		List<obj_IstatistikPaylasim> liste = new ArrayList<obj_IstatistikPaylasim>();
		Cursor cursor = qq.query(TB_ISTATISTIK, new String[] { "ist_ID",
				"ist_konu", "ist_yazi", "ist_tag", "ist_tarih",
				"ist_okunmasayisi" }, null, null, null, null, "ist_ID DESC");

		while (cursor.moveToNext()) {
			obj_IstatistikPaylasim aab = new obj_IstatistikPaylasim();
			aab.setIst_ID(cursor.getInt(0));
			aab.setIst_konu(cursor.getString(1));
			aab.setIst_yazi(cursor.getString(2));
			aab.setIst_tag(cursor.getString(3));
			aab.setIst_tarih(cursor.getString(4));
			aab.setIst_okunmasayisi(cursor.getInt(5));

			liste.add(aab);

		}

		return liste;

	}

	public void ReloadTables(SQLiteDatabase db) {
		Log.d("dbSqLite", "ReloadTables");
		db.execSQL("DROP TABLE IF EXISTS " + TB_ISTATISTIK);
		onCreate(db);
	}

}
