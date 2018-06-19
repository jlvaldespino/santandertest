package com.santander.demo.activities;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;



public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

     private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "santander_demo";

    private static final String TABLE_LOGIN = "login";

    private static final String KEY_ID = "id";
    private static final String KEY_USER = "user";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_NAME = "nombre";
    private static final String KEY_FIRTNAME = "apellido_paterno";
    private static final String KEY_SECONDNAME = "apellido_materno";
    private static final String KEY_BIRDAY = "fecha_nacimiento";
    private static final String KEY_SEX = "sexo";
    private static final String KEY_COUNTRY = "pais";
    private static final String KEY_STATE = "estado";
    private static final String KEY_LOG = "longitud";
    private static final String KEY_LAT = "latitud";






    private static final String KEY_CREATED_AT = "created_at";

    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_LOGIN + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_USER + " TEXT,"
                + KEY_EMAIL + " TEXT," + KEY_NAME + " TEXT,"
                + KEY_FIRTNAME + " TEXT," + KEY_SECONDNAME + " TEXT,"
                + KEY_BIRDAY + " TEXT," + KEY_SEX + " TEXT,"
                + KEY_COUNTRY + " TEXT," + KEY_STATE + " TEXT,"
                + KEY_LOG + " TEXT," + KEY_LAT + " TEXT,"
                + KEY_CREATED_AT + " TEXT" + ")";
        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tablas creadas");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);

        // Create tables again
        onCreate(db);
    }

    /**
     * Storing user details in database
     * */
    public void addUser(String user, String email, String name,String first_name,String second_name,String date,String sex,String country,String state,String lat,String log, String created_at) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, name);
        values.put(KEY_EMAIL, email);
        values.put(KEY_NAME, name);
        values.put(KEY_FIRTNAME, name);
        values.put(KEY_SECONDNAME, user);
        values.put(KEY_BIRDAY, date);
        values.put(KEY_SEX, sex);
        values.put(KEY_COUNTRY, country);
        values.put(KEY_STATE, state);
        values.put(KEY_LAT, lat);
        values.put(KEY_LOG, log);

        values.put(KEY_CREATED_AT, created_at);


        long id = db.insert(TABLE_LOGIN, null, values);
        db.close();

        Log.d(TAG, "Se inserto en sqlite el: " + id);
    }


    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_LOGIN;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("user", cursor.getString(1));
            user.put("email", cursor.getString(2));
            user.put("name", cursor.getString(3));
            user.put("created_at", cursor.getString(4));
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Obteniendo login de Sqlite: " + user.toString());

        return user;
    }


    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_LOGIN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();


        return rowCount;
    }


    public void deleteUsers() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_LOGIN, null, null);
        db.close();

        Log.d(TAG, "borrar todos los usuarios de  sqlite");
    }

}