package hardware;

import interfaces.PIDMotor;
import wrappers.*;

public class Elevator {

	// PIDMotor rightLifter;
	// PIDMotor leftLifter;

    PIDMotorGroup lifter;

	SingleSolenoid lock;

	double defaultSpeed = 0.4;

    double upperLimit = 10;
    double lowerLimit = 0;

	public Elevator(PIDMotor leftLifter, PIDMotor rightLifter, SingleSolenoid lock) {
		
		// this.rightLifter = rightLifter;
		// this.leftLifter = leftLifter;
        this.lifter = new PIDMotorGroup(rightLifter,leftLifter);

		this.lock = lock;

		// rightLifter.setSpeed(0.0);
		// leftLifter.setSpeed(0.0);
        lifter.setPercent(0);
		
	}

	public void setElevator(double speed) {

		// rightLifter.setPercent(speed);
		// leftLifter.setPercent(-speed);
        if((lifter.getPosition() > upperLimit && speed > 0) || (lifter.getPosition() < lowerLimit && speed < 0)){

            lifter.setPercent(0);
        
        } else {
        
            lifter.setPercent(speed);
        
        }

	}

	public void setElevatorUp() {

		// rightLifter.setPercent(defaultSpeed);
		// leftLifter.setPercent(-defaultSpeed);
        setElevator(defaultSpeed);

	}

	public void setElevatorDown() {

		// rightLifter.setPercent(-defaultSpeed);
		// leftLifter.setPercent(defaultSpeed);
        setElevator(-defaultSpeed);

	}

	public void stop() {

		// rightLifter.setPercent(0);
		// leftLifter.setPercent(0);
        lifter.setPercent(0);

	}

	public void setLock(boolean isLocked) {

		lock.set(isLocked);

	}

}