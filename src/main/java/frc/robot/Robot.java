package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import autonomous.*;
import autonomous.Command.CommandType;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;
import wrappers.Dashboard.graph;
import wrappers.Dashboard.slider;

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

	Limelight limelight;

	Shooter shooter;

	ShooterController shooterController;

	Dashboard.graph graph; 

	slider P;

	slider I;

	slider D;

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

		limelight = new Limelight("limelight");

		shooter = new Shooter(topMotor, bottomMotor);

		shooterController = new ShooterController(shooter, limelight, drive, intake, trigger, turret);

		// R seems like best value to determine color of first intaken ball
		//colorSensor = new ColorSensor();

			//for shuffleboard
		//smartDashboard = Shuffleboard.getTab("SmartDashboard");
		//intakeTestEntry = smartDashboard.add("intakeTesting", intakeTesting).getEntry();

		P = new Dashboard.slider("P", 0, 10, 0.01); 
		I = new Dashboard.slider("I", 0, 30, 0.01);
		D = new Dashboard.slider("D", 0, 4, 0.01);
		P.setValue(0.5);

		graph = new Dashboard.graph("turret speed");

		topSlider = new Dashboard.slider("top Speed", 0, 1, 0.01);
		bottomSlider = new Dashboard.slider("bottom Speed", 0, 1, 0.01);

		topGraph = new Dashboard.graph("Top Speed");
		bottomGraph = new Dashboard.graph("Bottom Speed");

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

	double speed = 0.0;
	double topSpeed;
	double bottomSpeed;

	Dashboard.slider topSlider;
	Dashboard.slider bottomSlider;

	Dashboard.graph topGraph;
	Dashboard.graph bottomGraph;
	
	@Override
	public void teleopInit() {

		turret.setZero();

		driver.setupButtons();
		operator.setupButtons();

		speed = 0.0;
		topSpeed = 0.0;
		bottomSpeed = 0.0;

	}

	boolean intakeTesting = false;

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

	@Override
	public void teleopPeriodic() {

		drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

		if (intakeTesting) {

			intake(-1);

		} else {

			intake(1);

		}

		if (operator.getToggle(XboxController.Buttons.X)) {
			
			intake.putUpIntake();

		} else {

			intake.putDownIntake();

		}
/*
		if (operator.getButtonDown(XboxController.Buttons.Y)) {

			speed += 0.05;

		}

		if (operator.getButtonDown(XboxController.Buttons.A)) {

			speed -= 0.05;

		}
*/

		topMotor.setPID(P.getValue(), I.getValue(), D.getValue());
		topMotor.setFF(1.5);

		topSpeed = topSlider.getValue();
		bottomSpeed = bottomSlider.getValue(); 

		topGraph.addData(topSpeed, topMotor.getSpeed());
		bottomGraph.addData(bottomSpeed, bottomMotor.getSpeed());

		topMotor.setSpeed(topSpeed);
		bottomMotor.setSpeed(bottomSpeed);

		graph.addData(turret.getSpeed());

		System.out.println("topmotor " + String.format("%.2f", topMotor.getSpeed()) + ", bottomotor " + String.format("%.2f", bottomMotor.getSpeed()));

		// multipled by 0.2 so max speed is 0.2 so no break
		turret.rotate(operator.getAxis(XboxController.Axes.RightX) * 0.2);

		if (operator.getButton(XboxController.Buttons.A)) {

			shooterController.aim();

		}

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}