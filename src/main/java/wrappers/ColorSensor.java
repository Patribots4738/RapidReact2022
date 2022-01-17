package wrappers;

import edu.wpi.first.wpilibj.I2C;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.util.Color;
import utils.*;

import com.revrobotics.ColorSensorV3.RawColor;

public class ColorSensor{

	I2C.Port port;

	ColorSensorV3 colorSensor;

	public ColorSensor () {

		port = I2C.Port.kOnboard;

		colorSensor = new ColorSensorV3(port);

	}

	public Color getColor() {
		
		return colorSensor.getColor();
		
	}

	public ColorSensorV3.RawColor getRawColor(){

		return colorSensor.getRawColor();

	}

	public double[] getRGBIR(){

		RawColor color = colorSensor.getRawColor();

		double max = Math.max(color.ir, Math.max(Math.max(color.red, color.blue), color.green));

		double r = ((color.red/max) * 255.0);
		double g = ((color.green/max) * 255.0);
		double b = ((color.blue/max) * 255.0);
		double ir =((color.ir/max) * 255.0);

		double[] rgbIR = {r, g, b, ir};

		return rgbIR;

	}

	public String determineColor() {

		double[] rgbIR = getRGBIR();

		for (int i = 0; i < Constants.COLORS.length; i++) {

			String checkColor = Constants.COLORS[i];

			if (isColor(rgbIR, checkColor)) {

				return checkColor;

			}

		}

		return "N/A";

	}

	public boolean isColor (double[] rgb, String colorName) {

		Constants.Color testColor;

		switch (colorName) {

			case "Yellow":
				testColor = Constants.Color.Yellow;
			break;

			case "Red":
				testColor = Constants.Color.Red;
			break;
			
			case "Green":
				testColor = Constants.Color.Green;
			break;

			case "Blue":
				testColor = Constants.Color.Blue;
			break;

			default:
				testColor = null;
			break;

		}

		if(rgb[0] > testColor.RMIN && rgb[0] < testColor.RMAX) {
			
			if(rgb[1] > testColor.GMIN && rgb[1] < testColor.GMAX) {
				
				if(rgb[2] > testColor.BMIN && rgb[2] < testColor.BMAX) {
					
					return true;

				}

			}

		}

		return false;

	} 

}