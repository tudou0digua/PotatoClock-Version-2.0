package com.cb.potatoclock.adapter;

public class TaskToDoListItem {
	private int id;
	private String taskName;
	
	public TaskToDoListItem(int id, String taskName) {
		super();
		this.id = id;
		this.taskName = taskName;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTaskName() {
		return taskName;
	}
	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
	
}
