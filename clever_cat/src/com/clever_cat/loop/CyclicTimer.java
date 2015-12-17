package com.clever_cat.loop;

public class CyclicTimer {

  public static enum Id {
    CAMERA_IMAGE_RETRIEVAL("Camera image retrieval"),
    VIEW_REDRAWING("View redrawing");

    private final String name;

    private Id(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }

	/**
	 * Convergence constant.
	 * {@code new_t = old_t * Q + meaured_t * (1-Q)}
	 */
	private static final double Q = 0.9;

	private Id id;
	private long currentBeforeMillis;

	private long millisSinceThisBefore;
	private long millisSincePreviousBefore;

	private long passTimeMillis;
	private long cycleTimeMillis;

	public CyclicTimer(Id id) {
		this.id = id;
		this.currentBeforeMillis = System.currentTimeMillis();
	}

	public void measureBefore() {
		long lastBeforeMillis = currentBeforeMillis;
		currentBeforeMillis = System.currentTimeMillis();
		updateCycleTime(currentBeforeMillis - lastBeforeMillis);
	}

	public void measureAfter() {
		long currentAfterMillis = System.currentTimeMillis();
		updatePassTime(currentAfterMillis - currentBeforeMillis);
	}

	private void updateCycleTime(long currentCycleTimeMillis) {
		millisSincePreviousBefore = currentCycleTimeMillis;
		cycleTimeMillis = converge(cycleTimeMillis, currentCycleTimeMillis);
	}

	private void updatePassTime(long currentPassTimeMillis) {
		millisSinceThisBefore = currentPassTimeMillis;
		passTimeMillis = converge(passTimeMillis, currentPassTimeMillis);
	}

	private static long converge(long convergedPassTimeMillis, long currentPassTimeMillis) {
		return (long) (Q * convergedPassTimeMillis + (1 - Q) * currentPassTimeMillis);
	}

	public String getName() {
		return id.getName();
	}

	public long getPassTimeMillis() {
		return passTimeMillis;
	}

	public long getCycleTimeMillis() {
		return cycleTimeMillis;
	}

	public long millisSinceThisBefore() {
		return millisSinceThisBefore;
	}

	public long millisSincePreviousBefore() {
		return millisSincePreviousBefore;
	}
}
