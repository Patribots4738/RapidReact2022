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

	//Intake intake;

	XboxController driver;
	XboxController operator;

	Turret turret;

	Compressor compressor;

	SparkMax max;

	@Override
	public void robotInit() {

		compressor = new Compressor();

		max = new SparkMax(2, true);
		max.setPID(0.5, 0, 0);

		//rightMotors = new PIDMotorGroup(new Falcon(0), new Falcon(1));
		//leftMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

		//drive = new Drive(leftMotors, rightMotors);

		driver = new XboxController(0);
		operator = new XboxController(1);

		//intake = new Intake(new SparkMax(2, true), new DoubleSolenoid(0, 1));

		turret = new Turret(max, 0.75, 0.25);

	}

	@Override
	public void robotPeriodic() {

		compressor.setState(true);
		System.out.println(max.getPosition());

	}

	@Override
	public void autonomousInit() {} 

	@Override
	public void autonomousPeriodic() {}

	// NO TOUCH
	@Override 
	public void disabledInit() {}
	
	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {}
	
	@Override
	public void teleopInit() {}

	@Override
	public void teleopPeriodic() {

		//drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

		/*if (driver.getAxis(XboxController.Axes.RightTrigger) > 0.25) {

			intake.runIntake(-.05);

		} else {

			intake.stopIntake();

		}*/

		//intake.setIntakeSpeed(-driver.getAxis(XboxController.Axes.RightTrigger));
		//intake.putUpIntake();

		//max.setPosition(69.9, -0.7, 0.7);

		// multipled by 0.15 so max speed is 0.15 so no break
		turret.rotate(operator.getAxis(XboxController.Axes.RightY) * 0.2);

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}