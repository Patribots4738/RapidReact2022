package utils;

public class Calc {

	// returns the y coordinate of @param x on the banana curve (graph on desmos: https://www.desmos.com/calculator/xxgjmyatz5)
	// input and output should be within -1 and 1 (inclusive)
	public static double bananaCurve(double x) {

		return Math.signum(x) * Math.pow(Math.abs(x), 1 + (Math.abs(x))); //original
		///return Math.signum(x) * Math.pow(Math.abs(x), 1.15 + (Math.abs(x))); //v2.0
		//return (1.175 * Math.signum(x) * Math.pow(Math.abs(x), 1 + (Math.abs(x)))) - (x * 0.175); //v3.0

		// graph of all 3: https://www.desmos.com/calculator/r6amhjsyyo

	}

	// modified version of the banana curve for the training wheels drive method
	public static double trainingWheelsBananaCurve(double x) {

		return Math.signum(x) * Math.pow(Math.abs(x), 0.8 + (Math.abs(x)));

	}

	public static boolean isBetween(double value, double min, double max) {

		return (value < max) && (value > min);

	}

	public static double degreesToRadians(double degrees) {

		return (degrees / 180.0) * Math.PI;

	} 

	// converts rotations of the robot to rotations of the drive motors
	public static double robotRotationsToDrive(double robotRotations) {

		return inchesToDrive((robotRotations) * Constants.ROBOT_WHEEL_CIRCLE_CIRCUMFRENCE);

	}
	
	// converts linear distance to rotations of the drive motors
	public static double inchesToDrive(double inches) {

		return ((inches) / Constants.DRIVE_WHEEL_CIRCUMFRENCE) / Constants.DRIVE_GEAR_RATIO;
		
	}

	/**
	 * @return degrees from chord to tangent of arc of spline, AKA angle to set up robot
	 * from robot being straight to end point. ALSO angle from chord that
	 * robot will end up at at end of spline path
	 */
	public static double getAngleFromSplineDestination(double chordLength, double arcHeight) {

		double radius = (chordLength * chordLength) / (8 * arcHeight) + (arcHeight / 2);

		double arcLength = 2 * radius * Math.asin(chordLength / (2 * radius));

		return (0.5 * arcLength);

	}

	// caps a value between the two specified inputs and outputs
	public static double cap(double value, double min, double max) {

		if(value > max) {

			return max;

		}

		if(value < min) {

			return min;

		}

		return value;

	}

}