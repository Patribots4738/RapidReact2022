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

<<<<<<< Updated upstream
	AutoPath autoPath;

	@Override
	public void robotInit() {}
=======
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

	Elevator elevator;

	Dashboard shooterDashboard;
	Dashboard autoDashboard;

	Dashboard.boolBox aligned;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;

	AutoDrive auto;

	private int autoIndex = 2;

	Countdown countdown;

	Dashboard.boxChooser autoChooser;

	double lastSpeedSet = 0;
	final double value = 0.025;

	@Override
	public void robotInit() {

		Timer.init();

		topMotor = new Falcon(7);
		bottomMotor = new Falcon(8);

		topMotor.setPID(1, 0, 0);
		bottomMotor.setPID(1, 0, 0);

		topMotor.setBrakeMode(false);
		bottomMotor.setBrakeMode(false);

		compressor = new Compressor();

		rightMotors = new PIDMotorGroup(new Falcon(0), new Falcon(1));
		leftMotors = new PIDMotorGroup(new Falcon(3), new Falcon(4));

		rightMotors.setBrakeMode(true);
		leftMotors.setBrakeMode(true);

		drive = new Drive(leftMotors, rightMotors);

		driver = new XboxController(0);
		operator = new XboxController(1);

		SparkMax intakeMotor = new SparkMax(5, true);
		SparkMax turretMotor = new SparkMax(2, true);
		
		intakeMotor.setPID(0.5, 0, 0);
		intake = new Intake(intakeMotor, new DoubleSolenoid(0, 1));
		
		turretMotor.setPID(1.2, 0, 0);
		turretMotor.getCanSparkMax().enableSoftLimit(SoftLimitDirection.kForward, true);
		turretMotor.getCanSparkMax().enableSoftLimit(SoftLimitDirection.kReverse, true);
		turretMotor.getCanSparkMax().setSoftLimit(SoftLimitDirection.kForward, 35);
		turretMotor.getCanSparkMax().setSoftLimit(SoftLimitDirection.kReverse, -35);
		
		turret = new Turret(turretMotor, 0.975);

		SparkMax triggerMotor = new SparkMax(6, true);
		
		triggerMotor.setPID(0.5, 0, 0);
		trigger = new Trigger(triggerMotor);

		imu = new IMU();

		limelight = new Limelight("limelight");

		shooter = new Shooter(topMotor, bottomMotor);

		shooterController = new ShooterController(shooter, limelight, drive, intake, trigger, turret);

		auto = new AutoDrive(leftMotors, rightMotors);
	
		elevator = new Elevator(new Falcon(9), new Falcon(10));

		autoDashboard = new Dashboard("auto");

		autoChooser = autoDashboard.new boxChooser("Auto path", "2-Ball", "3-Ball", "4-Ball");

		aligned = autoDashboard.new boolBox("Aligned");

		topBangBang = new DynamicBangBang(0.016, 0.0003, 0.005);
		bottomBangBang = new DynamicBangBang(0.016, 0.0003, 0.005);

		topLinearBang = new LinearBangBang(0.0003, 0.005);
		bottomLinearBang = new LinearBangBang(0.0003, 0.005);

	}

	LinearBangBang bottomLinearBang;
	LinearBangBang topLinearBang;
>>>>>>> Stashed changes

	@Override
	public void robotPeriodic() {}

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
<<<<<<< Updated upstream
=======

	
	double topSpeed;
	double bottomSpeed;
>>>>>>> Stashed changes
	
	@Override
	public void teleopInit() {}

	@Override
<<<<<<< Updated upstream
	public void teleopPeriodic() {}
=======
	public void teleopPeriodic() {

		shooterController.update();
		aligned.setValue(ShooterController.aligned);

		elevator.setElevator(operator.getAxis(XboxController.Axes.RightY) * 0.75);
		
		double speedSet = driver.getAxis(XboxController.Axes.LeftY);
		
		//slow down to 0 when changing direction

		//slew rate limiting
		if (Math.abs(speedSet) - Math.abs(lastSpeedSet) < -value) {

			if (speedSet + lastSpeedSet < 0.0) {

				speedSet = lastSpeedSet + value * (1 - Math.abs(driver.getAxis(XboxController.Axes.RightX)));
				

			} else if (speedSet + lastSpeedSet > 0.0) {

				speedSet = lastSpeedSet - value * (1 - Math.abs(driver.getAxis(XboxController.Axes.RightX)));

			}

		} 
		
		drive.curvature(speedSet, -driver.getAxis(XboxController.Axes.RightX) * 1);

		lastSpeedSet = speedSet;

		if (operator.getToggle(XboxController.Buttons.X)) {
			
			intake.putUpIntake();

		} else {

			intake.putDownIntake();

		}

		// Operator controls: 
		if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-0.3);

		}
	
		if (operator.getButton(XboxController.Buttons.A)) {

			if (!operator.getButton(XboxController.Buttons.B)) {

				double distance = shooterController.correctLimelightDistanceError(limelight.getDistance());
				// for manual input of turret speed testing:
				// double distance = distanceSlider.getValue();
				shooterController.setShooterSpeeds(distance);

			}
			
			if (operator.getButton(XboxController.Buttons.R)) {

				if (Math.abs(operator.getAxis(XboxController.Axes.LeftX)) > 0.1) {

					turret.rotate(operator.getAxis(XboxController.Axes.LeftX) * 0.4);

				} else {

					turret.rotate(0.0);

				}

			} else {

				shooterController.aim();

			}

			if (!operator.getButton(XboxController.Buttons.B)) {

				trigger.setSpeed(0.0);
				intake.setIntakeSpeed(0.0);

			}

		} else {

			shooterController.stopAim();

		}

		if (shooterController.aligned && operator.getButton(XboxController.Buttons.B)) {

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);

			} else {
				
				shooterController.fire();

			}

		} else {

			if (!operator.getButton(XboxController.Buttons.B) && !operator.getButton(XboxController.Buttons.A)) {

				if (intakeTesting) {

					intake(-1);
		
				} else {
		
					intake(1);
		
				}

			}

		}

		if (!operator.getButton(XboxController.Buttons.B)) {

			shooterController.firstShootingTime = true;

		}

	}
>>>>>>> Stashed changes

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}