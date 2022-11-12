package hardware;

import utils.PIDLoop;
import wrappers.*;
import utils.*;

public class ShooterController {

	Shooter shooter;

	Intake intake;

	public Trigger trigger;

	Turret turret;

	Limelight limelight;

	Drive drive;

	private double maxSpeed = 0.2;

	private double acceptableAngleError = 1.5;

	private double minSpeed = 0.06;

	private double converter = 2.0;

    public static boolean aligned = false;

	private boolean isGoingRight = true;

	private double offset = 0.03;

	// private PositionalLinearBangBang linearBang; ben wut is dis

	PIDLoop aimLoop;

	double P = 2.1;
	double I = 0.0;
	double D = 0.23;
	double FF = 0.0;
	int Izone = 25;

	public ShooterController(Shooter shooter, Limelight limelight, Drive drive, Intake intake, Trigger trigger, Turret turret) {
		
		this.shooter = shooter;

		this.intake = intake;

		this.trigger = trigger;

		this.turret = turret;

		this.limelight = limelight;

		this.drive = drive;
		
        aimLoop = new PIDLoop(P, I, D, FF, Izone);

		shootDelay = new TimeLoop(1);
        
	}

	// stops the shooter
	public void stop() {

		shooter.topWheel.setSpeed(0.09);
		shooter.bottomWheel.setSpeed(0.5);

		trigger.setSpeed(0.0);
		intake.setIntakeSpeed(0.0);

		shooter.eval(0);

	}

	// checks if the robot is aligned 
	// and if the shooter is spun up, 
	// then updates internal variables accordingly
	public void eval() {

		double angle = limelight.getHorizontalAngle();

		aligned = Math.abs(angle) <= acceptableAngleError; 
	 
		shooter.eval(correctLimelightDistanceError(limelight.getDistance()));

	}

	TimeLoop shootDelay;
	public boolean firstShootingTime = true;
	public boolean firstAutoTime = true;
	double startTime = 0.0;
	double autoStartTime = 0.0;
	double distance = 0.0;

	Countdown countdown;
	boolean firstStopTime = true;

	// this spins up the shooter and sets the conveyor/feeders
	// based on whether the shooter is up to speed
	public void fire() {

		eval();
		distance = correctLimelightDistanceError(limelight.getDistance());
		shooter.setShooterSpeeds(distance);

		if (shooter.readyToFire) {

			firstStopTime = true;

			if (firstShootingTime) {

				startTime = Timer.getTime();
				firstShootingTime = false;

			}

			if ((Timer.getTime() - startTime) > 0.5) {

				intake.setIntakeSpeed(-0.3);

			}

			trigger.setSpeed(0.35);

		} else {

			trigger.setSpeed(-0.1);
			intake.setIntakeSpeed(-0.1);

		}

	}

	public void autoFire(int auto) {

		eval();

		if (firstAutoTime) {

			firstAutoTime = false;
			autoStartTime = Timer.getTime();

		}

		if (Timer.getTime() - autoStartTime > 1.5) {

			firstStopTime = true;

			if (firstShootingTime) {

				startTime = Timer.getTime();
				firstShootingTime = false;

			}

			if (((Timer.getTime() - startTime) > 1.0) && (Timer.getTime() - startTime) < 1.5) {

				intake.setIntakeSpeed(-0.275);//0.3
				trigger.setSpeed(0.35);

			} else if (((Timer.getTime() - startTime) > 1.5) && (Timer.getTime() - startTime) < 2) {

				intake.setIntakeSpeed(-1.0);
				trigger.setSpeed(-0.3);

			} else if ((Timer.getTime() - startTime) > 2) {

				intake.setIntakeSpeed(-0.275);//0.3
				trigger.setSpeed(0.35);

			}

			if (Timer.getTime() - startTime < 0.3) {

				intake.setIntakeSpeed(0.1);
				trigger.setSpeed(-0.3);

			} else if ((Timer.getTime() - startTime > 0.3) && (Timer.getTime() - startTime < 0.5)) {

				intake.setIntakeSpeed(0);
				trigger.setSpeed(0.0);

			} else if ((Timer.getTime() - startTime) < 0.6 && (Timer.getTime() - startTime) > 0.5) {

				trigger.setSpeed(0.35);//0.35

			} else if ((Timer.getTime() - startTime) < 1.0) {

				trigger.setSpeed(0.0);

			}

		} else {

			if (Timer.getTime() - autoStartTime < 1.0) {

				trigger.setSpeed(-0.3);//-0.3
				intake.setIntakeSpeed(-1.0);//-1.0

			} else {

				trigger.setSpeed(0.0);
				intake.setIntakeSpeed(0.0);

			}
			
			// 10/23/22 - hardcoding values for auto
			switch (auto)
			{
				case 0: // topTarmac
				{
					turret.setPosition(0.1, -(0.222));
					shooter.setShooterSpeeds(10.33 * 12);
					break;
				}
				case 1: // midTarmac
				{
					turret.setPosition(0.1, -(0.25));
					
					// due to hangertarmac being a good pain, midtarmac wil attempt to go 43in 
					// rather than 77in
					// shooter.setShooterSpeeds(12.2 * 12); 
					shooter.setShooterSpeeds(10.33 * 12);
					
					break;
				}
				case 2: // hangerTarmac
				{
					// Second parameter in setPosition is manually calculated
					// as (degree you want to go to)/360; ex: (92.7/360) = 0.2639
					
					//turret.setPosition(0.1, -(0.2575));//0.2639 --> 95deg

					turret.setPosition(0.1, -(0.2639));//0.2778 --> 100deg

					shooter.setShooterSpeeds(12.5 * 12); // 13.12; 12.25 sorta worked ?
					
					break;
				}
				case 3: // threeBall and fourBall
				{
					this.aim();
					shooter.setShooterSpeeds(correctLimelightDistanceError(limelight.getDistance()));
					break;
				}
			}
			

		}

	}

