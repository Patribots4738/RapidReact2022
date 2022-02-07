package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;


// for shuffleboard
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

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

	PIDMotor topMotor;
	PIDMotor bottomMotor;

	ColorSensor colorSensor;
	
	ShuffleboardTab smartDashboard;
	NetworkTableEntry intakeTestEntry;

	@Override
	public void robotInit() {

		topMotor = new Falcon(7);
		bottomMotor = new Falcon(8);

		topMotor.setPID(0.5, 0, 0);
		bottomMotor.setPID(0.5, 0, 0);

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

		// R seems like best value to determine color of first intaken ball
		colorSensor = new ColorSensor();

			//for shuffleboard
		//smartDashboard = Shuffleboard.getTab("SmartDashboard");
		//intakeTestEntry = smartDashboard.add("intakeTesting", intakeTesting).getEntry();

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

	boolean intakeTesting = true;

	/**
	 * @param intakemode normal intake: 1, test intake:-1
	 */
	public void intake(int intakemode) {

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger));
			trigger.setSpeed(-0.1);

		} else if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger) * intakemode);
			trigger.setSpeed(0.35 * intakemode);

		} else {

			intake.setIntakeSpeed(0.0);
			trigger.setSpeed(0.0);

		}

	}
	/*
	public void intakeNormal() {

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger));
			trigger.setSpeed(-0.1);

		} else if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.LeftTrigger));
			trigger.setSpeed(0.35);

		} else {

			intake.setIntakeSpeed(0.0);
			trigger.setSpeed(0.0);

		}

	}

	public void intakeTest() {

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger));
			trigger.setSpeed(-0.1);

		} else if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			intake.setIntakeSpeed(operator.getAxis(XboxController.Axes.LeftTrigger));
			trigger.setSpeed(-0.35);
	
		} else {

			intake.setIntakeSpeed(0.0);
			trigger.setSpeed(0.0);

		}
		

	}*/

	@Override
	public void teleopPeriodic() {

		drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

		//for shuffleboard
		//intakeTesting = intakeTestEntry.getBoolean(intakeTesting);

		if (intakeTesting) {

			intake(-1);

		} else {

			intake(1);

		}

		/*
		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger) * 0.75);
			trigger.setSpeed(-0.1);
			//trigger.setSpeed(0.35);

		} else {

			intake.setIntakeSpeed(operator.getAxis(XboxController.Axes.LeftTrigger));

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(-0.35);

			} else {

				trigger.setSpeed(0.0);

			}

		}
		*/
		if (operator.getToggle(XboxController.Buttons.X)) {
			
			intake.putUpIntake();

		} else {

			intake.putDownIntake();

		}

		//topMotor.setSpeed(operator.getAxis(XboxController.Axes.LeftY) * 0.8);
		//bottomMotor.setSpeed(operator.getAxis(XboxController.Axes.LeftY) * 0.8);

		//System.out.println("topmotor " + String.format("%.2f", topMotor.getSpeed()) + ", bottomotor " + String.format("%.2f", bottomMotor.getSpeed()));

		// multipled by 0.15 so max speed is 0.15 so no break
		//turret.rotate(operator.getAxis(XboxController.Axes.RightY) * 0.2);

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}