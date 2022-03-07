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
	Dashboard driveDashboard;
	Dashboard autoDashboard;

	Dashboard.graph topMotorGraph;
	Dashboard.graph bottomMotorGraph;
	// Dashboard.slider P;
	// Dashboard.slider I;
	// Dashboard.slider D;
	// Dashboard.slider F;

	DynamicBangBang topBangBang;
	DynamicBangBang bottomBangBang;

	AutoDrive auto;

	private int autoIndex = 2;

	Countdown countdown;

	Dashboard.boxChooser autoChooser;

	// Dashboard.

	double lastSpeedSet = 0;
	double lastIntakeSpeedSet;
	final double value = 0.025;
	final double intakeValue = 0.025;

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

		auto = new AutoDrive(leftMotors, rightMotors);
	
 

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
		//driveDashboard = new Dashboard("drive");
		autoDashboard = new Dashboard("auto");

		autoChooser = autoDashboard.new boxChooser("Auto path", "2-Ball", "3-Ball", "4-Ball");

		// distance = shooterDashboard.new slider("distance",0, 250, .1);

		//motorGraph = driveDashboard.new graph("motor", 10);

		// P = shooterDashboard.new slider("P", -3, 3, 0.01);
		// I = shooterDashboard.new slider("I", -3, 3, 0.01);
		// D = shooterDashboard.new slider("D", -30, 30, 0.01);
		// F = shooterDashboard.new slider("FF", -3, 3, 0.01);

		topSlider = shooterDashboard.new slider("top Motor", -1, 1, 0.01);
		bottomSlider = shooterDashboard.new slider("bottom Motor", -1, 1, 0.01);

		//Pid values "good pid but storing for now"
		//P = -0.32
		//I = 0 lmao
		//D = oh no lets say 0.12
		//F = what was f... probobly 1
		//Comment by Lemmon "Very similar to no pid, can't tell what I like better"


		//graph = generalDashboard.new graph("turret speed", 10);

		topGraph = shooterDashboard.new graph("Top Speed", 10);
		bottomGraph = shooterDashboard.new graph("Bottom Speed", 10);
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
/* 
	@Override
	public void autonomousInit() {

		leftMotors.setPID(0.05, 0, 5);
		rightMotors.setPID(0.05, 0, 5);

		turret.setZero();

		auto.reset();

		auto.addCommands(new Command(CommandType.MOVE, -120, 0.6));
		auto.addCommands(new Command(CommandType.MOVE, 36, 0.6));
		//auto.addCommands(new Command(CommandType.ROTATE, 0.25, 0.1));

	} 

	@Override
	public void autonomousPeriodic() {

		auto.executeQueue();

		if (auto.queueIsEmpty()) {

			intake.setIntakeSpeed(-1.0);
			trigger.setSpeed(0.35);

		} else {

			intake.putDownIntake();
			intake.setIntakeSpeed(-1.0);
			trigger.setSpeed(-0.1);

		}

		bottomMotor.setSpeed(0.6);

	}

 */
