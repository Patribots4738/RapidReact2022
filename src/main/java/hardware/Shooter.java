package hardware;

import wrappers.*;
import interfaces.*;
import utils.*;

public class Shooter {

	PIDMotor topWheel;
	PIDMotor bottomWheel;

	public static boolean readyToFire = false;

	// in decimal percent
	private double acceptableSpeedError = 0.0075;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;

	// each index in this array is another foot of distance from the target, starting at 5ft away, ending at 25ft away
	// these will be used to determine the speeds the shooter wheels need to be at when the robot is firing
	// order is topSpeed, bottomSpeed
	private double[][] shooterSpeeds = { {0.01, 0.56}, {0.01, 0.56}, {0.05, 0.57}, {0.08, 0.57}, {0.09, 0.58}, //5-9ft
										 {0.1, 0.59}, {0.11, 0.6}, {0.13, 0.62}, {0.14, 0.62}, {0.14, 0.63}, //10-14ft
										 {0.14, 0.64}, {0.5, 0.65}, {0.6, 0.69}, {0.17, 0.7}, {0.17, 0.71}, //15-19ft
										 {0.18, 0.72}, {0.17, 0.76}, {0.2, 0.79}, {0.2, 0.79}, {0.2, 0.79}, // 20-24ft
										 {0.2, 0.79} }; // 25ft

	public Shooter(PIDMotor topWheel, PIDMotor bottomWheel) {

		this.topWheel = topWheel;
		this.bottomWheel = bottomWheel;

		topBangBang = new DynamicBangBang(0.01, 0.002, 0.0075);
		bottomBangBang = new DynamicBangBang(0.01, 0.002, 0.0075);

	}

	private double[] distanceToSpeeds(double distance) {

		double feet = distance / 12.0;

		// exception case for when the robot is at maximum range
		if  (feet >= 25.0) {

			return shooterSpeeds[20];

		}

		// exception case for when the robot is at minimum range
		if (feet <= 5.0) {

			return shooterSpeeds[0];

		}

		double arrayPosition = feet - 5.0;

		int lowerIndex = (int)arrayPosition;
		int upperIndex = lowerIndex + 1;

		double percentBetweenPoints = arrayPosition - (double)lowerIndex;

		double topSpeed = shooterSpeeds[lowerIndex][0] + (shooterSpeeds[upperIndex][0] - shooterSpeeds[lowerIndex][0]) * percentBetweenPoints;
		double bottomSpeed = shooterSpeeds[lowerIndex][1] + (shooterSpeeds[upperIndex][1] - shooterSpeeds[lowerIndex][1]) * percentBetweenPoints;

		return new double[] {topSpeed, bottomSpeed};

	}

	// sets the shooter speeds based on distance
	public void setShooterSpeeds(double distance) {

		double[] speeds = distanceToSpeeds(distance);

		System.out.println("speeds: " + speeds[0] + " " + speeds[1]);

		topWheel.setSpeed(topBangBang.getCommand(speeds[0], topWheel.getSpeed()));
		bottomWheel.setSpeed(bottomBangBang.getCommand(speeds[1], bottomWheel.getSpeed()));

		eval(distance);
		
	}

	public void eval(double distance) {

		double[] speeds = distanceToSpeeds(distance);

		boolean topReady = Calc.isBetween(topWheel.getSpeed(), speeds[0] - acceptableSpeedError, speeds[0] + acceptableSpeedError);
		boolean bottomReady = Calc.isBetween(Math.abs(bottomWheel.getSpeed()), speeds[1] - acceptableSpeedError, speeds[1] + acceptableSpeedError);

		readyToFire = topReady && bottomReady;
/*
		System.out.println("top: " + topReady);
		System.out.println("bottom: " + bottomReady);
*/
	}

	public void setRawSpeeds(double topSpeed, double bottomSpeed) {

		topWheel.setSpeed(topSpeed);
		bottomWheel.setSpeed(-bottomSpeed);

	}

	public void rawEval(double topSpeed, double bottomSpeed) {

		double[] speeds = {topSpeed, bottomSpeed};

		boolean topReady = Calc.isBetween(topWheel.getSpeed(), speeds[0] - acceptableSpeedError, speeds[0] + acceptableSpeedError);
		boolean bottomReady = Calc.isBetween(Math.abs(bottomWheel.getSpeed()), speeds[1] - acceptableSpeedError, speeds[1] + acceptableSpeedError);

		readyToFire = topReady && bottomReady;

	}

	public void stop() {
		
		topWheel.setPercent(0);
		bottomWheel.setPercent(0);

		readyToFire = false;

	}

}