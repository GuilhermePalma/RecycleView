package com.example.recyclerview.controller;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.recyclerview.model.People;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE = "PEOPLE";
    private static final int VERSION = 1;

    private final String TABLE_PEOPLE = "TB_PEOPLE";
    private final String ID = "id_people";
    private final String NAME = "name";
    private final String AGE = "age";

    public DataBaseHelper (Context context){
        super(context, DATABASE,null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "CREATE TABLE " + TABLE_PEOPLE + "(" +
                        ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        NAME + " VARCHAR(100) NOT NULL, " +
                        AGE + " INTEGER NOT NULL )"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PEOPLE);
        onCreate(db);
    }

    public int nextId(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;
        int next_id = 0;

        cursor = database.rawQuery("SELECT MAX(" + ID +") FROM " +
                TABLE_PEOPLE, null);

        if (cursor.moveToFirst()){
            next_id = cursor.getInt(0);
            return next_id + 1;
        }

        return next_id;
    }

    public int amountPeoples(){
        SQLiteDatabase database = this.getReadableDatabase();

        return (int) DatabaseUtils.queryNumEntries(database, TABLE_PEOPLE);
    }

    public void insertPeople(People people){
        SQLiteDatabase database = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ID, people.getId());
        values.put(NAME, people.getName());
        values.put(AGE, people.getAge());

        database.insert(TABLE_PEOPLE, null, values);
    }

    public Cursor selectAllPeoples(){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor;

        cursor = database.rawQuery("SELECT * FROM " + TABLE_PEOPLE, null);

        return cursor;
    }

    public void deletePeople(int id){
        SQLiteDatabase database = getWritableDatabase();

        database.delete(TABLE_PEOPLE, ID + "=" + id, null);
    }
}
