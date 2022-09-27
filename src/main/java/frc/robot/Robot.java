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

	Elevator elevator;

	Dashboard shooterDashboard;
	Dashboard driveDashboard;
	Dashboard autoDashboard;

	Dashboard.graph topMotorGraph;
	Dashboard.graph bottomMotorGraph;
	// Dashboard.slider P;
	// Dashboard.slider I;
	// Dashboard.slider D;
	// Dashboard.slider F;

	Dashboard.slider distanceSlider;

	Dashboard.boolBox aligned;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;

	AutoDrive auto;

	private int autoIndex = 2;

	Countdown countdown;

	Dashboard.boxChooser autoChooser;

	// Dashboard.

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
		intakeMotor.setPID(0.5, 0, 0);
		intake = new Intake(intakeMotor, new DoubleSolenoid(0, 1));

		SparkMax turretMotor = new SparkMax(2, true);
		turretMotor.setPID(1.2, 0, 0);//0.5, 0, 0);
		turretMotor.getCanSparkMax().enableSoftLimit(SoftLimitDirection.kForward, true);
		turretMotor.getCanSparkMax().enableSoftLimit(SoftLimitDirection.kReverse, true);
		turretMotor.getCanSparkMax().setSoftLimit(SoftLimitDirection.kForward, 35);
		turretMotor.getCanSparkMax().setSoftLimit(SoftLimitDirection.kReverse, -35);
		turret = new Turret(turretMotor, 0.975);//1.0

		SparkMax triggerMotor = new SparkMax(6, true);
		triggerMotor.setPID(0.5, 0, 0);
		trigger = new Trigger(triggerMotor);

		imu = new IMU();

		limelight = new Limelight("limelight");

		shooter = new Shooter(topMotor, bottomMotor);

		shooterController = new ShooterController(shooter, limelight, drive, intake, trigger, turret);

		auto = new AutoDrive(leftMotors, rightMotors);
	
		elevator = new Elevator(new Falcon(9), new Falcon(10));

			//for shuffleboard
		//smartDashboard = Shuffleboard.getTab("SmartDashboard");
		//intakeTestEntry = smartDashboard.add("intakeTesting", intakeTesting).getEntry();

		// P = new Dashboard.slider("P", 0, 10, 0.01); 
		// I = new Dashboard.slider("I", 0, 30, 0.01);
		// D = new Dashboard.slider("D", 0, 4, 0.01); 
		// P.setValue(0.5);

		shooterDashboard = new Dashboard("shooter");
		//driveDashboard = new Dashboard("drive");
		autoDashboard = new Dashboard("auto");

		autoChooser = autoDashboard.new boxChooser("Auto path", "2-Ball", "3-Ball", "4-Ball");

		// distance = shooterDashboard.new slider("distance",0, 250, .1);

		//motorGraph = driveDashboard.new graph("motor", 10);

		aligned = autoDashboard.new boolBox("Aligned");

		// P = shooterDashboard.new slider("P", -3, 3, 0.01);
		// I = shooterDashboard.new slider("I", -3, 3, 0.01);
		// D = shooterDashboard.new slider("D", -30, 30, 0.01);
		// F = shooterDashboard.new slider("FF", -3, 3, 0.01);

		topSlider = shooterDashboard.new slider("top Motor", -1, 1, 0.01);
		bottomSlider = shooterDashboard.new slider("bottom Motor", -1, 1, 0.01);

		distanceSlider = shooterDashboard.new slider("distanceSlider", 0, 400, 0.01);


		//graph = generalDashboard.new graph("turret speed", 10);

		topGraph = shooterDashboard.new graph("Top Speed", 10);
		bottomGraph = shooterDashboard.new graph("Bottom Speed", 10);
		triggerGraph = shooterDashboard.new graph("Trigger Speed", 10);
		// bottomGraph = shooterDashboard.new graph("Bottom Speed", 10);$

		// topSlider = driveDashboard.new slider("top Speed", 0, 1, 0.01);
		// bottomSlider = driveDashboard.new slider("bottom Speed", 0, 1, 0.01);

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
	private boolean firstWaitTime;

	@Override
	public void autonomousInit() {

		firstTime = true;
		firstWaitTime = true;

		leftMotors.setPID(0.05, 0, 5);
		rightMotors.setPID(0.05, 0, 5);

		turret.setZero(0.0);

		auto.reset();

		intake.putDownIntake();	

		autoIndex = autoChooser.getValue();

		shooting = false;

		switch(autoIndex){

			case 0: //twoBall

				auto.addCommands(new Command(CommandType.MOVE, -41, 0.25));

				//auto.addCommands(new Command(CommandType.MOVE, 41, 0.25));

				//Shoot

				break;	

			case 1: //threeBall

				auto.addCommands(new Command(CommandType.MOVE, -43, 0.25));//-41, 0.25
			
				//auto.addCommands(new Command(CommandType.MOVE, 41, 0.25));
			
				//Shoot
			
				//auto.addCommands(new Command(CommandType.ROTATE, -0.305, 0.25));
			
				//auto.addCommands(new Command(CommandType.MOVE, -97.96, 0.25));
			
				//Shoot
			
				break;
					
			case 2: //fourBall
			/*
				auto.addCommands(new Command(CommandType.MOVE, -41, 0.25));
				
				auto.addCommands(new Command(CommandType.MOVE, 41, 0.25));
				
				//Shoot
				 
				auto.addCommands(new Command(CommandType.ROTATE, -0.305, 0.25)); 
				
				auto.addCommands(new Command(CommandType.MOVE, -97.96, 0.25));
				
				auto.addCommands(new Command(CommandType.ROTATE, -0.0657, 0.25));
				
				auto.addCommands(new Command(CommandType.MOVE, 160, 0.25));
				
				auto.addCommands(new Command(CommandType.MOVE, -160, 0.25));
				
				//Shoot
*/
				auto.addCommands(new Command(CommandType.MOVE, -77, 0.25));

				break;
		}
		
	}
	
	boolean autoFirstTimeWait = true;
	Countdown autoFirstWaitCountdown;

	public void twoBallAuto() {
		//System.out.println("Two Ball Confirmed----------------");
		if (autoFirstTimeWait) {
			//System.out.println("First Time Wait Confirmed");
			autoFirstWaitCountdown = new Countdown(3);
			autoFirstTimeWait = false;
			shooting = false;
			
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {
			//System.out.println("Countdown Finished");
			if (auto.queueIsEmpty()) {
				//System.out.println("Firing Now");
				shooterController.autoFire();
				shooting = true;
	
			} else { //Queue not empty and timer is not running
				//System.out.println("Progressing Queue");
				auto.executeQueue();
				shooting = false;
	
			}

		} /*else { //autoFirstWaitCountdown.isRunning()
			System.out.println(String.format("Countdown: %.2f", autoFirstWaitCountdown.timeRemaining()));

		}*/

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
			anotherCountdown = new Countdown(3.5);//4.3333

		}

		if (anotherCountdown.isRunning()) {

			return;

		}
		
		if (/*auto.getQueueLength() == 3 || */auto.queueIsEmpty()){			
			//System.out.println("in pause area");
			if(firstTime) {

				countdown = new Countdown(4.0);//3.5
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
	System.out.println("ADDING COMMANDS");
						auto.addCommands(new Command(CommandType.MOVE, 43, 0.5));//41, 0.25 ; 43, 0.5
			
						//Shoot
					
						auto.addCommands(new Command(CommandType.ROTATE, -0.305, 0.25));
					
						auto.addCommands(new Command(CommandType.MOVE, -97.96, 0.25));
	
					}
	
				}

			} else {

				//System.out.println("pause");

				if (!shotFirst) {

					shooterController.autoFire();

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
			//System.out.println("First Time Wait Confirmed");
			autoFirstWaitCountdown = new Countdown(7);
			autoFirstTimeWait = false;
			shooting = false;
			
		}
		
		if (!autoFirstWaitCountdown.isRunning()) {
			//System.out.println("Countdown Finished");
			if (auto.queueIsEmpty()) {
				//System.out.println("Firing Now");
				shooterController.autoFire();
				shooting = true;
	
			} else { //Queue not empty and timer is not running
				//System.out.println("Progressing Queue");
				auto.executeQueue();
				shooting = false;
	
			}

		} /*else { //autoFirstWaitCountdown.isRunning()
			System.out.println(String.format("Countdown: %.2f", autoFirstWaitCountdown.timeRemaining()));

		}*/

	}

	@Override
	public void autonomousPeriodic() {

		double distance = shooterController.correctLimelightDistanceError(limelight.getDistance());

		shooter.setShooterSpeeds(distance);

		if (!shooting) {

			//System.out.println("TRIGGER SETTING BACKWARDS");
			//shooterController.aim();
			shooterController.stopAim();
			trigger.setSpeed(-0.3);//-0.1
			intake.setIntakeSpeed(-1.0);//-.7

		}

		//System.out.println("TRIGGER WHEEL SPEED: " + trigger.getSpeed());
		//System.out.println("queue length: " + auto.getQueueLength());
		//auto.executeQueue();

		switch(autoIndex){
			case 0:
				twoBallAuto();
				break;
			case 1:
				threeBallAuto();
				break;
			case 2: 
				fourBallAuto();
				break;	
		}
		
	}
	// NO TOUCH
	@Override 
	public void disabledInit() {
		//System.out.println(String.format("distance: %.2f; distanceCorrected: %.2f", limelight.getDistance(), shooterController.correctLimelightDistanceError(limelight.getDistance())));
	}

	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {
		//System.out.println(String.format("distance: %.2f; distanceCorrected: %.2f", limelight.getDistance(), shooterController.correctLimelightDistanceError(limelight.getDistance())));
		//System.out.println("distanceCorrected: " + String.format("%.2f", shooterController.correctLimelightDistanceError(limelight.getDistance())));
		
	}

	double topSpeed;
	double bottomSpeed;

	Dashboard.slider topSlider;
	Dashboard.slider bottomSlider;

	Dashboard.graph topGraph;
	Dashboard.graph bottomGraph;
	Dashboard.graph triggerGraph;

	//Dashboard.slider distance;

	
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

		

		/*if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			intake.setIntakeSpeed(speedSet);
			trigger.setSpeed(-0.1);

		} else if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			intake.setIntakeSpeed(speedSet * intakemode);
			trigger.setSpeed(0.35 * intakemode);

		} else {

			intake.setIntakeSpeed(0.0);
			trigger.setSpeed(0.0);

		}*/


		if (firstIntaking) {

			firstIntaking = false;
			firstIntakingStartTime = Timer.getTime();

		}

		intake.setIntakeSpeed(-operator.getAxis(XboxController.Axes.RightTrigger));

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1) {

			trigger.setSpeed(-0.1);//-0.65
			firstIntakingStartTime = Timer.getTime();

		} else {

			trigger.setSpeed(0.0);
			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);//-0.3
	
			}

		}

