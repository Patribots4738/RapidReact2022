package wrappers;

import edu.wpi.first.wpilibj.PneumaticsModuleType;

public class Compressor {

	edu.wpi.first.wpilibj.Compressor comp;

	public Compressor() {

		comp = new edu.wpi.first.wpilibj.Compressor(PneumaticsModuleType.CTREPCM);

		comp.enableDigital();

	}

	public void setState(boolean on) {

		if (on) {

			comp.enableDigital();

		} else {

			comp.disable();

		}

	}

	public boolean getState() {

		return comp.enabled();

	}

}