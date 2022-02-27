package hardware;

import utils.Calc;
import wrappers.*;

public class Drive {

	MotorGroup leftMotors;
	MotorGroup rightMotors;

	private final double deadBand = 0.07;

	public Drive(MotorGroup leftMotors, MotorGroup rightMotors) {

		this.leftMotors = leftMotors;
		this.rightMotors = rightMotors;

		leftMotors.setSpeed(0);
		rightMotors.setSpeed(0);

	}

	private double deadBand(double value) {

		if(Calc.isBetween(value, -deadBand, deadBand)) {

			return 0;

		}

		return value;

	}

	public void linearArcade(double throttle, double turning) {

		throttle = deadBand(throttle);
		turning = deadBand(turning);

		double leftMotorInput = (throttle + turning);
		double rightMotorInput = (throttle - turning);

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(-rightMotorInput);

	}

	public void parabolicArcade(double throttle, double turning) {

		throttle = deadBand(throttle);
		turning = deadBand(turning);

		double leftMotorInput = (throttle + turning) * Math.abs((throttle + turning));
		double rightMotorInput = (throttle - turning) * Math.abs((throttle - turning));

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(-rightMotorInput);

	}

	public void bananaArcade(double throttle, double turning) {

		throttle = deadBand(throttle);
		turning = deadBand(turning);

		double adjustedThrottle = Calc.bananaCurve(throttle);
		double adjustedTurning = Calc.bananaCurve(turning);

		double leftMotorInput = adjustedThrottle + adjustedTurning;
		double rightMotorInput = -(adjustedThrottle - adjustedTurning);

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

	public void linearTank(double leftStick, double rightStick) {

		leftStick = deadBand(leftStick);
		rightStick = deadBand(rightStick);

		double leftMotorInput = leftStick;
		double rightMotorInput = -rightStick;

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

	public void parabolicTank(double leftStick, double rightStick) {

		leftStick = deadBand(leftStick);
		rightStick = deadBand(rightStick);

		double leftMotorInput = leftStick * Math.abs(leftStick);
		double rightMotorInput = -(rightStick * Math.abs(rightStick));

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

	public void bananaTank(double leftStick, double rightStick) {

		leftStick = deadBand(leftStick);
		rightStick = deadBand(rightStick);

		double leftMotorInput = Calc.bananaCurve(leftStick);
		double rightMotorInput = -Calc.bananaCurve(rightStick);

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

	public void smartTank(double leftStick, double rightStick) {

		leftStick = deadBand(leftStick);
		rightStick = deadBand(rightStick);

		double leftMotorInput = leftStick;
		double rightMotorInput = -rightStick;

		if(Math.abs((Math.abs(leftStick) - Math.abs(rightStick))) < 0.07) {

			double avg = ((Math.abs(leftStick) + Math.abs(rightStick)) / 2) * (Math.abs(leftStick) / leftStick);

			leftMotorInput = avg;
			rightMotorInput = -avg;

		}

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

	public void curvature(double throttle, double turning) {

		throttle = deadBand(throttle);
		turning = deadBand(turning);

		throttle = Calc.bananaCurve(throttle);
		turning = Calc.bananaCurve(turning);

		if(throttle == 0) {

			bananaArcade(throttle, turning);
			
		} else {

			double speedDifference = (-Math.atan(turning * Math.PI) * throttle) * (Math.abs(throttle)/throttle);

			double leftMotorInput = throttle - speedDifference;
			double rightMotorInput = throttle + speedDifference;

			leftMotorInput = Calc.bananaCurve(leftMotorInput);
			rightMotorInput = -Calc.bananaCurve(rightMotorInput);

			leftMotors.setSpeed(leftMotorInput);
			rightMotors.setSpeed(rightMotorInput);

		}

	}

	public void trainingWheels(double throttle, double turning) {

		throttle = deadBand(throttle);
		turning = deadBand(turning);

		double adjustedThrottle = Calc.bananaCurve(throttle);

		double leftMotorInput = adjustedThrottle + turning;
		double rightMotorInput = -(adjustedThrottle - turning);

		leftMotors.setSpeed(leftMotorInput);
		rightMotors.setSpeed(rightMotorInput);

	}

}