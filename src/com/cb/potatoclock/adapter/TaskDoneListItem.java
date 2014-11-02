package com.cb.potatoclock.adapter;

public class TaskDoneListItem {
	private String taskName;
	private int workingTime;
	private String startTime;
	private String doneTime;
	private int id;
	
	public TaskDoneListItem(String taskName, int workingTime, String startTime,
			String doneTime, int id) {
		super();
		this.taskName = taskName;
		this.workingTime = workingTime;
		this.startTime = startTime;
		this.doneTime = doneTime;
		this.id = id;
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
	public int getWorkingTime() {
		return workingTime;
	}
	public void setWorkingTime(int workingTime) {
		this.workingTime = workingTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getDoneTime() {
		return doneTime;
	}
	public void setDoneTime(String doneTime) {
		this.doneTime = doneTime;
	}
	
}
