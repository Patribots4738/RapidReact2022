package wrappers;

public class Compressor {

	edu.wpi.first.wpilibj.Compressor comp;

	public Compressor() {

		comp = new edu.wpi.first.wpilibj.Compressor();

		comp.setClosedLoopControl(true);

	}

	public void setState(boolean on) {

		comp.setClosedLoopControl(on);

	}

	public boolean getState() {

		return comp.getClosedLoopControl();

	}

}