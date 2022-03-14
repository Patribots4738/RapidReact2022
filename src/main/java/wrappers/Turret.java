package wrappers;

import interfaces.PIDMotor;
import utils.*;

public class Turret {

    PIDMotor motor;

    private double maxRotation; 

    double acceptableError = 0.05;
    boolean iszeroing = false;

    double offset = 0.01;
    boolean isGoingRight = false; 

    double turretOffset = 0.0;

    /**
     * @param motor turret motor
     * @param maxRotation amount of rotations of the turret centered on zeroed position
     * @param resetEncoderSpeed speed at which it zeros to
     */
    public Turret(PIDMotor motor, double maxRotation) {

        this.motor = motor;
        this.maxRotation = maxRotation * Constants.FULL_TURRET_ROTATION / 2;
        setZero(0.0);

    }

    public double getMaxRotation() {
        
        return maxRotation;

    }


    /**
     * @param speed speed at which it rotates at
     */
    public void rotate(double speed) {

        //System.out.println("pos: " + this.getPosition());
        
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

        if (this.getPosition() >= maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (position > maxRotation) {
                
                motor.setPosition((maxRotation / Constants.FULL_TURRET_ROTATION) + (turretOffset * Constants.FULL_TURRET_ROTATION), 0.0, 0.0);

            } else {
                
                motor.setPosition(position + (turretOffset * Constants.FULL_TURRET_ROTATION), -speed, speed);

            }
            
        } else if (this.getPosition() <= -maxRotation / Constants.FULL_TURRET_ROTATION) {
            
            if (position < -maxRotation) {

                motor.setPosition((-maxRotation / Constants.FULL_TURRET_ROTATION) + (turretOffset * Constants.FULL_TURRET_ROTATION), 0.0, 0.0);

            } else {

                motor.setPosition(position + (turretOffset * Constants.FULL_TURRET_ROTATION), -speed, speed);

            }

        } else {
            
            motor.setPosition(position + (turretOffset * Constants.FULL_TURRET_ROTATION), -speed, speed);

        }

    }

    /**
     * @return position of turret in turret rotations
     */
    public double getPosition() {

        return motor.getPosition() / Constants.FULL_TURRET_ROTATION - turretOffset;

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
     * @param offset turret zero offset in robot rotations, POSITIVE offsets are to the LEFT of the actual zero
     */
    public void setZero(double turretOffset) {

        motor.resetEncoder();
        this.turretOffset = turretOffset;

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