package wrappers;

import interfaces.*;

public class PIDMotorGroup extends MotorGroup implements PIDMotor {

	PIDMotor[] pidMotors;

	public PIDMotorGroup(PIDMotor... motors) {

		super(motors);
		this.pidMotors = motors;

	}

	public PIDMotor[] getpidMotors() {

		return this.pidMotors;

	}

	public void setP(double p) {

		for (PIDMotor motor : pidMotors) {

			motor.setP(p);

		}

	}

	public void setI(double i) {

		for (PIDMotor motor : pidMotors) {

			motor.setI(i);

		}

	}

	public void setD(double d) {

		for (PIDMotor motor : pidMotors) {

			motor.setD(d);

		}

	}    

	public void setFF(double ff) {

		for (PIDMotor motor : pidMotors) {

			motor.setFF(ff);

		}

	}

	public void setPID(double p, double i, double d) {

		for (PIDMotor motor : pidMotors) {

			motor.setPID(p, i, d);

		}

	}

	public void setPercent(double percent) {

		for(PIDMotor motor : pidMotors) {

			motor.setPercent(percent);

		}

	}

	public void setPosition(double rotations, double minSpeed, double maxSpeed) {

		for (PIDMotor motor : pidMotors) {

			motor.setPosition(rotations, minSpeed, maxSpeed);

		}
		
	}

	public double getPosition() {

		double positionSum = 0.0;

		for (PIDMotor motor : pidMotors) {

			positionSum += motor.getPosition();

		}

		return positionSum / motors.length;

	}

	public double getSpeed() {

		double speedSum = 0.0;

		for (PIDMotor motor : pidMotors) {

			speedSum += motor.getSpeed();

		}

		return speedSum / motors.length;

	}

	public void resetEncoder() {

		for (PIDMotor motor : pidMotors) {

			motor.resetEncoder();

		}

	}

	public void setAccelerationPercent(double speed, double increment) {


		for (PIDMotor motor: pidMotors) {

			motor.setAccelerationPercent(speed, increment);

		}

	}

	public void setLastSpeed() {

		for (PIDMotor motor: pidMotors) {

			motor.setLastSpeed();

		}

	}

	/**
	 * function to slowly stop the robot over time, 
	 * it should be above zero and below 1 (not including 0), with the deceleration getting stronger as you approach 1
	 * your net motor % speed change per second (this shouldn't ever be very high) = decelerationRate * arbConstant * 50
	 * seconds to fully stop = initialSpeed / decelerationRate * arbConstant * 50
	 * this is currently set up for a max motor % speed change per second of 40%
	 * @param decelerationRate controls the strength of the deceleration
	 */
	public void safeStop(double decelerationRate) {

		// safety to prevent people from trying to insta-stop the robot with crazy values
		if(decelerationRate > 1) {

			decelerationRate = 1.0;

		}

		double currentSpeed = getSpeed();

		// arbitrary constant for converting between 0-1 decelerationRate and actual change in motor speed per loop
		double arbConstant = 0.008;

		// you should never be cutting the motor's power by more than a tiny amount in 1 loop (20ms), that's how you get motors to explode
		// for example, there are 50 loops in a second, so even doing 5% per loop will drop it from 100% to 0 in 400ms, which is bad, very, very bad
		double netSpeedDiff = arbConstant * decelerationRate;

		// as this is run continuously, the motor speed will slowly ramp down
		double nextPlannedSpeed = (currentSpeed - (netSpeedDiff * Math.signum(currentSpeed)));

		// once the motor is under 5% power, it's fine to just stop it completely
		if(Math.abs(currentSpeed) < 0.05) {

			nextPlannedSpeed = 0;

		}

		setSpeed(nextPlannedSpeed);

	}

	// NOT FUNCTIONAL
	public double getAmperage() {

		double ampsSum = 0.0;

		for (PIDMotor motor : pidMotors) {

			ampsSum += motor.getAmperage();

		}

		return ampsSum / motors.length;

	}

}