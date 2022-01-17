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

}