private boolean firstTime;
	private boolean firstWaitTime;

	@Override
	public void autonomousInit() {

		firstTime = true;
		firstWaitTime = true;

		leftMotors.setPID(0.05, 0, 5);
		rightMotors.setPID(0.05, 0, 5);

		turret.setZero();

		auto.reset();

		intake.putDownIntake();	

		autoIndex = autoChooser.getValue();

		switch(autoIndex){

			case 0: //twoBall

				auto.addCommands(new Command(CommandType.MOVE, -41, 0.1));

				auto.addCommands(new Command(CommandType.MOVE, 46, 0.1));

				//Shoot

				break;	

			case 1: //threeBall

				auto.addCommands(new Command(CommandType.MOVE, -41, 0.1));
			
				auto.addCommands(new Command(CommandType.MOVE, 46, 0.1));
			
				//Shoot
			
				auto.addCommands(new Command(CommandType.ROTATE, 1.825, 0.1)); //RADIANS
			
				auto.addCommands(new Command(CommandType.MOVE, -97.96, 0.1));
			
				//Shoot
			
				break;
					
			case 2: //fourBall
			
				auto.addCommands(new Command(CommandType.MOVE, 41, 0.1));
				
				auto.addCommands(new Command(CommandType.MOVE, -46, 0.1));
				
				//Shoot
				
				auto.addCommands(new Command(CommandType.ROTATE, 1.825, 0.1)); //RADIANS
				
				auto.addCommands(new Command(CommandType.MOVE, 97.96, 0.1));
				
				auto.addCommands(new Command(CommandType.ROTATE, 0.413, 0.1)); //RADIANS
				
				auto.addCommands(new Command(CommandType.MOVE, 160, 0.1));
				
				auto.addCommands(new Command(CommandType.MOVE, -160, 0.1));
				
				//Shoot

				break;
		}
		
	} 

	public void twoBallAuto() {

		if (auto.queueIsEmpty()) {

			if(firstTime) {
				countdown = new Countdown(1);
				firstTime = false;
			}	
			if(countdown.isRunning()){
				shooterController.fire();
			}

		} else {

			auto.executeQueue();

		}

	}

	public void threeBallAuto() {
		
		if (auto.getQueueLength() == 2 || auto.queueIsEmpty()){			

			if(firstTime) {
				countdown = new Countdown(1);
				firstTime = false;
			}

			if(!countdown.isRunning()){
				auto.executeQueue();
			}else{
				shooterController.fire();
			}
			
		}else {

			auto.executeQueue();
			firstTime = true;

		}
	}

	public void fourBallAuto() {
		
		if (auto.getQueueLength() == 5 || auto.queueIsEmpty()) {

			if(firstTime) {
				countdown = new Countdown(1);
				firstTime = false;
			}

			if(countdown.isRunning()){
				shooterController.fire();
			}

		}else if(auto.getQueueLength() == 1){

			if(firstWaitTime){
				countdown = new Countdown(1.5);
				firstWaitTime = false;
			}

			if(!countdown.isRunning()){
				auto.executeQueue();
			}

		}else {

			auto.executeQueue();
			firstTime = true;

		}

	}

	@Override
	public void autonomousPeriodic() {
		
		// intake.setIntakeSpeed(-1.0);

		// trigger.setSpeed(-0.1);

		System.out.println(auto.getQueue());
		auto.executeQueue();

		// shooterController.aim();

		// switch(autoIndex){
		// 	case 0:
		// 		twoBallAuto();
		// 		break;
		// 	case 1:
		// 		threeBallAuto();
		// 		break;
		// 	case 2: 
		// 		fourBallAuto();
		// 		break;	
		// }
		
	}
	// NO TOUCH
	@Override 
	public void disabledInit() {}
	
	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {
		//System.out.println(String.format("distance: %.2f; distanceCorrected: %.2f", limelight.getDistance(), shooterController.correctLimelightDistanceError(limelight.getDistance())));
		// System.out.println("distanceCorrected: " + String.format("%.2f", shooterController.correctLimelightDistanceError(limelight.getDistance())));

	}

	double topSpeed;
	double bottomSpeed;

	Dashboard.slider topSlider;
	Dashboard.slider bottomSlider;

	Dashboard.graph topGraph;
	Dashboard.graph bottomGraph;

	Dashboard.slider distance;

	
	@Override
	public void teleopInit() {

		turret.setZero();

		driver.setupButtons();
		operator.setupButtons();

		topSpeed = 0.0;
		bottomSpeed = 0.0;

		lastSpeedSet = 0.0;
		lastIntakeSpeedSet = 0.0;


	}

	boolean intakeTesting = false;

	/**
	 * @param intakemode normal intake: 1, test intake:-1
	 */
	public void intake(int intakemode) {

		double speedSet = -operator.getAxis(XboxController.Axes.RightTrigger);

		if (Math.abs(speedSet) - Math.abs(lastIntakeSpeedSet) < -intakeValue) {

			if (speedSet + lastIntakeSpeedSet < 0.0) {

				speedSet = lastIntakeSpeedSet + intakeValue;
				

			} else if (speedSet + lastSpeedSet > 0.0) {

				speedSet = lastIntakeSpeedSet - intakeValue;

			}

		} 

		lastIntakeSpeedSet = speedSet;

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


		intake.setIntakeSpeed(speedSet * intakemode);

		if (operator.getAxis(XboxController.Axes.RightTrigger) > 0.1 || Math.abs(speedSet) > 0.0) {

			trigger.setSpeed(-0.4);

		} else {

			trigger.setSpeed(0.0);

		}

	}

	@Override
	public void teleopPeriodic() {

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

		topGraph.addData(shooter.topWheel.getSpeed(), topSlider.getValue());
		bottomGraph.addData(shooter.bottomWheel.getSpeed(), bottomSlider.getValue());

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
	
		if (operator.getButton(XboxController.Buttons.A)) {

			shooterController.aim();

		} else {

			shooterController.stopAim();

		}

		if (shooterController.aligned && operator.getButton(XboxController.Buttons.B)) {

			shooterController.fire();

		} else {

			if (!operator.getButton(XboxController.Buttons.B)) {

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
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}