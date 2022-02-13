package utils;

import wrappers.*;

public class PositionalLinearBangBang {

    private double increment = 0.01;

    private double acceptableError = 0.03;

    private double newPosition = 0.0;

    private Turret turret;
 
    public PositionalLinearBangBang(Turret turret, double increment, double acceptableError) {

        this.turret = turret;
        this.increment = increment;
        this.acceptableError = acceptableError;

    }

    public void setIncrement(double increment) {

        this.increment = increment;
        
    }

    public void init() {

        newPosition = 0.0;

    }

    public double getCommand(double position) {

        if (turret.getPosition() < position - acceptableError) {

            newPosition += increment;

        } else if (turret.getPosition() > position + acceptableError) {

            newPosition -=  increment;

        }

        if (Math.abs(newPosition) >= 1.0) {

            newPosition = Math.signum(newPosition);

        }

        return newPosition;

    }

} 

