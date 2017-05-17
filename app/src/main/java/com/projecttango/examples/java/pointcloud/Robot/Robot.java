package com.projecttango.examples.java.pointcloud.Robot;

/**
 * Created by rafaelszuminski on 5/3/17.
 */

public class Robot {
    /*
    * Problem:
    * we have position data and a list of locations to accomplish
    * pathfinder deals with following the path
    * we need to translate this algorithm to motor data
    */

    private double[] position;
    private double[] orientation;
	private int speed, turning;


        /*
         * position is a 2D vector {x, y}
         * * in your actual code, position should be the tango pose data, which needs to be updated during
         * * the gotoLocation algorithm
         * orientation is a complex number (a + bi) ==> {a, b}
         * * in your actual code, orientation should be the tango pose data, which needs to be updated during
         * * the gotoLocation algorithm
         * you can simplify the quaternion w, x, y, z to the complex number a, b
         * 				I think.
         */

    public Robot() {
        position = new double[] {0, 0};
        orientation = new double[] {0, 0};
	speed = MainActivity.DEFAULT_PWM;
	turning = MainActivity.DEFAULT_STEERING;//I forget exactly what this is called
    }

        /*
         * this is the basic "go to this place" algorithm
         * from the pathfinder, get the next (most time-efficient) target location
         * pass that target location to gotoLocation(), and the robot should make it there.
         * after you've scanned, pass the next pathfinder target location, wrinse, and repeat.
         */

    public void gotoLocation(double[] target) {
	double angleTolerance = 5;
	    //target[0] = x
	    //target[1] = y
        double[] targetOrientation = deltaVector(target);
        targetOrientation[0] /= distance(target);
        targetOrientation[1] /= distance(target);
	    
	faceLocation(targetOrientation, angleTolerance);
        
        while(distance(target) > 0.5) {
		targetOrientation = deltaVector(target);
        	targetOrientation[0] /= distance(target);
        	targetOrientation[1] /= distance(target);
            	setSpeed(400);
	    	setSteering(Math.atan2(targetOrientation[1], targetOrientation[0]) 
				- Math.atan2(orientation[1], orientation[0]));
        }
    }
	
	public void faceLocation(double[] targetOrientation, double tolerance) {
		double tolerance1 = 90;
		double tolerance2 = 45;
		double angle = Math.atan2(targetOrientation[1], targetOrientation[0]) 
				- Math.atan2(orientation[1], orientation[0]);
		
		while(Math.abs(angle) > tolerance1) {
			setSteering(angle*4); //sign may need to be flipped
        	} 
		while(Math.abs(angle) > tolerance2) {
			setSteering(angle*3); //sign needs to be same as above
		}
		while(Math.abs(angle > tolerance) {
			setSteering(Math.signum(angle)*100);
		}
		setSteering(0);
        	
	}
	
	
	/*
	 * just in case you need some
	 * amphetamines to get through the day
	 */
	public int getSpeed() {
		return speed;	
	}
	public void setSpeed(int s) {
		speed = s;
		MainActivity.set_speed(MainActivity.DEFAULT_PWM + speed);
	}
	public int getSteering() {
		return steering;
	}
	public void setSteering(int s) {
		steering = s;
		MainActivity.set_speed(MainActivity.DEFAULT_PWM + steering);
	}
	
	
    /*
     * vector between position and target
     */
    public double[] deltaVector(double[] target) {
        return new double[] {target[0] - position[0], target[1] - position[1]};
    }
    /*
     * distance between position and target
     */
    public double distance(double[] target) {
        return Math.sqrt(Math.pow(deltaVector(target)[0], 2) +Math.pow(deltaVector(target)[1], 2));
    }
}
