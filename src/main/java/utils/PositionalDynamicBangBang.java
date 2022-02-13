package utils;

import wrappers.*;

public class PositionalDynamicBangBang {

    private double maxIncrement = 0.1;

    private double acceptableError = 0.03;

    private double newPosition = 0.0;

    private Turret turret;

    private double desiredPosition;
 
    public PositionalDynamicBangBang(Turret turret, double maxIncrement, double acceptableError) {

        this.turret = turret;
        this.maxIncrement = maxIncrement;
        this.acceptableError = acceptableError;

    }

    public void setMaxIncrement(double maxIncrement) {

        this.maxIncrement = maxIncrement;
        
    }

    private void setDesiredPosition(double desiredPosition){

        this.desiredPosition = desiredPosition;

    }

    public void init() {

        newPosition = 0.0;

    }
    
    // desmos graph of functions: https://www.desmos.com/calculator/8rb1xl9mbz
    public double getCommand(double position) {

        double positionSign = Math.signum(position);
        double dynamicIncrement = 0.0;

        if (turret.getPosition() < position - acceptableError) {
// use m(x)
             System.out.println("BELOW");
            dynamicIncrement = positionSign * equationM(Math.abs(position));
            newPosition += dynamicIncrement;

        } else if (turret.getPosition() > position + acceptableError) {
// use n(x)
             System.out.println("ABOVE");
            dynamicIncrement = positionSign * equationN(Math.abs(position));   
            newPosition -=  dynamicIncrement;

        } else if (position == 0.0) {

             System.out.println("ZERO");
            newPosition = 0.0;

        }

        if (Math.abs(newPosition) >= 1.0) {

            newPosition = Math.signum(newPosition);

        }
         System.out.println("increment: " + String.format("%.2f", dynamicIncrement)); 
         System.out.println("newPosition: " + String.format("%.2f", newPosition));
        return newPosition;

    }

    private double equationM(double position) {

        setDesiredPosition(position);
        return ((equationH() - equationF(turret.getPosition() + 1)) * equationW());

    }

    private double equationN(double position) {

        setDesiredPosition(position);
        return ((equationH() - equationF(2 * equationH() - (turret.getPosition() + 1))) * equationW());
        
    }

    private double equationF(double position) {

        return ((Math.pow(Math.E, equationG(position))) / (Math.pow(Math.E, equationG(position)) + 1)) * equationH();

    }

    private double equationG(double position) {
    
        return (equationJ(position) * 7 - (5));

    }

    private double equationJ(double position) {
        
        return (position * (1/equationH()));

    }

    private double equationH() {

        return desiredPosition + 1;

    }

    private double equationW() {

        return ((maxIncrement)/(2)) * 1.2;

    }

} 

