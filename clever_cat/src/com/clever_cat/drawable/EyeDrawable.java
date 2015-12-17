package com.clever_cat.drawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;

import com.clever_cat.loop.Clock;
import com.clever_cat.loop.CyclicTimer;
import com.clever_cat.loop.CyclicTimerProvider;

public class EyeDrawable extends Drawable {

	private static final double PUPIL_WIDTH_RATIO = 0.5;
	private static final double PUPIL_HEIGHT_RATIO = 0.4;

  private static final double IMAGE_RETRIEVAL_MARGIN = 1.5;

  private final CyclicTimer cameraImageRetrievalTimer =
      CyclicTimerProvider.getCyclicTimer(CyclicTimer.Id.CAMERA_IMAGE_RETRIEVAL);

	private ShapeDrawable eyeballDrawable;
	private ShapeDrawable pupilDrawable;

	private Rect rect;
	private SlidingPoint direction;

	public EyeDrawable(Clock clock) {
		eyeballDrawable = new ShapeDrawable(new OvalShape());
		eyeballDrawable.getPaint().setColor(Color.WHITE);
		pupilDrawable = new ShapeDrawable(new OvalShape());
		pupilDrawable.getPaint().setColor(Color.BLACK);
		direction = new SlidingPoint(clock, new Point(0, 0));
	}

	@Override
	public void setBounds(int left, int top, int right, int bottom) {
		super.setBounds(left, top, right, bottom);

		eyeballDrawable.setBounds(left, top, right, bottom);
		setPupilDirection(new Rect(-1, -1, 1, 1), new Point(0, 0));
	}

	@Override
	public void setAlpha(int alpha) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setColorFilter(ColorFilter cf) {
		// TODO Auto-generated method stub
	}

	@Override
	public int getOpacity() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setPupilDirection(Rect rect, Point point) {
		if (!rect.equals(this.rect)) {
			setPupilDirection(rect, point, 0);
		} else {
			setPupilDirection(rect, point,
			    (long) (cameraImageRetrievalTimer.getCycleTimeMillis() * IMAGE_RETRIEVAL_MARGIN));
		}
	}

	public void setPupilDirection(Rect rect, Point point, long inMillis) {
		this.rect = rect;
		this.direction.setDesiredPosition(point, inMillis);
	}

	@Override
	public void draw(Canvas canvas) {
		updatePupilDirection(rect, direction.getCurrentPosition());
		eyeballDrawable.draw(canvas);
		pupilDrawable.draw(canvas);
	}

	private void updatePupilDirection(Rect rect, Point point) {
		Rect eyeBounds = getBounds();
		int eyeWidth = eyeBounds.right - eyeBounds.left;
		int eyeHeight = eyeBounds.bottom - eyeBounds.top;
		int pupilWidth = (int) (eyeWidth * PUPIL_WIDTH_RATIO);
		int pupilHeight = (int) (eyeHeight * PUPIL_HEIGHT_RATIO);
		Rect pupilCornerBounds = new Rect(
				eyeBounds.left,
				eyeBounds.top,
				eyeBounds.right - pupilWidth,
				eyeBounds.bottom - pupilHeight);
		Point pupilCorner = RectUtil.mapPoint(point, rect, pupilCornerBounds);
		pupilDrawable.setBounds(
				pupilCorner.x,
				pupilCorner.y,
				pupilCorner.x + pupilWidth,
				pupilCorner.y + pupilHeight);
	}
}
