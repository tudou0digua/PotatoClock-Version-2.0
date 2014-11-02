package com.cb.sqlite;

import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteDao implements SQLiteOperate{
	private MySQLiteOpenHelper mySQLiteOpenHelper;
	private SQLiteDatabase db;
	private final String TABLE_TASK_DONE = "task_done";
	private final String TABLE_TASK_TODO = "task_todo";
	
	public SQLiteDao(Context context){
		mySQLiteOpenHelper = new MySQLiteOpenHelper(context);
	}
	
	public void openDB(){
		db = mySQLiteOpenHelper.getReadableDatabase();
	}
	
	public void closeDB(){
		db.close();
	}
	
	@Override
	public void addDone(String taskName, long startTime, long doneTime, int workingTime) {
		openDB();
		ContentValues cv = new ContentValues();
		String date = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault()).format(startTime);
		cv.put("date",date);
		cv.put("time_start", startTime);
		cv.put("time_done", doneTime);
		cv.put("time_working", workingTime);
		cv.put("task_name",taskName);
		db.insert(TABLE_TASK_DONE, null, cv);
		db.close();
	}

	@Override
	public void deleteDone(int id) {
		openDB();
		db.delete(TABLE_TASK_DONE, "_id=?", new String[]{String.valueOf(id)});
		db.close();
	}

	@Override
	public Cursor queryDoneDate(String date) {
		openDB();
		String[] columns = {"time_start","time_done","time_working","_id","task_name"};
		String selection = "date LIKE?";
		String[] selectionArgs = {"%" + date + "%"};
		Cursor cursor = db.query(TABLE_TASK_DONE, columns, selection, selectionArgs, null, null, null);
		
		return cursor;
	}

	@Override
	public void addToDo(String taskToDoName) {
		openDB();
		ContentValues cv = new ContentValues();
		cv.put("task_name",taskToDoName);
		db.insert(TABLE_TASK_TODO, null, cv);
		db.close();
	}

	@Override
	public void deleteToDo(int id) {
		openDB();
		db.delete(TABLE_TASK_TODO, "_id=?", new String[]{String.valueOf(id)});
		db.close();
	}

	@Override
	public Cursor queryToDoList() {
		openDB();
		String[] columns = {"_id","task_name"};
		Cursor cursor = db.query(TABLE_TASK_TODO, columns, null, null, null, null, null);
		return cursor;
	}

}
