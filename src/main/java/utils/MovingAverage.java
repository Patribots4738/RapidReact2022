package utils;

// this class is used to take the average of a constantly changing set of values
public class MovingAverage {

	double[] values;

	private int maxSize;
	private int currentIndex;
	private int numberOfValues;

	public MovingAverage(int size) {

		this.values = new double[size];
		this.maxSize = size;
		this.numberOfValues = 0;

	}

	public void addValue(double newVal) {

		values[currentIndex] = newVal;

		this.increaseIndex();

	}

	public double getAverage() {

		double sum = 0;

		for (int i = 0; i < values.length; i++) {

			sum += values[i];

		}
	
		return sum / numberOfValues;
		
	}

	private void increaseIndex() {

		if (currentIndex + 1 >= maxSize) {

			currentIndex = 0;

		} else {

			currentIndex++;

		}

		if (numberOfValues < maxSize) {

			numberOfValues++;

		}

	}
	
}