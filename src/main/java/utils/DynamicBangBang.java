package utils;

import interfaces.PIDMotor;

public class DynamicBangBang {

    private double maxIncrement = 0.1;

    private double acceptableError = 0.03;

    private double newSpeed = 0.0;

    private PIDMotor motor;
 
    public DynamicBangBang(PIDMotor motor, double maxIncrement, double acceptableError) {

        this.motor = motor;
        this.maxIncrement = maxIncrement;
        this.acceptableError = acceptableError;

    }

    public void setMaxIncrement(double maxIncrement) {

        this.maxIncrement = maxIncrement;
        
    }

    public void init() {

        newSpeed = 0.0;

    }
    
    // desmos graph of functions: https://www.desmos.com/calculator/8rb1xl9mbz
    public double getCommand(double speed) {

        double speedSign = Math.signum(speed);
        double dynamicIncrement = 0.0;

        if (motor.getSpeed() < speed - acceptableError) {
// use m(x)
            dynamicIncrement = speedSign * equationM(Math.abs(speed));
            newSpeed += dynamicIncrement;

        } else if (motor.getSpeed() > speed + acceptableError) {
// use n(x)
            dynamicIncrement = speedSign * equationN(Math.abs(speed));   
            newSpeed -=  dynamicIncrement;

        }

        if (Math.abs(newSpeed) >= 1.0) {

            newSpeed = Math.signum(newSpeed);

        }

        return newSpeed;

    }

    private double equationM(double speed) {

        double m;   
        m = ((equationH(speed) - equationF(motor.getSpeed() + 1)) * equationW(speed));
        return m;

    }

    private double equationN(double speed) {

        double n;
        n = ((equationH(speed) - equationF(2 * equationH(speed) - (motor.getSpeed() + 1))) * equationW(speed));
        return n;
        
    }

    private double equationF(double speed) {

        double f;
        f = ((Math.pow(Math.E, equationG(speed))) / (Math.pow(Math.E, equationG(speed)) + 1)) * equationH(speed);
        return f;

    }

    private double equationG(double speed) {
    
        double g;
        g = (equationJ(speed) * 7 - (5));
        return g;

    }

    private double equationJ(double speed) {

        double j;
        j = (motor.getSpeed() * (1/equationH(speed)));
        return j;

    }

    private double equationH(double speed) {

        double h;
        h = speed + 1;
        return h;

    }

    private double equationW(double speed) {

        double w;
        w = ((maxIncrement)/(2)) * 1.2;
        return w; 

    }

} 

