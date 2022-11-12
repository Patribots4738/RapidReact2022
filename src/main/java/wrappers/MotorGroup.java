package wrappers;

import interfaces.*;

public class MotorGroup implements Motor {

	Motor[] motors;

	public MotorGroup(Motor... motors) {

		this.motors = motors;

	}

	public void setSpeed(double speed) {

		for(Motor motor : motors){

			motor.setSpeed(speed);
			
		}

	}

	/**
	 * @param isBrake if true, it sets it to break mode, if false, to coast mode
	 */
	public void setBrakeMode(boolean isBrake) {

		for (Motor motor : motors) {

			motor.setBrakeMode(isBrake);

		}
	
	}


}