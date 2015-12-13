package com.clever_cat.loop;

public class DiscreteClock implements Clock {
	
	private long timeMillis;
	
	public long getTimeMillis() {
		return timeMillis;
	}
	
	public void setTimeMillis(long timeMillis) {
		this.timeMillis = timeMillis;
	}
}
