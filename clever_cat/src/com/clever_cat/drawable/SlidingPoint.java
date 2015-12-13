package com.clever_cat.drawable;

import com.clever_cat.loop.Clock;

import android.graphics.Point;

/**
 * A point container, which allows to move point smoothly in time.
 */
public class SlidingPoint {

	private final Clock clock;
	
	private Point currentPos;
	private Point desiredPos;
	private long currentTimeMillis;
	private long desiredTimeMillis;
	
	public SlidingPoint(Clock clock, Point point) {
		this.clock = clock;
		this.currentPos = point;
		currentTimeMillis = clock.getTimeMillis();
	}

	public void setDesiredPosition(Point point, long inMillis) {
		currentTimeMillis = clock.getTimeMillis();
		desiredTimeMillis = currentTimeMillis + inMillis;
		desiredPos = point;
	}
	
	public Point getCurrentPosition() {
		updateCurrentPosition();
		return currentPos;
	}

	private void updateCurrentPosition() {
		long updatedTimeMillis = clock.getTimeMillis();
		
		if (updatedTimeMillis > desiredTimeMillis) {
			currentPos = desiredPos;
			return;
		}
		
		double elapsedFraction = (double) (updatedTimeMillis - currentTimeMillis)
		    / (desiredTimeMillis - currentTimeMillis);
		Point updatedDirection = new Point(
				(int) (currentPos.x + (desiredPos.x - currentPos.x) * elapsedFraction),
				(int) (currentPos.y + (desiredPos.y - currentPos.y) * elapsedFraction));
		currentTimeMillis = updatedTimeMillis;
		currentPos = updatedDirection;
	}
}
