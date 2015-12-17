package com.clever_cat.loop;

import java.util.concurrent.TimeUnit;

import com.clever_cat.view.CatView;

public class MainLoop {
	
	private static final long MILLIS_IN_SECOND = TimeUnit.SECONDS.toMillis(1L);
	private static final int EXPECTED_FPS = 30;
	
	private CatView catView;
	private Thread thread;
	private boolean isActive;
	private final DiscreteClock discreteClock;
	
	public MainLoop(CatView catView, DiscreteClock discreteClock) {
		this.catView = catView;
		this.discreteClock = discreteClock;
	}
	
	public synchronized void start() {
	  if (isActive) {
	    return;
	  }
		isActive = true;
		thread = new Thread(new ViewRefresher());
		thread.start();
	}
	
	public synchronized void stop() {
	  if (!isActive) {
	    return;
	  }
		isActive = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class ViewRefresher implements Runnable {
		@Override
		public void run() {
			CyclicTimer cyclicTimer = CyclicTimerProvider.getCyclicTimer(CyclicTimer.Id.VIEW_REDRAWING);
			try {
				while(isActive) {
					discreteClock.setTimeMillis(System.currentTimeMillis());
					cyclicTimer.measureBefore();
					catView.redraw();
					cyclicTimer.measureAfter();
					long remainingMillis =
					    MILLIS_IN_SECOND / EXPECTED_FPS - cyclicTimer.millisSinceThisBefore();
					Thread.sleep(Math.max(remainingMillis, 0));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
