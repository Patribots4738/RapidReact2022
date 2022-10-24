package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import java.security.spec.DSAGenParameterSpec;

import com.revrobotics.CANSparkMax.SoftLimitDirection;

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

	Limelight limelight;

	Shooter shooter;

	ShooterController shooterController;

	Elevator elevator;

	Dashboard autoDashboard;

	Dashboard.boolBox aligned;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;

	AutoDrive auto;

	private int autoIndex = 3;

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

		autoChooser = autoDashboard.new boxChooser("Auto path", "top", "mid", "hanger", "3-Ball", "4-Ball");

		aligned = autoDashboard.new boolBox("Aligned");

		topBangBang = new DynamicBangBang(0.016, 0.0003, 0.005);
		bottomBangBang = new DynamicBangBang(0.016, 0.0003, 0.005);

		topLinearBang = new LinearBangBang(0.0003, 0.005);
		bottomLinearBang = new LinearBangBang(0.0003, 0.005);

	}

	LinearBangBang bottomLinearBang;
	LinearBangBang topLinearBang;

	@Override
	public void robotPeriodic() {

		compressor.setState(true);

	}

	private boolean firstTime;

	@Override
	public void autonomousInit() {

		firstTime = true;

		leftMotors.setPID(0.05, 0, 5);
		rightMotors.setPID(0.05, 0, 5);

		turret.setZero(0.0);

		auto.reset();

		intake.putDownIntake();	

		autoIndex = autoChooser.getValue();

		shooting = false;

		switch(autoIndex){

			case 0: // topTarmac

				auto.addCommands(new Command(CommandType.MOVE, -43, 0.25));

				break;	

			case 1: // midTarmac

				auto.addCommands(new Command(CommandType.MOVE, -77, 0.25));  

				break;

			case 2: // hangerTarmac

				auto.addCommands(new Command(CommandType.MOVE, -77, 0.25));  

				break;

			case 3: // threeBall

				auto.addCommands(new Command(CommandType.MOVE, -43, 0.25));
			
				break;
					
			case 4: // fourBall
				
				auto.addCommands(new Command(CommandType.MOVE, -77, 0.25));

				break;
		}
		
	}
	
	boolean autoFirstTimeWait = true;
	Countdown autoFirstWaitCountdown;

	public void topTarmacAuto() {
		
		if (autoFirstTimeWait) {
		
			autoFirstWaitCountdown = new Countdown(3);
			autoFirstTimeWait = false;
			shooting = false;
			
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {
			
			if (auto.queueIsEmpty()) {
			
				shooterController.autoFire(0);
				shooting = true;
	
			} else { //Queue not empty and timer is not running
			
				auto.executeQueue();
				shooting = false;
	
			}

		}

	}

	public void midTarmacAuto() {
		
		if (autoFirstTimeWait) {
		
			autoFirstWaitCountdown = new Countdown(3);
			autoFirstTimeWait = false;
			shooting = false;
		
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {
			
			if (auto.queueIsEmpty()) {
			
				shooterController.autoFire(1);
				shooting = true;
	
			} else { //Queue not empty and timer is not running
			
				auto.executeQueue();
				shooting = false;
	
			}

		}

	}

	public void hangerTarmacAuto() {
		
		if (autoFirstTimeWait) {
		
			autoFirstWaitCountdown = new Countdown(3);
			autoFirstTimeWait = false;
			shooting = false;
		
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {
			
			if (auto.queueIsEmpty()) {
			
				shooterController.autoFire(2);
				shooting = true;
	
			} else { //Queue not empty and timer is not running
			
				auto.executeQueue();
				shooting = false;
	
			}

		}

	}

	boolean shooting = false;

	boolean firstThreeBallTime = false;
	boolean firstThreeBallTimeTwo = true;

	boolean anotherFirst = true;
	Countdown anotherCountdown;

	boolean shotFirst = false;

	public void threeBallAuto() {

		if (anotherFirst) {

			anotherFirst = false;
			anotherCountdown = new Countdown(3.5);

		}

		if (anotherCountdown.isRunning()) {

			return;

		}
		
		if (auto.queueIsEmpty()){			
			
			if(firstTime) {

				countdown = new Countdown(4.0);
				firstTime = false;
				shooting = true;

			}

			if (!countdown.isRunning()) {
				auto.executeQueue();
				shooting = false;
				firstTime = true;
				shotFirst = true;

				if (firstThreeBallTime) {

					firstThreeBallTime = false;
	
					if (firstThreeBallTimeTwo) {
	
						firstThreeBallTimeTwo = false;
						auto.addCommands(new Command(CommandType.MOVE, 43, 0.5));
						auto.addCommands(new Command(CommandType.ROTATE, -0.305, 0.25));
						auto.addCommands(new Command(CommandType.MOVE, -97.96, 0.25));
	
					}
	
				}

			} else {

				if (!shotFirst) {

					shooterController.autoFire(3);

				} else {

					shooterController.autoSecondFire();

				}
				shooting = true;
				firstThreeBallTime = true;

			}
			
		} else {

			auto.executeQueue();
			shooting = false;
			firstTime = true;

		}
	}

	public void fourBallAuto() {

		//CHANGE COUNTDOWN FOR INTAKE TIME FOR 5 BALL
		if (autoFirstTimeWait) {

			autoFirstWaitCountdown = new Countdown(7);
			autoFirstTimeWait = false;
			shooting = false;
			
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {

			if (auto.queueIsEmpty()) {

				shooterController.autoFire(3);
				shooting = true;
	
			} else { //Queue not empty and timer is not running
				//Progresses the queue
				auto.executeQueue();
				shooting = false;
	
			}

		}

	}

	@Override
	public void autonomousPeriodic() {

		if (!shooting) {

			shooterController.stopAim();
			trigger.setSpeed(-0.3);
			intake.setIntakeSpeed(-1.0);

		}

		switch(autoIndex){
			case 0:
				topTarmacAuto();
				break;
			case 1:
				midTarmacAuto();
				break;
			case 2:
				hangerTarmacAuto();
				break;
			case 3:
				threeBallAuto();
				break;
			case 4: 
				fourBallAuto();
				break;	
		}
		
	}
	
	// NO TOUCH
	// IF ANYTHING IS IN EITHER OF THESE LINES, 
	// THE ROBOT WILL NOT WORK IN COMP
	@Override 
	public void disabledInit() {}

	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {}

	
	double topSpeed;
	double bottomSpeed;

	@Override
	public void teleopInit() {

		turret.setZero(0.0);
		turret.setIsGoingRight(true);

		driver.setupButtons();
		operator.setupButtons();

		topSpeed = 0.0;
		bottomSpeed = 0.0;

		lastSpeedSet = 0.0;

		intake.init();

	}

	boolean intakeTesting = false;

	/**
	 * @param intakemode normal intake: 1, test intake:-1
	 */
	public void intake(int intakemode) {

		if (firstIntaking) {

			firstIntaking = false;
			firstIntakingStartTime = Timer.getTime();

		}

		intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger));

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			trigger.setSpeed(-0.1);
			firstIntakingStartTime = Timer.getTime();

		} else {

			trigger.setSpeed(0.0);

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);
	
			}

		}

	}

	@Override
	public void teleopPeriodic() {

		shooterController.update();

		aligned.setValue(ShooterController.aligned && shooter.readyToFire);

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
	/*	if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-0.3);

		}*/
	
		if (operator.getButton(XboxController.Buttons.A)) {

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);
	
			} else {

				trigger.setSpeed(0.0);

			}

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

				//trigger.setSpeed(0.0);
				//intake.setIntakeSpeed(0.0);

			}

		} else {

			shooterController.stopAim();

		}

		// Unsure as to why this is static but the robot runs :-)
		if (shooterController.aligned && operator.getButton(XboxController.Buttons.B)) {

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);

			} else {
				
				//shooterController.fire();

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

	@Override
	public void testInit() {}
	
	boolean firstIntaking = true;
	double firstIntakingStartTime;

	@Override
	public void testPeriodic() {}

}