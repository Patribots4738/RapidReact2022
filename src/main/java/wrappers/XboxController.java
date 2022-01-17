package wrappers;

public class XboxController extends Gamepad {

	public enum Buttons {

		A, B, X, Y, L, R, Select, Start, LJ, RJ

	}

	public enum Axes {

		LeftX, LeftY, LeftTrigger, RightTrigger, RightX, RightY;

	}

	public XboxController(int port) {

		super(port);

	}
	/**
	 * 
	 * @param axis the enum axis that 
	 * @returns a number between 0 and 1.0 that describes a joystick's position
	 */
	public double getAxis(Axes axis) {

		return this.getAxis(axis.ordinal());

	}
	/**
	 * Gets whether the button is being pressed or not on the controller
	 * @param button the button enum that the state of which is trying to be determined
	 * @returns whether the button is being pressed (true) or not being pressed (false)
	 */
	public boolean getButton(Buttons button) {

		return this.getButton(button.ordinal());

	}

	/**
	 * 
	 * @param button
	 * @return
	 */
	public boolean getToggle(Buttons button) {

		return this.getToggle(button.ordinal());

	}
	/**
	 * 
	 * @param button
	 * @return
	 */
	public boolean getButtonDown(Buttons button) {

		return this.getButtonDown(button.ordinal());

	}

	/**
	 * 
	 * @param button
	 * @return
	 */
	public boolean getButtonUp(Buttons button) {

		return this.getButtonUp(button.ordinal());

	}
	/**
	 * 
	 * @param direction
	 * @return
	 */
	public boolean getDPad(Directions direction) {

		return this.getPOV(direction);

	}
	/**
	 * 
	 * @param isLeftRumble
	 * @param rumble
	 */
	public void setRumble(boolean isLeftRumble, double rumble) {

		super.setRumble(isLeftRumble, rumble);

	}

}