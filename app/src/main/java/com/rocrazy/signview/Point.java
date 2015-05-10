package com.rocrazy.signview;


/**
 * This class contain a point values on the view.
 * They are x, y values and the time (milisecond) when user move finger to the point.
 */

public class Point {
	public final float x;
	public final float y;
	public final long time;
	
	public Point(float x, float y, long time){
		this.x = x;
		this.y = y;
		this.time = time;
	}

	/**
	 * Caculate the distance between current point to the other.
	 * @param p the other point
	 * @return
	 */
	private float distanceTo(Point p){
		return (float) (Math.sqrt(Math.pow((x - p.x), 2) + Math.pow((y - p.y), 2)));
	}


	/**
	 * Caculate the velocity from the current point to the other.
	 * @param p the other point
	 * @return
	 */
	public float velocityFrom(Point p) {
		return distanceTo(p) / (this.time - p.time);
	}
}
