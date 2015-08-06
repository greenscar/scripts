/*
 * Created on Jul 6, 2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package cac;

import robocode.*;
import robocode.util.*;
import java.awt.geom.*;
/**
 * @author Student19
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class FirstAttempt extends AdvancedRobot 
{
	protected static int FORWARD;
	
	public void run() 
	{
		while (true) 
		{
			// Tell the game that when we take move,
			//  we'll also want to turn right... a lot.
			setTurnRight(100);
			// Limit our speed to 5
			setMaxVelocity(8);
			// Start moving (and turning)
			ahead(100);
			// Repeat.
		}
	}
	
	public void onScannedRobot( ScannedRobotEvent e ) 
	{
		double bulletPower = Math.min(3.0,getEnergy());
		double myX = getX();
		double myY = getY();
		double absoluteBearing = getHeadingRadians() + e.getBearingRadians();
		double enemyX = getX() + e.getDistance() * Math.sin(absoluteBearing);
		double enemyY = getY() + e.getDistance() * Math.cos(absoluteBearing);
		double enemyHeading = e.getHeadingRadians();
		double enemyVelocity = e.getVelocity();	
		
		double deltaTime = 0;
		double battleFieldHeight = getBattleFieldHeight();
		double battleFieldWidth = getBattleFieldWidth();
		double predictedX = enemyX;
		double predictedY = enemyY;
		
		while((++deltaTime) * (20.0 - 3.0 * bulletPower) < Point2D.Double.distance(myX, myY, predictedX, predictedY))
		{		
			predictedX += Math.sin(enemyHeading) * enemyVelocity;	
			predictedY += Math.cos(enemyHeading) * enemyVelocity;
			if(	predictedX < 17.0 
				|| predictedY < 17.0
				|| predictedX > battleFieldWidth - 17.0
				|| predictedY > battleFieldHeight - 17.0){
				predictedX = Math.min(Math.max(17.0, predictedX), battleFieldWidth - 17.0);	
				predictedY = Math.min(Math.max(17.0, predictedY), battleFieldHeight - 17.0);
				break;
			}
		}
		double theta = Utils.normalAbsoluteAngle(Math.atan2(predictedX - getX(), predictedY - getY()));
		
		setTurnRadarRightRadians(Utils.normalRelativeAngle(absoluteBearing - getRadarHeadingRadians()));
		setTurnGunRightRadians(Utils.normalRelativeAngle(theta - getGunHeadingRadians()));
		fire(bulletPower);
	}
	public void onHitRobot( HitRobotEvent e ) 
	{
	}
	public void onWin()
	{
		
	}
}
