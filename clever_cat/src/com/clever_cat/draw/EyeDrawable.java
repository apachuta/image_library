package com.clever_cat.draw;

import com.clever_cat.gl.Color;
import com.clever_cat.gl.EclipseDrawer;
import com.clever_cat.gl.PolygonDrawer;


public class EyeDrawable extends AbstractDrawable {

	private static final int SEGMENTS = 50;
	
	private EclipseDrawer eclipseDrawer;
	
	public EyeDrawable() {
		this(new PolygonDrawer());
	}
	
	public EyeDrawable(PolygonDrawer polygonDrawer) {
		eclipseDrawer = new EclipseDrawer(polygonDrawer, SEGMENTS);
	}
	
	@Override
	public void draw() {
		float xCenter = left + width/2;
		float yCenter = bottom + height/2;
		
		// draw eyeball
		eclipseDrawer.drawEclipse(mvpMatrix, xCenter, yCenter, width/2, height/2, Color.WHITE);
		// draw pupil
		eclipseDrawer.drawEclipse(mvpMatrix, xCenter, yCenter, width/4, height/4, Color.BLACK);
	}
}
