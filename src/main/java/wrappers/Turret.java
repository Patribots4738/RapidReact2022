package wrappers;

import interfaces.PIDMotor;

public class Turret {

    PIDMotor motor;
    double maxEncoderPosition; 
    double resetEncoderSpeed;
    double acceptableError = 0.05;
    boolean iszeroing = false;

    /**
     * @param motor turret motor
     * @param maxEncoderPosition maximum position of the encoder so that the turret doesn't twist it'self up, aquired through testing
     * @param resetEncoderSpeed speed at which it zeros to
     */
    public Turret(PIDMotor motor, double maxEncoderPosition, double resetEncoderSpeed) {

        this.motor = motor;
        this.maxEncoderPosition = maxEncoderPosition;
        this.resetEncoderSpeed = resetEncoderSpeed;
        setZero();


    }

    /**
     * @param speed speed at which it rotates at
     */
    public void rotate(double speed) {

        // overrides rotation to get it back to zero so cords dont tangle
        if (iszeroing) {
            
            if (getPosition() < acceptableError && getPosition() > -acceptableError) {

                iszeroing = false;

            }

            setPosition(resetEncoderSpeed, 0.0);
            return;

        }


        if (getPosition() >= 1.0) {
            
            iszeroing = true;
            
        } else if(getPosition() <= -1.0) {
            
            iszeroing = true;

        } else {
            
            motor.setSpeed(speed);

        }
 

    }

    /**
     * @param speed speed at which it gets to position
     * @param position position it goes to, -1.0 to 1.0
     */
    public void setPosition(double speed, double position) {

        position *= maxEncoderPosition;
        
        motor.setPosition(position, -speed, speed);

    }

    /**
     * @return position of motor, -1.0 to 1.0
     */
    public double getPosition() {

        return motor.getPosition() / maxEncoderPosition;

    }

    /**
     * zeros encoder of motor, should be called
     * in auto/teleop/robot init
     */
    public void setZero() {

        motor.resetEncoder();

    }

}