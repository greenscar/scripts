package nsm;
import robocode.*;
import java.util.*;

public class Sisyphus extends Robot {

	boolean peek;		// Don't turn if there's a robot there
	double moveAmount;	// How much to move
	double positionToBe;
	boolean evasion;
	boolean isHit;
	boolean normal;

	public void run() {
		moveAmount = Math.max(getBattleFieldWidth(),getBattleFieldHeight());

		peek = false;
		//setScanColor(Color.WHITE);
				peek = false;
		normal = true;
		evasion = false;
		isHit = false;
		
		// turnLeft to face a wall.
		// getHeading() % 90 means the remainder of 
		// getHeading() divided by 90.
		turnLeft(getHeading() % 90);
		ahead(moveAmount);
		// Turn the gun to turn right 90 degrees.
		peek = true;
		turnGunLeft(90);
		turnLeft(90);
		
		while (true)
		{
			
			
			if (!normal){
				turnLeft(getHeading() % 90);
				normal = true;
			}
			
			// Look before we turn when ahead() completes.
			peek = true;
			// Move up the wall
			ahead(moveAmount);
			// Don't look now
			peek = false;
			// Turn to the next wall
			turnLeft(90);
			resetGun();
		}
	}
	
	//resets the gun
	public void resetGun(){
		
		/*if(getHeading()>=270 && getHeading()<360){
			setGunToPosition(0);
		}
		else if(getHeading()>=180 && getHeading()<270){
			setGunToPosition(270);	
		}
		else if(getHeading()>=90 && getHeading()<180){
			setGunToPosition(180);	
		}
		else{
				setGunToPosition(90);
		}*/
		if(getHeading()>=270 && getHeading()<360){
			setGunToPosition(180);
		}
		else if(getHeading()>=180 && getHeading()<270){
			setGunToPosition(90);	
		}
		else if(getHeading()>=90 && getHeading()<180){
			setGunToPosition(0);	
		}
		else{
				setGunToPosition(270);
		}
		
	}
	
	public void setGunToPosition(double pos){
		double toLeft=getGunHeading()-pos;
		double toRight=pos-getGunHeading();
		if(toLeft<=180){
			turnGunRight(toRight);
		}
		else{
			turnGunLeft(toLeft);	
		}
	}
	
	/**
	 * onHitRobot:  Move away a bit.
	 */	
	public void onHitRobot(HitRobotEvent e) {
		// If he's in front of us, set back up a bit.
		if (e.getBearing() > -90 && e.getBearing() < 90)
			back(100);
		// else he's in back of us, so set ahead a bit.
		else
			ahead(100);
		normal=false;
	}

	/**
	 * onScannedRobot:  Fire!
	 */	
	public void onScannedRobot(ScannedRobotEvent e) {
		if(getEnergy()>10 && peek && getOthers()==1){
			fire(.5);
		turnGunLeft(2.72);
		}
		else if(getEnergy()>10 && peek){
			//if(getEnergy()>=60)
				fire(3);
			/*else if(getEnergy()>=30)
				fire(2);
			else if(getEnergy()>=10)
				fire(1);*/	
		}
	}
	
	public void onHitByBullet(HitByBulletEvent e){
			/*back(1000);
			turnRight(90);
			back(1000);*/
		if(!evasion && getOthers()==1){
			evasion = true;
			turnRight(45);
			back(100);
			turnLeft(45);
			ahead(200);
			turnRight(45);
			ahead(100);
			turnLeft(45);
			normal = false;
			evasion = false;
		}
	}
	
	
}												