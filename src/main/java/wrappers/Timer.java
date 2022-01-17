package wrappers;

public class Timer {

	static edu.wpi.first.wpilibj.Timer timer;

	public static void init() {

		timer = new edu.wpi.first.wpilibj.Timer();

		timer.start();

	}

	// returns current time since robot code began, in seconds;
	public static double getTime() {

		return timer.get();

	}

}