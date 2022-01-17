package wrappers;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class DoubleSolenoid {

	edu.wpi.first.wpilibj.DoubleSolenoid DoubleSolenoid;

	// represents the state of the double solenoid with -1 representing the reverse channel being active,
	// 1 representing the forward channel being active, and 0 representing both channels being off
	private int state;

	public DoubleSolenoid(int port1, int port2) {

		DoubleSolenoid = new edu.wpi.first.wpilibj.DoubleSolenoid(port1, port2);

		state = 0;

		deactivate();

	}

	// activates a channel based on the input
	public void activateChannel(boolean isForwardChannel) {

		if (isForwardChannel) {

			state = 1;

			DoubleSolenoid.set(Value.kForward);

		} else {

			state = -1;

			DoubleSolenoid.set(Value.kReverse);

		}

	}

	// deactivates both channels
	public void deactivate() {

		state = 0;

		DoubleSolenoid.set(Value.kOff);

	}

	// returns the current active channel
	public int getState() {

		return state;

	}

	// toggles which channel is active, then returns the current active channel.
	// if both channels are off, then 0 is returned and no channels are activated
	public int toggle() {

		state = 0 - state;

		if(state == 1){

			DoubleSolenoid.set(Value.kForward);

		} 

		if(state == -1) {

			DoubleSolenoid.set(Value.kReverse);

		}

		return state;

	}

}