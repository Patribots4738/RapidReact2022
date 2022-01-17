package wrappers;

import interfaces.*;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;

public class Victor implements Motor {

	VictorSPX motor;

	public Victor(int canID){

		motor = new VictorSPX(canID);
		
	}

	public void setSpeed(double speed){

		motor.set(ControlMode.PercentOutput, speed);

	}

}