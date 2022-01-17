package wrappers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;

public class Gamepad {

	public enum Directions {

		N, NE, E, SE, S, SW, W, NW
		
	}

	Joystick joystick;

	int port;

	public TogglableButton buttons[];

	public Gamepad(int port) {

		this.port = port;

		joystick = new Joystick(port);

		int buttonCount = joystick.getButtonCount();

		buttons = new TogglableButton[buttonCount];

		for (int i = 0; i < buttons.length; i++) {

			buttons[i] = new TogglableButton();

		}

	}

	/**
	 * NEEDS TO BE IN TELEOP INIT AND TEST INIT
	 * This function makes there not need to be an initial
	 * start up crash after powering on the robot
	 * call for ALL xboxcontrollers or driving devices
	 */
	public void setupButtons() {

		int buttonCount = joystick.getButtonCount();

		buttons = new TogglableButton[buttonCount];

		for (int i = 0; i < buttons.length; i++) {

			buttons[i] = new TogglableButton();

		}

	}

	/**
	 * @param button Button to be toggled.
	 * @return Returns opposite state from when button was previously toggled.
	 */
	public boolean getToggle(int button) {

		return buttons[button].toggle(joystick.getRawButton(button + 1));

	}

	/**
	 * @param button Button being pressed down.
	 * @return Returns true on the button press.
	 */
	public boolean getButtonDown(int button) {

		return buttons[button].wasPressed(joystick.getRawButton(button + 1));

	}

	/**
	 * @param button Button being released.
	 * @return Returns true on the button's release.
	 */
	public boolean getButtonUp(int button) {

		return buttons[button].wasReleased(joystick.getRawButton(button + 1));

	}

	/**
	 * @param direction The direction the d-pad is being pressed.
	 * @return If direction being pressed is direction it returns true, otherwise
	 *         false.
	 */
	public boolean getPOV(Directions direction) {

		return (joystick.getPOV() == direction.ordinal() * 45);

	}

	public double getAxis(int axis) {

		return joystick.getRawAxis(axis);

	}

	public boolean getButton(int button) {

		return joystick.getRawButton(button + 1);

	}

	public void setRumble(boolean isLeftRumble, double value) {

		RumbleType type = (isLeftRumble) ? RumbleType.kLeftRumble : RumbleType.kRightRumble;

		joystick.setRumble(type, value);

	}

}