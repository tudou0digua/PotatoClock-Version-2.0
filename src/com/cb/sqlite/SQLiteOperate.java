package com.cb.sqlite;

import android.database.Cursor;

public interface SQLiteOperate {
	
	public void addDone(String taskName, long startTime, long doneTime, int workingTime);
	public void deleteDone(int id);
	public Cursor queryDoneDate(String date);
	public void addToDo(String taskToDoName);
	public void deleteToDo(int id);
	public Cursor queryToDoList();
}