	public void autoSecondFire() {


		eval();

		if (firstAutoTime) {

			firstAutoTime = false;
			autoStartTime = Timer.getTime();

		}

		if (Timer.getTime() - autoStartTime > 1.5) {

			firstStopTime = true;

			if (firstShootingTime) {

				startTime = Timer.getTime();
				firstShootingTime = false;

			}

			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-1);

		} else {

			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-1);
			this.aim();

		}

	}

	// FOR WHEN LIMELIGHT CRAPS OUT
	// forces a shot, but, again, emergency
	public void emergencyFire() {

		shooter.setShooterSpeeds(15 * 12);

		if (shooter.readyToFire) {

			// random variables, need to be tested
			
			// why are these random? ^^
			trigger.setSpeed(0.25);
			intake.setIntakeSpeed(0.25);

		}

	}

	public double correctLimelightDistanceError(double rawDistance) {

		/*
			starting at 5 feet, using this google sheet
			https://docs.google.com/spreadsheets/d/1Aa292hdF5q5tXOj8oWQ8oRuvwU20PR3VLEx_ESRx3jg
			you can just copy paste the values into the array
			to do this next year: 
			position a tape measure down the field from the target
			start the robot at 5ft from the target,
			the back of robot at the tape measure
			record the limelight.getDistance(); after logging
			record that data onto a text editor
			then use that sheet and copy paste the output :')
			What this does is corrects the goofy limelight to the correct distance
			for the motor speeds to go to the correct positions --> good pewpew
		*/

		double[] errorData = {-14.34, // 5ft
							-14.8	, 
							-13.78	, 
							-13.58	,
							-12.44	,
							-10.04	, // 10ft
							-8.41	,
							-6.92	,
							-5.7	,
							-5.1	,
							0.89	, // 15ft
							-4.22	,
							-0.46	,
							-4.46	,
							5.34	,
							3.24	, // 20ft
							1.08	,
							6.98	,
							4.88	,
							-15.99	,
							-21.49    // 25ft
							};		

							  

		double arrayPosition = (rawDistance - 60.0) / 12.0;

		if(arrayPosition < 0) {

			return rawDistance;

		}

		if(arrayPosition > errorData.length - 1) {

			return rawDistance - 20;

		}

		int lowerIndex = (int)arrayPosition;
		int upperIndex = lowerIndex + 1;

		double percentBetweenPoints = arrayPosition - (double)lowerIndex;

		double correctionFactor = errorData[lowerIndex] + (((errorData[upperIndex] - errorData[lowerIndex])) * percentBetweenPoints);
		
		return rawDistance + correctionFactor;

	}

	public void aim() {

		double currentPos = turret.getPosition();
		
		double currentError = limelight.getHorizontalAngle() / 248;

		double angle = limelight.getHorizontalAngle();

		double speed = aimLoop.getCommand(0.0, currentError) * 1.75;//2;

		if (Math.abs(speed) < minSpeed) {

			speed = minSpeed * Math.signum(speed);

		}

		if (Math.abs(speed) > maxSpeed) {

			speed = maxSpeed * Math.signum(speed);

		}

		aligned = Math.abs(angle) <= acceptableAngleError;

		if (!aligned) {

			double setPos = currentPos + currentError;

			if (Math.abs(setPos) > (turret.getMaxRotation() / Constants.FULL_TURRET_ROTATION)) {

				setPos = (turret.getMaxRotation() / Constants.FULL_TURRET_ROTATION) * Math.signum(setPos);

			}

			turret.setPosition(speed, setPos);
			
		} else {

			turret.setPosition(0.0, currentPos);

		}


		if (!limelight.targetFound()) {

			if (Math.abs(turret.getPosition()) > turret.getMaxRotation() * 0.75) {

				turret.scan(0.1);

			} else {

				turret.scan(0.3);

			}	
		}
	}

	public void stopAim() {

		turret.setPosition(0.0, turret.getPosition());

	}

	public void setShooterSpeeds(double distance){

		shooter.setShooterSpeeds(distance);

	}

	public void update() {

		double angle = limelight.getHorizontalAngle();
		aligned = Math.abs(angle) <= acceptableAngleError;

	}

}
