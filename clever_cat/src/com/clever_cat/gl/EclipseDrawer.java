package com.clever_cat.gl;


public class EclipseDrawer {

	private PolygonDrawer polygonDrawer;
	private int segments;
	private Point[] unitaryVertices;
	
	public EclipseDrawer(PolygonDrawer polygonDrawer, int segments) {
		this.polygonDrawer = polygonDrawer;
		this.segments = segments;
		computeUnitaryVertices(segments);
	}

	private void computeUnitaryVertices(int segments) {
		unitaryVertices = new Point[segments];
		double alpha = 0, delta = Math.PI * 2 / segments;
		for (int i = 0; i < segments; ++i) {
			unitaryVertices[i] = new Point((float) Math.sin(alpha), (float) Math.cos(alpha));
			alpha += delta;
		}
	}

	public void drawEclipse(float[] mvpMatrix, float x, float y, float xRadius, float yRadius,
			Color color) {
		Point[] vertices = new Point[segments];
		for (int i = 0; i < segments; ++i) {
			vertices[i] = new Point(
					unitaryVertices[i].getX() * xRadius + x,
					unitaryVertices[i].getY() * yRadius + y);
		}
		polygonDrawer.drawPolygon(mvpMatrix, vertices, color);
	}
}
