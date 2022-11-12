package hardware;

import interfaces.PIDMotor;
import wrappers.*;

public class Elevator {

	PIDMotor rightLifter;
	PIDMotor leftLifter;

    PIDMotorGroup lifter;

	SingleSolenoid lock;

	double defaultSpeed = 0.4;

    double upperLimit = 10;
    double lowerLimit = 0;

	public Elevator(PIDMotor leftLifter, PIDMotor rightLifter) {
		
		this.rightLifter = rightLifter;
		this.leftLifter = leftLifter;

		this.rightLifter.setBrakeMode(true);
		this.leftLifter.setBrakeMode(true);

		rightLifter.setSpeed(0.0);
		leftLifter.setSpeed(0.0);
		
	}

	public void setElevator(double speed) {

		if (Math.abs(speed) < 0.1) {

			speed = 0.0;

		}

		rightLifter.setPercent(speed);
		leftLifter.setPercent(-speed);

	}

	public void setElevatorUp() {

		rightLifter.setPercent(defaultSpeed);
		leftLifter.setPercent(-defaultSpeed);

	}

	public void setElevatorDown() {

		rightLifter.setPercent(-defaultSpeed);
		leftLifter.setPercent(defaultSpeed);

	}

	public void stop() {

		rightLifter.setPercent(0);
		leftLifter.setPercent(0);

	}

	public void setLock(boolean isLocked) {

		lock.set(isLocked);

	}

}