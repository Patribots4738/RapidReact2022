package wrappers;

import edu.wpi.first.wpilibj.ADIS16470_IMU;

public class IMU {

    ADIS16470_IMU imu;

    double prevCompX = 0.0;
    double prevCompY = 0.0;

    double currentCompX = 0.0;
    double currentCompY = 0.0;

    double rotCompX = 0.0;
    double rotCompY = 0.0;

    /**
     * Axis can be found on a diagram on the front
     * of the roborio
     * 
     * At the current state of this library
     */
    public IMU() {

        imu = new ADIS16470_IMU();
        this.reset();
    }

    /**
     * When the X Axis is pointing towards the ground
     * The acceleration will be equal to 1 g, because
     * there is always 1 earth gravity pulling towards
     * the center of the earth
     * @return acceleration on X axis in g's
     */
    public double getXAcceleration() {

        return imu.getAccelX();

    }

    /**
     * When the Y Axis is pointing towards the ground
     * The acceleration will be equal to 1 g, because
     * there is always 1 earth gravity pulling towards
     * the center of the earth
     * @return acceleration on Y axis in g's
     */
    public double getYAcceleration() {

        return imu.getAccelY();

    }

    /**
     * When the Z Axis is pointing towards the ground
     * The acceleration will be equal to 1 g, because
     * there is always 1 earth gravity pulling towards
     * the center of the earth
     * @return acceleration on Z axis in g's
     */
    public double getZAcceleration() {

        return imu.getAccelZ();

    }

    /**
     * -360 = 1 rotation backwards
     * +360 = 1 rotation forwards
     * -720 = 2 rotations backwards
     * +720 = 2 rotations forwards
     * @return rotations on the X axis in degrees
     */
    public double getXRotation() {

        currentCompX = imu.getYComplementaryAngle();
        
        double difference = currentCompX - prevCompX;

        if (difference >= 300) {
            
            rotCompX -= 360;

        } else if (difference <= -300) {
            
            rotCompX += 360;

        }

        prevCompX = currentCompX;

        rotCompX += difference;

        return rotCompX;

    }

    /**
     * -360 = 1 rotation backwards
     * +360 = 1 rotation forwards
     * -720 = 2 rotations backwards
     * +720 = 2 rotations forwards
     * @return rotations on the Y axis in degrees
     */
    public double getYRotation() {

        currentCompY = imu.getXComplementaryAngle();
        
        double difference = currentCompY - prevCompY;

        if (difference >= 300) {
            
            rotCompY -= 360;

        } else if (difference <= -300) {
            
            rotCompY += 360;

        }

        prevCompY = currentCompY;

        rotCompY += difference;

        return rotCompY;

    }

    /**
     * -360 = 1 rotation backwards
     * +360 = 1 rotation forwards
     * -720 = 2 rotations backwards
     * +720 = 2 rotations forwards
     * @return rotations on the Z axis in degrees
     */
    public double getZRotation() {

       return imu.getAngle();
    }

    public void reset() {

        imu.reset();

    }

    public void calibrate(){

        imu.calibrate();

    }
}