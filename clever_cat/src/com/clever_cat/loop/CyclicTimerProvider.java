package com.clever_cat.loop;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import android.util.Log;

public class CyclicTimerProvider {

	private static final int LOG_PERIOD_SECS = 1;
	
	private static Map<String, CyclicTimer> cyclicTimers =
			new TreeMap<String, CyclicTimer>();
	private static Timer timer;
	
	public static CyclicTimer newCyclicTimer(String name) {
		CyclicTimer cyclicTimer = cyclicTimers.get(name);
		if (cyclicTimer == null) {
			cyclicTimer = new CyclicTimer(name);
			cyclicTimers.put(name, cyclicTimer);
		}
		return cyclicTimer;
	}
	
	public static void deleteCyclicTimer(CyclicTimer cyclicTimer) {
		cyclicTimers.remove(cyclicTimer.getName());
	}

	public static void startLogging() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (CyclicTimer cyclicTimer : cyclicTimers.values()) {
					float fps = (float) TimeUnit.SECONDS.toMillis(1) / cyclicTimer.getCycleTimeMillis();
					Log.i("CyclicTimer", String.format("%s runs on average %d ms every %d ms (%.1f FPS).",
							cyclicTimer.getName(), cyclicTimer.getPassTimeMillis(), cyclicTimer.getCycleTimeMillis(), fps));
				}
			}
		}, 0, TimeUnit.SECONDS.toMillis(LOG_PERIOD_SECS));
	}
	
	public static void stopLogging() {
		timer.cancel();
	}
}
