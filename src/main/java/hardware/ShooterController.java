package hardware;

import utils.PIDLoop;
import wrappers.*;
import wrappers.Dashboard.graph;

public class ShooterController {

	Shooter shooter;

	Intake intake;

	Trigger trigger;

	Turret turret;

	Limelight limelight;

	Drive drive;

	// swap out current alignment math with PIDLoop class

	// these may need some tuning as things change
	private double maxSpeed = 0.25;  //0.15 worked

	private double acceptableAngleError = 2.3;

	private double minSpeed = 0.035;

	private double converter = 2.0;

    public static boolean aligned = false;

	PIDLoop aimLoop;

	double P = 2.5;
	double I = 0.0;
	double D = 0.19;
	double FF = 0.0;
	int Izone = 25;

	//Dashboard.graph graph;

	public ShooterController(Shooter shooter, Limelight limelight, Drive drive, Intake intake, Trigger trigger, Turret turret) {
		this.shooter = shooter;

		this.intake = intake;

		this.trigger = trigger;

		this.turret = turret;

		this.limelight = limelight;

		this.drive = drive;

		//graph = new Dashboard.graph("turret speed");
		  //0.5,0.5,0 PID worked ok
		//aimLoop = new PIDLoop(.95, .15, .075, 1, 25); GOOD PID
		
        aimLoop = new PIDLoop(P, I, D, FF, Izone);
        
	}

	// stops the shooter
	public void stop() {

		shooter.stop();

		trigger.setSpeed(0.0);

		shooter.eval(0);

	}

	// checks if the robot is aligned and if the shooter is spun up, then updates internal variables accordingly
	public void eval() {

		double offset = correctLimelightDistanceError(limelight.getDistance());

		double angle = limelight.getHorizontalAngle() - offset;

		aligned = Math.abs(angle) <= acceptableAngleError; 
	 
		shooter.eval(correctLimelightDistanceError(limelight.getDistance()));

	}

	// this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
	public void fire() {

		shooter.setShooterSpeeds(correctLimelightDistanceError(limelight.getDistance()));

		eval();

		if (Shooter.readyToFire) {

			// random variables, need to be tested
			trigger.setSpeed(0.25);
			intake.setIntakeSpeed(0.25);

		}

		//System.out.println("shooter: " + Shooter.readyToFire);

	}

	// this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
	// FOR WHEN LIMELIGHT CRAPS OUT
	public void emergencyFire() {

		shooter.setShooterSpeeds(15 * 12);

		if (Shooter.readyToFire) {

			// random variables, need to be tested
			trigger.setSpeed(0.25);
			intake.setIntakeSpeed(0.25);

		}

		//System.out.println("shooter: " + Shooter.readyToFire);

	}

	public double correctLimelightDistanceError(double rawDistance) {

		// each index after 0 is 2 feet of distance from the target starting at 5ft away
		double[] errorData = {-0.157, 0.395, 1.5,
							  6.25, 7.1, 11.5,
							  15.55, 26.1, 29.1
							  -13.9, -16.9};

		double arrayPosition = (rawDistance - 60.0) / 24.0;

		if(arrayPosition < 0) {

			return rawDistance;

		}

		if(arrayPosition >= 10) {

			return rawDistance + 20;

		}

		int lowerIndex = (int)arrayPosition;
		int upperIndex = lowerIndex + 1;

		double percentBetweenPoints = arrayPosition - (double)lowerIndex;

		double correctionFactor = errorData[lowerIndex] + (((errorData[upperIndex] - errorData[lowerIndex])) * percentBetweenPoints);
		
		return rawDistance + correctionFactor;

	}

	// this aligns the robot with the vision target found by the limelight
	public void aim() {

		double angle = limelight.getHorizontalAngle();

		double desiredPosition = turret.getPosition() + angle / 248;

		aligned = Math.abs(angle) <= acceptableAngleError;

		double speed = -aimLoop.getCommand(desiredPosition, turret.getPosition()) * converter; 

		if (Math.abs(speed) < minSpeed) {

			speed = minSpeed * Math.signum(speed);

		}

		if (Math.abs(speed) > maxSpeed) {

			speed = maxSpeed * Math.signum(speed);

		}

		//turret.setPosition(speed, position);
		//turret.rotate(speed);
		System.out.println("angle: " + String.format("%.2f", angle));
		System.out.println("turret " + String.format("%.2f", turret.getPosition() + angle / 287));
		System.out.println("positi " + String.format("%.2f", turret.getPosition()));
		
		//graph.addData(turret.getSpeed(), speed);

		turret.setPosition(speed, desiredPosition);

	}

}