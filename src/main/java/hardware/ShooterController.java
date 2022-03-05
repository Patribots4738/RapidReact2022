package hardware;

import utils.PIDLoop;
import wrappers.*;
import wrappers.Dashboard.graph;
import wrappers.Dashboard.slider;	
import utils.*;

public class ShooterController {

	Shooter shooter;

	Intake intake;

	Trigger trigger;

	Turret turret;

	Limelight limelight;

	Drive drive;

	private double maxSpeed = 0.25;

	private double acceptableAngleError = 1.3  ;

	private double minSpeed = 0.06;

	private double converter = 2.0;

    public static boolean aligned = false;

	private boolean isGoingRight = true;

	private double offset = 0.03;

	// private PositionalLinearBangBang linearBang; ben wut is dis

	PIDLoop aimLoop;

	double P = 1.5;//2.0;
	double I = 0.0;
	double D = 0.0;
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

		///shooter.setShooterSpeeds(correctLimelightDistanceError(limelight.getDistance()));
		shooter.setShooterSpeeds(limelight.getDistance());

		eval();

		if (Shooter.readyToFire) {

			// random variables, need to be tested
			trigger.setSpeed(0.35);
			intake.setIntakeSpeed(-1.0);

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
		// edit-- attempting to get it after every foot because more data --> more accuracy.
		// explain more, tysm </3
		double[] errorData = {-14.34,
							-14.8	,
							-13.78	,
							-13.58	,
							-12.44	,
							-10.04	,
							-8.41	,
							-6.92	,
							-5.7	,
							-5.1	,
							0.89	,
							-4.22	,
							-0.46	,
							-4.46	,
							5.34	,
							3.24	,
							1.08	,
							6.98	,
							4.88	,
							-15.99	,
							-21.49};

							  

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

		double speed = aimLoop.getCommand(0.0, currentError) * 2.2;

		if (Math.abs(speed) < minSpeed) {

			speed = minSpeed * Math.signum(speed);

		}

		if (Math.abs(speed) > maxSpeed) {

			speed = maxSpeed * Math.signum(speed);

		}

		aligned = Math.abs(angle) <= acceptableAngleError;

		if (!aligned) {

			//turret.setPosition(speed, (currentError + currentPos));
			//System.out.println("speed: " + speed);
			turret.setPosition(speed, currentPos + currentError);//linearBang.getCommand(currentPos + currentError));
			
		} else {

			turret.setPosition(0.0, currentPos);

		}

		System.out.println(aligned);

		if (!limelight.targetFound()) {

			//0.2 fast boi
			turret.scan(0.125);
			//System.out.println((limelight.getHorizontalAngle() > 0)? "Going Right" : "Going Left");
			
		} else {

			turret.setIsGoingRight(limelight.getHorizontalAngle() > 0);

		}

	}

	public void setShooterSpeeds(double distance){

		shooter.setShooterSpeeds(distance);

	}

}