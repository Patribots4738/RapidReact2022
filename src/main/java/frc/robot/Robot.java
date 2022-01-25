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

	DriverCamera camera;
	SparkMax max;

	@Override
	public void robotInit() {

		camera = new DriverCamera(0);

		compressor = new Compressor();

		max = new SparkMax(2, true);

		rightMotors = new PIDMotorGroup(new Falcon(0), new Falcon(1));
		leftMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

		drive = new Drive(leftMotors, rightMotors);

		driver = new XboxController(0);
		operator = new XboxController(1);

		//intake = new Intake(new SparkMax(2, true), new DoubleSolenoid(0, 1));

		// change 100 to correct max encoder position
		//turret = new Turret(new SparkMax(5, true), 100, 0.2);

	}

	@Override
	public void robotPeriodic() {

		compressor.setState(true);

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

		drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

		/*if (driver.getAxis(XboxController.Axes.RightTrigger) > 0.25) {

			intake.runIntake(-.05);

		} else {

			intake.stopIntake();

		}*/

		//intake.setIntakeSpeed(-driver.getAxis(XboxController.Axes.RightTrigger));
		//intake.putUpIntake();

		//max.setPosition(30, -0.2, 0.2);
		System.out.println(max.getPosition());

		// multipled by 0.15 so max speed is 0.15 so no break
		//turret.rotate(operator.getAxis(XboxController.Axes.RightTrigger) * 0.15);

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}