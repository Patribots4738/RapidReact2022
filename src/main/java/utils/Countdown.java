package utils;

import wrappers.*;

public class Countdown {

	double startTime;
	double endTime;
	double runTime;
	
	public Countdown(double runTime) {

		startTime = Timer.getTime();
		this.runTime = runTime;
		endTime = startTime + runTime;

	}

	public boolean isRunning() {

		return Timer.getTime() < endTime;

	}

	public void reset() {

		startTime = Timer.getTime();
		endTime = startTime + runTime;

	}

	/**
	 * @return time remaining in seconds
	 */
	public double timeRemaining() {

		return (endTime - Timer.getTime());

	}

}