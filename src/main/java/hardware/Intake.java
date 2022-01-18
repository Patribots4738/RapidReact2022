package hardware;

import wrappers.*;
import interfaces.*;

public class Intake {

    PIDMotor motor;
    DoubleSolenoid piston;

    public Intake(PIDMotor motor, DoubleSolenoid piston) {

        this.motor = motor;
        this.piston = piston;

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

        if (speed > 1.0) {

            speed = 1.0;

        }

        if (speed < -1.0) {

            speed = -1.0;

        }

        motor.setSpeed(speed);

    }

}