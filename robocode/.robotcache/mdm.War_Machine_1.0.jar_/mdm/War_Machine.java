package mdm;
import robocode.*;
import java.awt.Color;

/**
 * MyWalls - a robot by michael.mahnke
 */
public class War_Machine extends Robot
{
	
	boolean peek;
	
	double moveAmount;
	
	boolean normal;
	
	boolean evasion;
	
	boolean isHit;
	
	int others;
	

	double maxX, maxY;

	
	/**
	 * run: MyWalls's default behavior
	 */
	public void run() {
		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:
		setColors(Color.black,Color.black,Color.black);

		maxX = getBattleFieldWidth();
		
		maxY = getBattleFieldHeight();
		
		moveAmount = Math.max(maxX, maxY);
		
		peek = false;
		normal = true;
		evasion = false;
		others = getOthers();
		
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		
		peek = true;
		
		turnGunLeft(90);
		turnLeft(90);
		
	
		while(true) {

			if (!normal){
				turnLeft(getHeading() % 90);
				normal = true;
			}
			
			peek = true;
			
			ahead(moveAmount);
			
			peek = false;
		
			turnLeft(90);
			
			others = getOthers();
			
		}
	}
	
	
	public void onHitRobot(HitRobotEvent e){
		
		if (e.getBearing() > -90 && e.getBearing() < 90)
			back(100);
		else
			ahead(100);
		normal = false;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		if(getEnergy() > 10) 
			fire(2.5);
		
		
		if(peek)
			scan();

	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		
		if(!evasion && others == 1){
			evasion = true;
			turnRight(45);
			back(100);
			turnLeft(45);
			ahead(200);
			turnRight(45);
			ahead(100);
			turnLeft(45);
			scan();
			normal = false;
			evasion = false;
		}
	}
	
	public void onWin(WinEvent win){

		turnGunRight(1080);
		turnLeft(1080);
	}
}
												