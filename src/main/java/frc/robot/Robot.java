package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import java.security.spec.DSAGenParameterSpec;

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

	Dashboard shooterDashboard;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;


	@Override
	public void robotInit() {

		topMotor = new Falcon(7);
		bottomMotor = new Falcon(8);

		//topMotor.setPID(0.5, 0, 0);
		//bottomMotor.setPID(0.5, 0, 0);
		topMotor.setFF(1.0);
		bottomMotor.setFF(1.0);

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
		turret = new Turret(turretMotor, 0.75);

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

		/* P = new Dashboard.slider("P", 0, 10, 0.01); 
		I = new Dashboard.slider("I", 0, 30, 0.01);
		D = new Dashboard.slider("D", 0, 4, 0.01); */
		//P.setValue(0.5);

		shooterDashboard = new Dashboard("shooter");

		//graph = generalDashboard.new graph("turret speed", 10);


		topSlider = shooterDashboard.new slider("top Speed", -1, 1, 0.01);
		bottomSlider = shooterDashboard.new slider("bottom Speed", -1, 1, 0.01);

		topGraph = shooterDashboard.new graph("Top Speed", 10);
		bottomGraph = shooterDashboard.new graph("Bottom Speed", 10);

		topBangBang = new DynamicBangBang(topMotor, 0.01, 0.0075);
		bottomBangBang = new DynamicBangBang(bottomMotor, 0.01, 0.0075);

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

		topBangBang.init();
		bottomBangBang.init();

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

		//drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX));

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

		// topMotor.setPID(P.getValue(), I.getValue(), D.getValue());
		// topMotor.setFF(1.5);

		//turret.scan();

		topSpeed = topSlider.getValue();
		bottomSpeed = bottomSlider.getValue(); 

		//System.out.println(String.format("Desired top speed: %.2f; Desired bottom speed: %.2f", topSpeed, bottomSpeed));

		double topBangSpeed = topBangBang.getCommand(topSpeed);
		double bottomBangSpeed = bottomBangBang.getCommand(bottomSpeed);

		topGraph.addData(topSpeed, topMotor.getSpeed(), topBangSpeed);
		bottomGraph.addData(bottomSpeed, bottomMotor.getSpeed(), bottomBangSpeed);

		// bang bang control
		//topMotor.setSpeed(topBangSpeed); 
		//bottomMotor.setSpeed(bottomBangSpeed);

		//graph.addData(turret.getSpeed());

		//System.out.println("topmotor " + String.format("%.2f", topMotor.getSpeed()) + ", bottomotor " + String.format("%.2f", bottomMotor.getSpeed()));

		// multipled by 0.2 so max speed is 0.2 so no break
		//turret.rotate(operator.getAxis(XboxController.Axes.RightX) * 0.2);

		//turret.scan();

		System.out.println("distance: " + limelight.getDistance());

		if (operator.getButton(XboxController.Buttons.A)) {

			shooterController.aim();

		}

		if (shooterController.aligned && operator.getButton(XboxController.Buttons.B)) {

			shooterController.fire();

		}

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {



	}

}