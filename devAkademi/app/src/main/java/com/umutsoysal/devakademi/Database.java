package com.umutsoysal.devakademi;

import java.util.ArrayList;
import java.util.HashMap;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Database extends SQLiteOpenHelper {


    private static final int DATABASE_VERSION = 2;

    // Database Name
    private static final String DATABASE_NAME = "sqllite_database";

    private static final String TABLE_NAME = "Coin";
    private static String ID = "id";
    private static String LAST = "fiyat";
    private static String LOW = "tarih";

    private static final String TABLE = "alarm";
    private static String ALARM_ID = "id";
    private static String MONEY = "money";
    private static String BTC_ID = "btc_id";


    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + LAST + " TEXT,"
                + LOW + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);

        String CREATE_TABLE2 = "CREATE TABLE " + TABLE + "("
                + ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BTC_ID + " TEXT,"
                + MONEY + " TEXT" + ")";
        db.execSQL(CREATE_TABLE2);

    }



    public void islemEkle(String last, String low) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LAST, last);
        values.put(LOW, low);
        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public void alarmEkle(String last,String btc_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MONEY, last);
        values.put(BTC_ID,btc_id);
        db.insert(TABLE, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }
    public void alarmSil(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE + " where ALARM_ID='" + id + "'");
        db.close();
    }

    public int getRowCount() {
        // Bu method bu uygulamada kullanılmıyor ama her zaman lazım olabilir.Tablodaki row sayısını geri döner.
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }

    public  ArrayList<HashMap<String, String>> Kurlar(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> oturumlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                oturumlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return oturumlist;
    }

    public  ArrayList<HashMap<String, String>> ALARMLAR(){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> oturumlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                oturumlist.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return oturumlist;
    }

    public void HepsiniSilAlarm(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, null, null);
        db.close();
    }

    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }


    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }

}
