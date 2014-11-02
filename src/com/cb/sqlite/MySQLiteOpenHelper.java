package com.cb.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	private static final int VERSION = 1;
	private static final String DATABASE_NAME = "potato";
	private static final String TABLE_NAME = "task_done";
	private static final String TABLE_NAME_TODO = "task_todo";
	private static final String _ID = "_id";
	private static final String DATE = "date";
	private static final String TASK_NAME = "task_name";
	private static final String TIME_START = "time_start";
	private static final String TIME_DONE = "time_done";
	private static final String TIME_WORKING = "time_working";
	private static final String CREATE_TABLE = "create table " + TABLE_NAME + " ( " + _ID + " integer primary key autoincrement, " 
		+ DATE + " text, " + TIME_START + " integer, " + TASK_NAME + " text, " + TIME_DONE + " integer, " + TIME_WORKING + " integer);";  
	private static final String CREATE_TABLE_TODO = "create table " + TABLE_NAME_TODO + " ( " + _ID + " integer primary key autoincrement, "
			+ TASK_NAME + " text);";
	
	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_TABLE_TODO);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
