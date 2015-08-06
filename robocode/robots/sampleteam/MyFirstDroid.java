package sampleteam;
import robocode.*;
//import java.awt.Color;

/**
 * SimpleDroid - a sample robot by Mathew Nelson
 * 
 * Follows orders of team leader
 */
public class MyFirstDroid extends TeamRobot implements Droid
{
	/**
	 * run: Droid's default behavior
	 */
	public void run() {
		System.out.println("MyFirstDroid ready.");
	}
	
	/**
	 * onMessageReceived: What to do when our leader sends a message
	 */
	public void onMessageReceived(MessageEvent e)
	{
		// Fire at a point
		if (e.getMessage() instanceof Point)
		{
			Point p = (Point)e.getMessage();
			// Calculate x and y to target
			double dx = p.getX() - this.getX();
			double dy = p.getY() - this.getY();
			// Calculate angle to target
			double theta = Math.toDegrees(Math.atan2(dx,dy));
			// Turn gun to target
			turnGunRight(normalRelativeAngle(theta - getGunHeading()));
			// Fire hard!
			fire(3);
		}
		// Set our colors
		else if (e.getMessage() instanceof RobotColors)
		{
			RobotColors c = (RobotColors)e.getMessage();
			setColors(c.getBodyColor(),c.getGunColor(),c.getRadarColor());
		}
	}
	
	/**
	 * normalRelativeAngle:  returns angle such that -180<angle<=180
	 */	
	public double normalRelativeAngle(double angle) {
		if (angle > -180 && angle <= 180)
			return angle;
		double fixedAngle = angle;
		while (fixedAngle <= -180)
			fixedAngle += 360;
		while (fixedAngle > 180)
			fixedAngle -= 360;
		return fixedAngle;
	}
}
