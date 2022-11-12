package interfaces;

public interface Motor {

	/**
	 * @param speed decimal percent of motor output from -1 to 1
	 */
	public void setSpeed(double speed);

	/**
	 * Toggles the brake mode according to isBrake
	 * @param isBrake is the if the brake should be true or false-- true = brake
	 */
    public void setBrakeMode(boolean isBrake);

}