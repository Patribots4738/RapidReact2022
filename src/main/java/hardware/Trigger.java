package hardware;

import interfaces.*;

public class Trigger {

    public PIDMotor motor;
    
    public Trigger(PIDMotor motor) {

        this.motor = motor;

    }

    public void setSpeed(double speed) {

        if (speed > 1.0) {

            speed = 1.0;

        }

        if (speed < -1.0) {

            speed = -1.0;

        }

        motor.setSpeed(speed);

    }

    public double getSpeed() {

        return motor.getSpeed();

    }

    public void stop() {

        motor.setPercent(0.0);

    }

}
