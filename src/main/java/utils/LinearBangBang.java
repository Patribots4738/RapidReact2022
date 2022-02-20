package utils;

import interfaces.PIDMotor;

public class LinearBangBang {

    private double increment = 0.01;

    private double acceptableError = 0.03;

    private double newSpeed = 0.0;
 
    public LinearBangBang(double increment, double acceptableError) {

        this.increment = increment;
        this.acceptableError = acceptableError;

    }

    public void setIncrement(double increment) {

        this.increment = increment;
        
    }

    public void init() {

        newSpeed = 0.0;

    }

    public double getCommand(double desired, double actual)
    {
        
        if (Math.signum(desired) != Math.signum(actual) && (actual > acceptableError || actual < -acceptableError)) {

            // System.out.println("Switch Sign");
            desired = 0;

        }
       
        return getCommand(actual - desired);

    }

    public double getCommand(double difference) {

        if (difference < 0 - acceptableError) {

            newSpeed += increment;

        } else if (difference > acceptableError) {

            newSpeed -=  increment;

        }

        if (Math.abs(newSpeed) >= 1.0) {

            newSpeed = Math.signum(newSpeed);

        }

        return newSpeed;

    }

    public void setNewSpeed(double speed) {

        newSpeed = speed;

    }

} 

