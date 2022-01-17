package wrappers;

public class Limitswitch {

	TogglableButton switchButton;

	edu.wpi.first.wpilibj.DigitalInput DIOPort;    

	public Limitswitch(int port) {

		switchButton = new TogglableButton();

		DIOPort = new edu.wpi.first.wpilibj.DigitalInput(port);

	}

	// get the raw value from the digital input/output port
	public boolean getState() {

		return DIOPort.get();

	}

	// these next three functions just access the base toggleableButton
	// features with the state of the swtich as the boolean
	public boolean getToggle() {

		return switchButton.toggle(getState());

	}

	public boolean getPressed() {

		return switchButton.wasPressed(getState());

	}

	public boolean getReleased() {

		return switchButton.wasReleased(getState());

	}

} 