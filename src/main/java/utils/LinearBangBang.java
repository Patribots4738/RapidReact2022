package utils;

import interfaces.PIDMotor;

public class LinearBangBang {

    private double increment = 0.01;

    private double acceptableError = 0.03;

    private double newSpeed = 0.0;

    private PIDMotor motor;
 
    public LinearBangBang(PIDMotor motor, double increment, double acceptableError) {

        this.motor = motor;
        this.increment = increment;
        this.acceptableError = acceptableError;

    }

    public void setIncrement(double increment) {

        this.increment = increment;
        
    }

    public void init() {

        newSpeed = 0.0;

    }

    public double getLinearCommand(double speed) {

        if (motor.getSpeed() < speed - acceptableError) {

            newSpeed += increment;

        } else if (motor.getSpeed() > speed + acceptableError) {

            newSpeed -=  increment;

        }

        if (Math.abs(newSpeed) >= 1.0) {

            newSpeed = Math.signum(newSpeed);

        }

        return newSpeed;

    }

} 

