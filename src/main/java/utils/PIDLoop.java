package utils;

public class PIDLoop {

	/*
	README!
	P must be greater than or equal to 0
	FF must be greather than or equal to 1
	*/

	// these are the constants for the PID controller
	private double P;
	private double I;
	private double D;

	/* 
	this is the feedForward constant, its used to account
	for stuff like weight or any kind of constant resistence
	it's effectively a speedMultiplier
	to find it, divide your desired command by what the thing acheives with no PID control
	*/
	private double FF;

	// these are the various types of error used for the calculation
	private double PError;
	private double IError;
	private double DError;

	// this is the current difference between the desired command and the current state
	private double error;

	/*
	this keeps a log of the previous errors back for a period of time,
	it's used for finding the integral and devivative
	*/
	private double[] errorLog;
	private int dLog;

	// this is the size of the errorlog, it determines how long the memory of the controller is for the I constant
	private int Izone;
	public int Dzone;

	public PIDLoop(double P, double I, double D, double FF, int Izone, int Dzone) {

		this.P = P;
		this.I = I;
		this.D = D;
		this.FF = FF;

		PError = 0;
		IError = 0;
		DError = 0;

		error = 0;

		this.Izone = Izone;
		this.Dzone = Dzone;

		errorLog = new double[Izone];
		dLog = Dzone;

	}

	public PIDLoop(double P, double I, double D, double FF, int Izone) {

		this.P = P;
		this.I = I;
		this.D = D;
		this.FF = FF;

		PError = 0;
		IError = 0;
		DError = 0;

		error = 0;

		this.Izone = Izone;

		errorLog = new double[Izone];
		dLog = 1;

	}

	public PIDLoop(double P, double I, double D) {

		this.P = P;
		this.I = I;
		this.D = D;
		this.FF = 1;

		PError = 0;
		IError = 0;
		DError = 0;

		error = 0;
		errorLog = new double[100];
  
	}

	public void setP(double P) {

		this.P = P;

	}

	public double getP() {

		return this.P;

	}

	public void setI(double I) {

		this.I = I;

	}
	
	public double getI() {

		return this.I;

	}

	public void setD(double D) {

		this.D = D;
		
	}

	public double getD() {

		return this.D;

	}

	public void setIzone(int Izone) {

		this.Izone = Izone;

	}

	public double getIzone() {

		return this.Izone;

	}

	public void setFF(double FF) {

		this.FF = FF;

	}

	public double getFF() {

		return this.FF;
		
	}

	public double getCommand(double desiredCommand, double currentState) {
		
		error = desiredCommand - currentState;
		
		double errorSum = 0;

		for (int i = errorLog.length - 1; i >= 0; i--) {

			if (i == 0) {

				errorLog[0] = error;

			} else {

				errorLog[i] = errorLog[i - 1];
			   
			}

		}

		for (int i = 0; i < errorLog.length; i++) {

			errorSum += errorLog[i];

		}

		PError = error;

		IError = errorSum * Constants.LOOP_TIME;

		DError = (errorLog[0] - errorLog[dLog]) / Constants.LOOP_TIME;

		double correctedCommand = (FF * desiredCommand) + (P * PError) + (I * IError) + (D * DError);

		return correctedCommand;

	}

}