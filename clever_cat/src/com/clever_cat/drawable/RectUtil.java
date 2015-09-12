package com.clever_cat.drawable;

import android.graphics.Point;
import android.graphics.Rect;

/**
 * Utility class for {@link Rect}.
 */
public final class RectUtil {

	/**
	 * Maps 2D {@code point} from rectangle {@code fromRect} to rectange {@code toRect}.
	 */
	public static Point mapPoint(Point point, Rect fromRect, Rect toRect) {
		int x = (int) mapPoint(point.x, fromRect.left,  fromRect.right, toRect.left, toRect.right);
		int y = (int) mapPoint(point.y, fromRect.top,  fromRect.bottom, toRect.top, toRect.bottom);
		return new Point(x, y);
	}
	
	/**
	 * Maps 1D {@code point} from segment [{@code fromA}, {@code fromB}] to segment [{@code toA}, {@code toB}].
	 */
	private static double mapPoint(double point, double fromA, double fromB, double toA, double toB) {
		double ratio = (point - fromA) / (fromB - fromA);
		return toA + (toB - toA) * ratio;
	}
	
	private RectUtil() {}
}
