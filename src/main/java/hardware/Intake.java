package hardware;

import wrappers.*;
import interfaces.*;

public class Intake {

    PIDMotor motor;
    DoubleSolenoid piston;

    double lastIntakeSpeedSet;
	final double intakeValue = 0.025;

    public Intake(PIDMotor motor, DoubleSolenoid piston) {

        this.motor = motor;
        this.piston = piston;

        lastIntakeSpeedSet = 0.0;

    }

    public void runIntake(double speed) {

        putDownIntake();
        setIntakeSpeed(speed);

    }

    public void stopIntake() {

        putUpIntake();
        setIntakeSpeed(0.0);

    }

    public void putDownIntake() {

        piston.activateChannel(true);

    }

    public void putUpIntake() {

        piston.activateChannel(false);

    }

    public void setIntakeSpeed(double speed) {

        double speedSet = speed;
/*
		if (Math.abs(speedSet) - Math.abs(lastIntakeSpeedSet) < -intakeValue) {

			if (speedSet + lastIntakeSpeedSet < 0.0) {

				speedSet = lastIntakeSpeedSet + intakeValue;
				

			} else if (speedSet + lastIntakeSpeedSet > 0.0) {

				speedSet = lastIntakeSpeedSet - intakeValue;

			}

		} 

        if (Math.abs(speedSet) - Math.abs(lastIntakeSpeedSet) > intakeValue) {

			if (speedSet + lastIntakeSpeedSet > 0.0) {

				speedSet = lastIntakeSpeedSet + intakeValue;
				

			} else if (speedSet + lastIntakeSpeedSet < 0.0) {

				speedSet = lastIntakeSpeedSet - intakeValue;

			}

		} 
*/
		//lastIntakeSpeedSet = speedSet;

        if (speedSet > 1.0) {

            speedSet = 1.0;

        }

        if (speedSet < -1.0) {

            speedSet = -1.0;

        }

        motor.setSpeed(speedSet);

    }

    public double getSpeedSet() {

        return lastIntakeSpeedSet;

    }

    public void init() {

        lastIntakeSpeedSet = 0.0;

    }

}