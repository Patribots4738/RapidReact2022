package utils;

import wrappers.*;

public class TimeLoop {

	double loopTime;

	double startTime;

	int loopsCompleted;

	public TimeLoop(double loopTime) {

		this.loopTime = loopTime;

		loopsCompleted = 0;

		startTime = Timer.getTime();

	}

	// returns true whenever a loop of is completed, false otherwise
	public boolean loopCompleted() {

		if(((Timer.getTime() - startTime) % loopTime) == 0) {

			return true;

		}

		return false;

	}

	public int getLoops() {

		return (int)((Timer.getTime() - startTime) / loopTime);

	}

}