package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import java.util.ArrayList;

import autonomous.*;
import autonomous.Command.CommandType;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

	Drive drive;

	PIDMotorGroup rightMotors;
	PIDMotorGroup leftMotors;

	Intake intake;

	XboxController driver;
	XboxController operator;

	Turret turret;

	Trigger trigger;

	Compressor compressor;

	IMU imu;

	@Override
	public void robotInit() {

		compressor = new Compressor();

		rightMotors = new PIDMotorGroup(new Falcon(0), new Falcon(1));
		leftMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

		drive = new Drive(leftMotors, rightMotors);

		driver = new XboxController(0);
		operator = new XboxController(1);

		SparkMax intakeMotor = new SparkMax(2, true);
		intakeMotor.setPID(0.5, 0, 0);
		intake = new Intake(intakeMotor, new DoubleSolenoid(0, 1));

		SparkMax turretMotor = new SparkMax(5, true);
		turretMotor.setPID(0.5, 0, 0);
		turret = new Turret(turretMotor, 0.75, 0.25);

		SparkMax triggerMotor = new SparkMax(6, true);
		triggerMotor.setPID(0.5, 0, 0);
		trigger = new Trigger(triggerMotor);

		imu = new IMU();

	}

	@Override
	public void robotPeriodic() {

		compressor.setState(true);
		//System.out.println(max.getPosition());

	}

	@Override
	public void autonomousInit() {

		turret.setZero();

	} 

	@Override
	public void autonomousPeriodic() {}

	// NO TOUCH
	@Override 
	public void disabledInit() {}
	
	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {}
	
	@Override
	public void teleopInit() {

		turret.setZero();

		driver.setupButtons();
		operator.setupButtons();

	}

	@Override
	public void teleopPeriodic() {

		System.out.println("imu" + imu.getZRotation());

		drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger) * 0.75);
			trigger.setSpeed(-0.1);

		} else {

			intake.setIntakeSpeed(operator.getAxis(XboxController.Axes.LeftTrigger));

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(-0.35);

			} else {

				trigger.setSpeed(0.0);

			}

		}

		if (operator.getToggle(XboxController.Buttons.X)) {
			
			intake.putUpIntake();

		} else {

			intake.putDownIntake();

		}

		// multipled by 0.15 so max speed is 0.15 so no break
		//turret.rotate(operator.getAxis(XboxController.Axes.RightY) * 0.2);

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}