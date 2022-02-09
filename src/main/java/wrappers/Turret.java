package wrappers;

import interfaces.PIDMotor;
import utils.*;

public class Turret {

    PIDMotor motor;
    double maxRotation; 
    double resetEncoderSpeed;
    double acceptableError = 0.05;
    boolean iszeroing = false;

    /**
     * @param motor turret motor
     * @param maxRotation amount of rotations of the turret centered on zeroed position
     * @param resetEncoderSpeed speed at which it zeros to
     */
    public Turret(PIDMotor motor, double maxRotation, double resetEncoderSpeed) {

        this.motor = motor;
        this.maxRotation = maxRotation * Constants.FULL_TURRET_ROTATION / 2;
        this.resetEncoderSpeed = resetEncoderSpeed;
        setZero();


    }

    /**
     * @param speed speed at which it rotates at
     */
    public void rotate(double speed) {

        if (getPosition() >= 1.0) {
            
            if (speed > 0.0) {

                motor.setSpeed(0.0);

            } else {

                motor.setSpeed(speed);

            }
            
        } else if (getPosition() <= -1.0) {
            
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

        if (position >= maxRotation) {

            position = maxRotation;

        }
        
        if (position <= -maxRotation) {

            position = -maxRotation;

        }

        motor.setPosition(position, -speed, speed);

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

}