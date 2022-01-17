package wrappers;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import utils.*;

public class LEDStrip {

	AddressableLED strip;
	AddressableLEDBuffer lights;

	int length;

	// this class is for controlling an addresable PWM LEDStrips
	// length is in number of individual LEDs
	public LEDStrip(int PWMPort, int length) {

		this.length = length;

		strip = new AddressableLED(PWMPort);
		strip.setLength(length);

		lights = new AddressableLEDBuffer(length);

		strip.setData(lights);
		strip.start();

	}

	// sets an individual LED on the strip
	public void setLight(int light, int r, int g, int b) {

		// cap the values to valid RGB inputs and the light to be set to be within the indexes of the strip
		light = (int)Calc.cap(light, 0, length - 1);
		r = (int)Calc.cap(r, 0, 255);
		g = (int)Calc.cap(g, 0, 255);
		b = (int)Calc.cap(b, 0, 255);

		lights.setRGB(light, r, g, b);

		strip.setData(lights);

	}

	// sets the entire strip to the color input
	public void setStrip(int r, int g, int b) {

		// i in this case is the light on the strip
		for(int i = 0; i < length; i++) {

			setLight(i, r, g, b);

		}

		strip.setData(lights);

	}

}