package mt;
import robocode.*;
import java.awt.Color;

/**
 * TigerBot - a robot by Musa Tesfamichael
 */
public class TigerBot extends Robot
{
	
	double last=0;
	
	/**
	 * run: TigerBot's default behavior
	 */
	public void run() {
		setColors(Color.orange,Color.black,Color.white);
			
			while(true) {
				turnGunRight(360);
			}
	}
	
	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		
		if(e.getDistance() < 10){
			fire(3);
		}else if(e.getDistance() < 30){
			fire(2);
		}else{
			fire(1);
		}

		moveAlong();
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnGunRight(e.getBearing());
		turnRight(e.getBearing());
		fire(1);
		moveAlong();
	}
	
	public void onHitWall(HitWallEvent event){
		moveAlong();
	}
	
	public void onHitRobot(HitRobotEvent event){
		turnGunRight(event.getBearing());
		turnRight(event.getBearing());
		fire(1);
		moveAlong();
	}
	
	public void onBulletHit(BulletHitEvent event){
		fire(1);
	}
	
	public void onBulletMissed(BulletMissedEvent event){
		moveAlong();
	}
	
	public void moveAlong(){
		turnRight(45);
		ahead(100);
	}
	
	public boolean isStill(HitByBulletEvent e){
		if(e.getBearing() == last){
			return true;
		}
		return false;
	}
			
}