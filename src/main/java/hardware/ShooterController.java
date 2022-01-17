package hardware;

import utils.PIDLoop;
import wrappers.Limelight;

public class ShooterController {

	Shooter shooter;

	//Conveyor conveyor;

	Limelight limelight;

	Drive drive;

	// swap out current alignment math with PIDLoop class

	// these may need some tuning as things change
	private double maxSpeed = 0.12;  //0.15 worked

	private double acceptableAngleError = .9;

	private double minSpeed = 0.033;

	private double converter = 1.0 / 15;

    public static boolean aligned = false;
    
	private double shortOffset = 3.55;//3.55;

	private double longOffset = 1.68;

	PIDLoop aimLoop;

	double P = 0.95;
	double I = 0.15;
	double D = 0.075;
	double FF = 1;
	int Izone = 25;

	public ShooterController(Shooter shooter, Limelight limelight, Drive drive) {

		this.shooter = shooter;

		//this.conveyor = conveyor;

		this.limelight = limelight;

		this.drive = drive;
		  //0.5,0.5,0 PID worked ok
		//aimLoop = new PIDLoop(.95, .15, .075, 1, 25); GOOD PID
		
        aimLoop = new PIDLoop(P, I, D, FF, Izone);
        
	}

	// stops the shooter
	public void stop() {

		shooter.stop();

		//conveyor.setSpeed(0);

		shooter.eval(0);

	}

	// checks if the robot is aligned and if the shooter is spun up, then updates internal variables accordingly
	public void eval() {

		double offset = (correctLimelightDistanceError(limelight.getDistance()) > 200) ? (longOffset) : (shortOffset);

		double angle = limelight.getHorizontalAngle() - offset;

		aligned = Math.abs(angle) <= acceptableAngleError; 
	 
		shooter.eval(correctLimelightDistanceError(limelight.getDistance()));

	}

	// this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
	public void fire() {

		shooter.setShooterSpeeds(correctLimelightDistanceError(limelight.getDistance()));

		eval();

		//conveyor.setConveyor(Shooter.readyToFire);

		shooter.setFeeders(Shooter.readyToFire);

		//System.out.println("shooter: " + Shooter.readyToFire);

	}

	// this spins up the shooter and sets the conveyor and feeders based on wether the shooter is up to speed
	// FOR WHEN LIMELIGHT CRAPS OUT
	public void emergencyFire() {

		shooter.setShooterSpeeds(15 * 12);

		//conveyor.setConveyor(Shooter.readyToFire);

		shooter.setFeeders(Shooter.readyToFire);

		//System.out.println("shooter: " + Shooter.readyToFire);

	}

	public double correctLimelightDistanceError(double rawDistance) {

		// each index after 0 is 2 feet of distance from the target starting at 10ft away
		double[] errorData = {-0.157, 0.395, 1.5,
							  6.25, 7.1, 11.5,
							  15.55, 26.1, 29.1
							  -13.9, -16.9, 0.1,
							  11.1};

		double arrayPosition = (rawDistance - 120.0) / 24.0;

		if(arrayPosition < 0) {

			return rawDistance;

		}

		if(arrayPosition >= 11) {

			return rawDistance +20;

		}

		int lowerIndex = (int)arrayPosition;
		int upperIndex = lowerIndex + 1;

		double percentBetweenPoints = arrayPosition - (double)lowerIndex;

		double correctionFactor = errorData[lowerIndex] + (((errorData[upperIndex] - errorData[lowerIndex])) * percentBetweenPoints);
		
		return rawDistance + correctionFactor;

	}

	// this aligns the robot with the vision target found by the limelight
	public void aim() {

		double offset = (correctLimelightDistanceError(limelight.getDistance()) > 200) ? (longOffset) : (shortOffset);

		double angle = ((limelight.getHorizontalAngle()) - offset);

		aligned = Math.abs(angle) <= acceptableAngleError;

		double speed = -(aimLoop.getCommand(0, angle) * converter); 

		if(Math.abs(speed) < minSpeed) {

			speed = minSpeed * Math.signum(speed);

		}

		if(Math.abs(speed) > maxSpeed) {

			speed = maxSpeed * Math.signum(speed);

		}

        drive.bananaTank(speed, -speed);

	}

}