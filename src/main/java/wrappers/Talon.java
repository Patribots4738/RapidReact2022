package wrappers;

import interfaces.*;
import utils.Constants;
import utils.PIDLoop;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

public class Talon implements PIDMotor{

	private TalonSRX motor;

	private PIDLoop PIDLoop;

	double lastSpeed;

	public Talon(int canID){

		motor = new TalonSRX(canID);

		//this is a black box, dont touch, get Zach (unless I'm gone, then google, I'm sorry for the horrors that await you)
		motor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 100);
		motor.configAllowableClosedloopError(0, 0, 20);
		motor.config_kF(0, 0, 20);
		motor.configNominalOutputForward(0, 20);
		motor.configNominalOutputReverse(0, 20);
		motor.configPeakOutputForward(1, 20);
		motor.configPeakOutputReverse(-1, 20);
		motor.setSensorPhase(true); // you might need to change this, but hopefully not

		PIDLoop = new PIDLoop(0, 0, 0);

		lastSpeed = 0.0;

	}

	public void setSensorPhase(boolean phase) {

		motor.setSensorPhase(phase);

	}

	public void setP(double P) {

		motor.config_kP(0, P, 20);
		PIDLoop.setP(P);

	}

	public void setI(double I) {

		motor.config_kI(0, I, 20);
		PIDLoop.setI(I);

	}

	public void setD(double D) {

		motor.config_kD(0, D, 20);
		PIDLoop.setD(D);
		
	}

	public void setFF(double ff) {

		motor.config_kF(0, ff, 0);
		PIDLoop.setFF(ff);
		
	}

	public void setPID(double P, double I, double D) {

		setP(P);
		setI(I);
		setD(D);

	}

	public void setSpeed(double speed){

		motor.configPeakOutputForward(1.0);
		motor.configPeakOutputReverse(-1.0);
	
		double currentCommand = PIDLoop.getCommand(speed, getSpeed());

		this.setPercent(currentCommand);
		
	}

	public void setPercent(double percent) {

		motor.set(ControlMode.PercentOutput, percent);

	}

	public void setPosition(double rotations, double minSpeed, double maxSpeed) {

		motor.configPeakOutputReverse(minSpeed, 20);
		motor.configPeakOutputForward(maxSpeed, 20);

		motor.set(ControlMode.Position, (int)(rotations * Constants.TALON_CLICKS));

	}

	/**
	 * accelerates robot so it doesnt tip over, or just for smoothness
	 * @param speed desired speed
	 * @param increment added percent speed per 0.02 second loop (set VERY low, like 0.01 or something)
	 */
	public void setAccelerationPercent(double speed, double increment) {

		double newSpeed = lastSpeed + increment;

		if (speed > 1.0) {

			speed = 1.0;

		} 

		if (speed < -1.0) {

			speed = -1.0;

		}

		if (newSpeed > speed) {

			setPercent(speed);
		
		} else {

			setPercent(newSpeed);

		}

	}

	/**
	 * run at end of (insert_name_here)Periodic loop in the mode you are using in robot.java (auto, teleop, disabled (wtf are you doing), or test)
	 */
	public void setLastSpeed() {

		lastSpeed = getSpeed();

	}

	public double getPosition() {

		return motor.getSelectedSensorPosition() / Constants.TALON_CLICKS; 

	}

	public double getSpeed() {

		return motor.getSelectedSensorVelocity();

	}

	public void resetEncoder() {

		motor.setSelectedSensorPosition(0);

	}

	// NOT FUNCTIONAL
	public double getAmperage() {

		return 0.0;

	}

}