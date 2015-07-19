package com.clever_cat.gl;

public class Color {

	private float[] values;
	
	public Color(float r, float g, float b, float a) {
		values = new float[]{r, g, b, a};
	}
	
	public float[] getValues() {
		return values;
	}
	
	public static Color RED = new Color(255, 0, 0, 0);
	public static Color BLUE = new Color(0, 255, 0, 0);
	public static Color GREEN = new Color(0, 0, 255, 0);
	public static Color WHITE = new Color(255, 255, 255, 0);
	public static Color BLACK = new Color(0, 0, 0, 0); 
}
