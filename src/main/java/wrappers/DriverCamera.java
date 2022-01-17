package wrappers;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.cameraserver.CameraServer;

public class DriverCamera {

	UsbCamera cam;

	int camNum;

	boolean constructed = false;

	// usb port the camera is plugged into, should be 0 or 1
	public DriverCamera(int camNum) {

		this.camNum = camNum;

		try {

			cam = CameraServer.getInstance().startAutomaticCapture(camNum);
			cam.setResolution(240, 160);
			cam.setFPS(30);
			cam.setExposureManual(50);

			constructed = true;

		} catch (Exception e) {

			System.out.println("Camera Not Found");

		}

	}

	public void retryConnection() {

		try {

			cam.setExposureManual(51);

		} catch(Exception e) {

			constructed = false;

		}

		if(constructed) {

			return;

		}

		try {

			cam = CameraServer.getInstance().startAutomaticCapture(camNum);
			cam.setResolution(240, 160);
			cam.setFPS(30);
			cam.setExposureManual(50);

			constructed = true;

		} catch (Exception exception) {

			System.out.println("Camera Not Found");

		}

	}

	public boolean getConstructed() {

		return constructed;

	}

}