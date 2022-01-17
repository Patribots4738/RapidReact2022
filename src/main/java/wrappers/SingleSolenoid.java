package wrappers;

import edu.wpi.first.wpilibj.Solenoid;

public class SingleSolenoid{

	Solenoid solenoid;

	// current state of the solenoid, true for on/open, false for off/closed
	private boolean state = false;

	public SingleSolenoid(int port) {

		solenoid = new Solenoid(port);

	}

	// sets the solenoid to the state provided as a parameter
	public void set(boolean state) {

		this.state = state;

		solenoid.set(state);

	}
	
	// sets the solenoid to the opposite of its current state
	public void toggle() {

		state = !state;

		solenoid.set(state);

	}
	
	// returns the current state of the solenoid
	public boolean getState() {

		return state;

	}

}