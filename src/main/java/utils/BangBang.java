package utils;

import interfaces.PIDMotor;

public class BangBang {

    private double increment = 0.001;

    private double acceptableError = 0.03;

    private double newSpeed = 0.0;

    private PIDMotor motor;
 
    public BangBang(PIDMotor motor, double increment, double acceptableError) {

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

    public double getCommand(double speed) {

        if (motor.getSpeed() < speed - acceptableError) {

            newSpeed += increment;

        } else if (motor.getSpeed() > speed + acceptableError) {

            newSpeed -=  increment;

        }

        return newSpeed;

    }  

} 

