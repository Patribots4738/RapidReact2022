package hardware;

import wrappers.*;

import java.io.LineNumberReader;

import interfaces.*;
import utils.*;

public class Shooter {

	public PIDMotor topWheel;
	public PIDMotor bottomWheel;

	public static boolean readyToFire = false;

	// in decimal percent
	private double acceptableSpeedError = 0.0075;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;
	
	LinearBangBang topLinearBangBang;
	LinearBangBang bottomLinearBangBang;

	// each index in this array is another foot of distance from the target, starting at 5ft away, ending at 25ft away
	// these will be used to determine the speeds the shooter wheels need to be at when the robot is firing
	// order is topSpeed, bottomSpeed
	private double[][] shooterSpeeds = { {0.14, 0.63}, {0.12, 0.68}, {0.11, 0.70}, {0.09, 0.74}, {0.09, 0.76}, //5-9ft
										 {0.09, 0.78}, {0.08, 0.79}, {0.08, 0.81}, {0.08, 0.87}, {0.08, 0.89}, //10-14ft
										 {0.09, 0.91}, {0.11, 0.92}, {0.13, 0.93}, {0.15, 0.93}, {0.17, 0.94}, //15-19ft
										 {0.19, 0.94}, {0.20, 0.94}, {0.22, 0.94}, {0.23, 0.94}, {0.25, 0.94}, // 20-24ft
										 {0.31, 0.95} }; // 25ft

	public Shooter(PIDMotor topWheel, PIDMotor bottomWheel) {

		this.topWheel = topWheel;
		this.bottomWheel = bottomWheel;

		topBangBang = new DynamicBangBang(0.01, 0.002, 0.0075);
		bottomBangBang = new DynamicBangBang(0.01, 0.002, 0.0075);

		topLinearBangBang = new LinearBangBang(0.0003, 0.005);
		bottomLinearBangBang = new LinearBangBang(0.0003, 0.005);

	}

	// public PIDMotor[] getMotors(){
	// 	return {topWheel, bottomWheel};
	// }

	public double[] distanceToSpeeds(double distance) {

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

		//System.out.println(String.format("speeds:     %.4f; %.4f", speeds[0], speeds[1]));
		//System.out.println(String.format("realSpeeds: %.4f; %.4f" , topWheel.getSpeed(), bottomWheel.getSpeed()));

		topWheel.setFF(1.2);
		bottomWheel.setFF(1);

		if (Math.abs(topWheel.getSpeed() - speeds[0]) < 0.05) {

			topWheel.setPercent(topLinearBangBang.getCommand(speeds[0], topWheel.getSpeed()));

		} else {

			topLinearBangBang.setNewSpeed(speeds[0] * 1.1);

			topWheel.setSpeed(speeds[0]);

		}

		if (Math.abs(bottomWheel.getSpeed() - speeds[1]) < 0.025) {

			bottomWheel.setPercent(bottomLinearBangBang.getCommand(speeds[1], bottomWheel.getSpeed()));

		} else {

			bottomLinearBangBang.setNewSpeed(speeds[1] * 1.075);

			bottomWheel.setSpeed(speeds[1]);

		}
		
		eval(distance);

	}

	public void eval(double distance) {

		double[] speeds = distanceToSpeeds(distance);

		boolean topReady = Calc.isBetween(topWheel.getSpeed(), speeds[0] - acceptableSpeedError, speeds[0] + acceptableSpeedError);
		boolean bottomReady = Calc.isBetween(Math.abs(bottomWheel.getSpeed()), speeds[1] - acceptableSpeedError, speeds[1] + acceptableSpeedError);

		readyToFire = topReady && bottomReady;

		//System.out.println("top: " + topReady);
		//System.out.println("bottom: " + bottomReady);

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