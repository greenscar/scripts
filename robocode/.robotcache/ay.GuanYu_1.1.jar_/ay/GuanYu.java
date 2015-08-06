package ay;
import robocode.*;
import java.lang.*;
import java.awt.Color;

/**
 * GuanYu - the Chinese God of War.  All fear him and his Blue Dragon Crescent Blade.
 * 
 * Moves in a circle, firing hard when an enemy is detected
 */
public class GuanYu extends AdvancedRobot
{
	/**
	 * run: GuanYu's default behavior
	 */
	public void run() {
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:
		setColors(Color.green,Color.green,Color.green);
		
		goToBottomMiddle();		

		double turnRate = 180;
		double gunTurnRate = 360;
			while(true) {
				
	//		setAhead((getBattleFieldHeight()/(180/turnRate))*1);
		
			setAhead(10000);
			boolean movingForward = true;

			setTurnLeft(turnRate);
			setTurnGunLeft(gunTurnRate);
			
			waitFor(new TurnCompleteCondition(this));
		}
	}

	public void goToBottomMiddle()
	{
		
		turnRight(180-getHeading());
			ahead(getY()-100);
	
		if(getWidth() > getBattleFieldWidth() / 2)
		{
			turnRight(90);

		} else {
			turnLeft(90);
		}
		ahead((getBattleFieldWidth() / 2) - getX());
		
		if (getWidth() > getBattleFieldWidth() / 2) {
			turnRight(180);
		}
		
		
	}
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		fire(1000);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {

	}
	
}
