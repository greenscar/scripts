package jrj;
import robocode.*;
import java.awt.Color;

/**
 * Wolverine - a robot by Justin Joseph that sucks!
 */
public class Wolverine extends Robot
{	
	boolean moveForward;
	/**
	 * run: Wolverine's default behavior
	 */
	public void run()
	{
		setColors(Color.blue, Color.white, Color.red);
		
		while (true)
		{	
			ahead(200);
			turnRight(180);
			moveForward = true;
			turnLeft(90);
			turnGunLeft(360);
			
			back(200);
			turnRight(90);
			turnGunRight(360);
		}
	}
	
	 public void onHitWall()
	{
		reverse();
	}
	
	public void reverse()
	{
		if(moveForward)
		{
			moveForward = false;
			back(200);
			turnLeft(90);
			turnGunRight(360);
		}
		else
		{
			ahead(200);
			turnRight(90);
			turnGunLeft(360);
		}
	}
	
	
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) 
	{	
		if (e.getDistance() <= 10 || e.getEnergy() >= 75)
		{
			stop();
			fire(3);
		}
		
		else if (e.getDistance() <= 30 || e.getEnergy() >= 25)
		{
			stop();
			fire(2);
		}
		
		else
		{
			stop();
			fire(1);
		}
		
			scan();
	}						
	
	public void onHitRobot(HitRobotEvent e)
	{	
		if (e.getBearing() > -90 && e.getBearing() < 90)
		{
			back(200);
		}
		else
		{
			ahead(200);
		}
	}
	
	public void onBulletHit()
	{
		fire(1);
	}
	
	public void onBulletMissed()
	{
		turnRight(90);
		ahead(50);
	}
	

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) 
	{
		if (e.getHeading() <= 50 || e.getBearing() <= 50) 
		{
			turnRight(90);
			turnGunRight(360);
			ahead(50);
			fire(1);
		}
		
		if (e.getHeading() <= 100 || e.getBearing() <= 100) 
		{
			turnLeft(90);
			turnGunLeft(360);
			ahead(50);
			fire(1);
		}
		
		if (e.getHeading() <= 150 || e.getBearing() <= 150) 
		{
			turnRight(90);
			turnGunRight(360);
			ahead(50);
			fire(1);
		}
		
		else
		{
			fire(1);
		}
	}
}
		