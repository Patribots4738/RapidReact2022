package utils;

public class Logger {

    double[] values;

	private int maxSize;
	private int currentIndex;
	private int numberOfValues;

	public Logger(int size) {

		this.values = new double[size];
		this.maxSize = size;
		this.numberOfValues = 0;

	}

	public void addValue(double newVal) {

		values[currentIndex] = newVal;

		this.increaseIndex();

    }
    
    public void printValues() {

        for (int i = 0; i < maxSize; i++) {

            System.out.println(values[i]);

        }

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