/*
		if (Timer.getTime() - firstIntakingStartTime < 0.2) {

			trigger.setSpeed(-0.0);//-0.15

		} else {	

			trigger.setSpeed(-0.0);//-0.15

		}
*/

	}

	@Override
	public void teleopPeriodic() {

		shooterController.update();
		aligned.setValue(ShooterController.aligned);

		elevator.setElevator(operator.getAxis(XboxController.Axes.RightY) * 0.75);
		
		//rightMotors.setPID(P.getValue(), I.getValue(), D.getValue());
		//leftMotors.setPID(P.getValue(), I.getValue(), D.getValue());

		//motorGraph.addData(-rightMotors.getSpeed(), driver.getAxis(XboxController.Axes.LeftY));

		double speedSet = driver.getAxis(XboxController.Axes.LeftY);
/*
		slow down to 0 when changing direction

		if (Math.signum(speedSet) != Math.signum(lastSpeedSet) && ((lastSpeedSet > 0.07) || lastSpeedSet < -0.07)) {
			
			speedSet = 0;
		
		}
*/

		//slew rate limiting
		if (Math.abs(speedSet) - Math.abs(lastSpeedSet) < -value) {

			if (speedSet + lastSpeedSet < 0.0) {

				speedSet = lastSpeedSet + value * (1 - Math.abs(driver.getAxis(XboxController.Axes.RightX)));
				

			} else if (speedSet + lastSpeedSet > 0.0) {

				speedSet = lastSpeedSet - value * (1 - Math.abs(driver.getAxis(XboxController.Axes.RightX)));

			}

		} 
		
		drive.curvature(speedSet, -driver.getAxis(XboxController.Axes.RightX) * 1);
		//drive.curvature(driver.getAxis(XboxController.Axes.LeftY), -driver.getAxis(XboxController.Axes.RightX) * 1);

		lastSpeedSet = speedSet;

		if (operator.getToggle(XboxController.Buttons.X)) {
			
			intake.putUpIntake();

		} else {

			intake.putDownIntake();

		}
		/*
		shooter.topWheel.setFF(1.1);
		shooter.bottomWheel.setFF(1);

		if (Math.abs(shooter.topWheel.getSpeed() - topSlider.getValue()) < 0.025) {

			shooter.topWheel.setSpeed(topLinearBang.getCommand(topSlider.getValue(), shooter.topWheel.getSpeed()));

		} else {

			topLinearBang.setNewSpeed(topSlider.getValue() * 1.03);

			shooter.topWheel.setSpeed(topSlider.getValue());

		}

		if (Math.abs(shooter.bottomWheel.getSpeed() - bottomSlider.getValue()) < 0.025) {

			shooter.bottomWheel.setSpeed(bottomLinearBang.getCommand(bottomSlider.getValue(), shooter.bottomWheel.getSpeed()));

		} else {

			bottomLinearBang.setNewSpeed(bottomSlider.getValue() * 1.03);

			shooter.bottomWheel.setSpeed(bottomSlider.getValue());

		}*/

/*
		if (Math.abs(topSlider.getValue()) - Math.abs(topSlider.getValue()) > 0.0) {

			shooter.topWheel.setFF(2);

		} else {

			shooter.topWheel.setFF(1.11);

		}

		if (Math.abs(shooter.topWheel.getSpeed() - topSlider.getValue()) < 0.025) {

			shooter.topWheel.setPercent(bottomLinearBang.getCommand(topSlider.getValue(), shooter.topWheel.getSpeed()));

		} else if (Math.abs(shooter.topWheel.getSpeed() - topSlider.getValue()) < 0.08) {

			bottomLinearBang.setNewSpeed(topSlider.getValue() * 1.125);

			shooter.topWheel.setFF(1.11);
			shooter.topWheel.setSpeed(topSlider.getValue());

		} else if (Math.abs(topSlider.getValue()) - Math.abs(topSlider.getValue()) > 0.0) {

			shooter.topWheel.setSpeed(topSlider.getValue() /* 2.003*);

		} else {

			shooter.topWheel.setSpeed(topSlider.getValue());

		}*/

		//shooter.topWheel.setSpeed(0.2);
		//shooter.bottomWheel.setSpeed(0.65);

		//shooterController.setShooterSpeeds(9 * 12);

		//topGraph.addData(shooter.topWheel.getSpeed(), topSlider.getValue());
		//bottomGraph.addData(shooter.bottomWheel.getSpeed(), bottomSlider.getValue());
		topGraph.addData(shooter.topWheel.getSpeed(),shooter.topAverage.getAverage(), topSlider.getValue());
		bottomGraph.addData(shooter.bottomWheel.getSpeed(),shooter.bottomAverage.getAverage(), bottomSlider.getValue());
		triggerGraph.addData(shooterController.trigger.motor.getSpeed());

		// shooterController.setShooterSpeeds(distance.getValue());
		// topGraph.addData(shooter.distanceToSpeeds(distance.getValue())[0], shooter.topWheel.getSpeed());
		// bottomGraph.addData(shooter.distanceToSpeeds(distance.getValue())[1], shooter.bottomWheel.getSpeed());
		// topMotor.setPID(P.getValue(), I.getValue(), D.getValue());
		// bottomMotor.setPID(P.getValue(), I.getValue(), D.getValue());
		// topMotor.setFF(F.getValue());
		// bottomMotor.setFF(F.getValue());

		//turret.scan();

		// topSpeed = topSlider.getValue();
		// bottomSpeed = bottomSlider.getValue();

		//System.out.println(String.format("Desired top speed: %.2f; Desired bottom speed: %.2f", topSpeed, bottomSpeed));

		//double topBangSpeed = topBangBang.getCommand(topSpeed, topMotor.getSpeed());
		// double bottomBangSpeed = bottomBangBang.getCommand(bottomSpeed, bottomMotor.getSpeed());

		// topGraph.addData(-topSpeed, topMotor.getSpeed());//, topBangSpeed);
		// bottomGraph.addData(bottomSpeed, bottomMotor.getSpeed());//, bottomBangSpeed);

		

		// bang bang control
		//topMotor.setPercent(topBangSpeed); 
		//bottomMotor.setPercent(bottomBangSpeed);
/*
		if (Math.abs(bottomMotor.getSpeed() - bottomSlider.getValue()) < 0.025) {

			bottomMotor.setPercent(bottomLinearBang.getCommand(bottomSlider.getValue(), bottomMotor.getSpeed()));

		} else if (Math.abs(bottomMotor.getSpeed() - bottomSlider.getValue()) < 0.08) {

			bottomLinearBang.setNewSpeed(bottomSlider.getValue() * 1.2);

			bottomMotor.setSpeed(bottomSlider.getValue());

		} else if (Math.abs(bottomSlider.getValue()) - Math.abs(bottomMotor.getSpeed()) > 0.0) {

			bottomMotor.setSpeed(bottomSlider.getValue() * 2.003);

		} else {

			bottomMotor.setSpeed(bottomSlider.getValue());

		}*/
		

		//topMotor.setSpeed(0.2);
		//bottomMotor.setSpeed(0.6);

		//graph.addData(turret.getSpeed());

		//System.out.println("topmotor " + String.format("%.2f", topMotor.getSpeed()) + ", bottomotor " + String.format("%.2f", bottomMotor.getSpeed()));

		// multipled by 0.2 so max speed is 0.2 so no break
		//turret.rotate(operator.getAxis(XboxController.Axes.RightX) * 0.2);

		//turret.scan();

		//System.out.println("distance: " + String.format("%.2f", limelight.getDistance()));
		//System.out.println("distanceCorrected: " + String.format("%.2f", shooterController.correctLimelightDistanceError(limelight.getDistance())));

//FOLLOWIN CODE IS VERY GOOD

		if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-0.3);//-0.3

		}
	
		if (operator.getButton(XboxController.Buttons.A)) {

			//shooterController.aim();

			if (!operator.getButton(XboxController.Buttons.B)) {

				//double distance = shooterController.correctLimelightDistanceError(limelight.getDistance());
double distance = distanceSlider.getValue();
				shooterController.setShooterSpeeds(distance);//-18
				
				System.out.println(distance);

			}

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				//trigger.setSpeed(0.35);
				//intake.setIntakeSpeed(-0.3);//-0.3

			} else {

				//shooterController.aim();
//CHANGE
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

			}

		} else {

			shooterController.stopAim();

		}

		if (shooterController.aligned && operator.getButton(XboxController.Buttons.B)) {

			if (operator.getAxis(XboxController.Axes.LeftTrigger) > 0.1) {

				trigger.setSpeed(0.35);
				intake.setIntakeSpeed(-0.3);//-0.1

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

			//shooterController.stop();

		}

		if (!operator.getButton(XboxController.Buttons.B)) {

			shooterController.firstShootingTime = true;

		}

	}

	@Override
	public void testInit() {

	}
	
	boolean firstIntaking = true;
	double firstIntakingStartTime;

	@Override
	public void testPeriodic() {
		
	}

}