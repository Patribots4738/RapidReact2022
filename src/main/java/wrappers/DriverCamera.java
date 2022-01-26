package wrappers;

import edu.wpi.first.cscore.UsbCamera;
import edu.wpi.first.cscore.VideoMode.PixelFormat;
import edu.wpi.first.cameraserver.CameraServer;

import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.cscore.MjpegServer;

public class DriverCamera {

	UsbCamera cam;

	int camNum;

	boolean constructed = false;

	// usb port the camera is plugged into, should be 0 or 1
	public DriverCamera(int camNum) {

		this.camNum = camNum;

		try {

			cam = CameraServer.startAutomaticCapture();
			cam.setResolution(100, 100);
			cam.setFPS(30);
			cam.setExposureManual(5);
			cam.setPixelFormat(PixelFormat.kMJPEG);

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

			cam = CameraServer.startAutomaticCapture(camNum);
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