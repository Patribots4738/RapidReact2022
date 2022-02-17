package wrappers;

import interfaces.PIDMotor;
import utils.*;

public class Turret {

    PIDMotor motor;

    private double maxRotation; 

    double acceptableError = 0.05;
    boolean iszeroing = false;

    double offset = 0.01;
    boolean isGoingRight = true;

    /**
     * @param motor turret motor
     * @param maxRotation amount of rotations of the turret centered on zeroed position
     * @param resetEncoderSpeed speed at which it zeros to
     */
    public Turret(PIDMotor motor, double maxRotation) {

        this.motor = motor;
        this.maxRotation = maxRotation * Constants.FULL_TURRET_ROTATION / 2;
        setZero();


    }

    public double getMaxRotation() {
        
        return maxRotation;

    }


    /**
     * @param speed speed at which it rotates at
     */
    public void rotate(double speed) {

        System.out.println("pos: " + this.getPosition());
        
        if (this.getPosition() > maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (speed > 0.0) {

                motor.setSpeed(0.0);

            } else {

                motor.setSpeed(speed);

            }
            
        } else if (this.getPosition() < -maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (speed < 0.0) {

                motor.setSpeed(0.0);

            } else {

                motor.setSpeed(speed);

            }

        } else {
            
            motor.setSpeed(speed);

        }
 

    }

    /**
     * @param speed speed at which it gets to position
     * @param position turret rotations to either side of zero point
     */
    public void setPosition(double speed, double position) {

        position *= Constants.FULL_TURRET_ROTATION;

        if (getPosition() >= maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (position > maxRotation) {
                

            } else {

                motor.setPosition(position, -speed, speed);

            }
            
        } else if (getPosition() <= -maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (position < -maxRotation) {

                

            } else {

                motor.setPosition(position, -speed, speed);

            }

        } else {
            
            motor.setPosition(position, -speed, speed);

        }

    }

    /**
     * @return position of turret in turret rotations
     */
    public double getPosition() {

        return motor.getPosition() / Constants.FULL_TURRET_ROTATION;

    }

    /**
     * @return speed of turret in turret rotations
     */
    public double getSpeed() {

        return motor.getSpeed();

    }

    /**
     * zeros encoder of motor, should be called
     * in auto/teleop/robot init
     */
    public void setZero() {

        motor.resetEncoder();

    }

    public void setIsGoingRight(boolean isGoingRight) {

        this.isGoingRight = isGoingRight;

    }

    public boolean getIsGoingRight() {

        return isGoingRight;    

    }

    public void scan(double speed) {

        if (this.getPosition() > ((maxRotation / Constants.FULL_TURRET_ROTATION) - offset)) {

            setIsGoingRight(false);

        }

        else if (this.getPosition() < -((maxRotation / Constants.FULL_TURRET_ROTATION) - offset)){

            setIsGoingRight(true);

        }

        if(isGoingRight) {

            this.rotate(speed);

        } else {

            this.rotate(-speed);

        }

	}

}