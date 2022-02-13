package utils;

import edu.wpi.first.networktables.NetworkTablesJNI;
import interfaces.PIDMotor;

public class DynamicBangBang {

    private double maxIncrement = 0.1;

    private double acceptableError = 0.03;

    private double newSpeed = 0.0;

    private PIDMotor motor;

    private double desiredSpeed;
 
    public DynamicBangBang(PIDMotor motor, double maxIncrement, double acceptableError) {

        this.motor = motor;
        this.maxIncrement = maxIncrement;
        this.acceptableError = acceptableError;

    }

    public void setMaxIncrement(double maxIncrement) {

        this.maxIncrement = maxIncrement;
        
    }

    private void setDesiredSpeed(double desiredSpeed){

        this.desiredSpeed = desiredSpeed;

    }

    public void init() {

        newSpeed = 0.0;

    }
    
    // desmos graph of functions: https://www.desmos.com/calculator/8rb1xl9mbz
    public double getCommand(double speed) {

        speed = Math.abs(speed);        
        double dynamicIncrement = 0.0;

        if (Math.abs(motor.getSpeed()) < speed - acceptableError) {
// use m(x)
            System.out.println("BELOW");
            dynamicIncrement = equationM(Math.abs(speed));
            newSpeed += dynamicIncrement;

        } else if (Math.abs(motor.getSpeed()) > speed + acceptableError) {
// use n(x)
            System.out.println("ABOVE");
            dynamicIncrement = equationN(Math.abs(speed));   
            newSpeed -=  dynamicIncrement;

            if (speed == 0) {
                System.out.println("ZEROING");
                dynamicIncrement = Math.signum(motor.getSpeed()) * equationN(Math.abs(0.01));
                newSpeed -=  dynamicIncrement;

            }

        } else if (speed == 0.0) {

            System.out.println("ZERO");
            newSpeed = 0.0;

        } if (Math.signum(speed) < 0.0) {
            newSpeed *= -1;

        }

        // System.out.println(String.format("Just Speed: %.2f", speed));
        System.out.println(String.format("New Speed: %.2f; increment: %.2f; Desired Speed: %.2f", newSpeed, dynamicIncrement, speed));

        if (Math.abs(newSpeed) >= 1.0) {

            newSpeed = Math.signum(newSpeed);

        }
        // System.out.println("increment: " + String.format("%.2f", dynamicIncrement)); 
        return newSpeed;

    }

    private double equationM(double speed) {

        setDesiredSpeed(speed);
        return ((equationH() - equationF(motor.getSpeed() + 1)) * equationW());

    }

    private double equationN(double speed) {

        setDesiredSpeed(speed);
        return ((equationH() - equationF(2 * equationH() - (motor.getSpeed() + 1))) * equationW());
        
    }

    private double equationF(double speed) {

        return ((Math.pow(Math.E, equationG(speed))) / (Math.pow(Math.E, equationG(speed)) + 1)) * equationH();

    }

    private double equationG(double speed) {
    
        return (equationJ(speed) * 7 - (5));

    }

    private double equationJ(double speed) {
        
        return (speed * (1/equationH()));

    }

    private double equationH() {

        return desiredSpeed + 1;

    }

    private double equationW() {

        return ((maxIncrement)/(2)) * 1.2;

    }

} 

