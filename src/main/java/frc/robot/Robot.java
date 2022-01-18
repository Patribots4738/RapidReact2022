package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import java.util.ArrayList;

import autonomous.*;
import autonomous.Command.CommandType;
import hardware.*;
import interfaces.*;
import networking.*;
import utils.*;
import wrappers.*;

public class Robot extends TimedRobot {

	int i = 0;
	logMotors log;
	PIDMotor motor;

	@Override
	public void robotInit() {}

	@Override
	public void robotPeriodic() {}

	@Override
	public void autonomousInit() {} 

	@Override
	public void autonomousPeriodic() {}

	// NO TOUCH
	@Override 
	public void disabledInit() {}
	
	// VERY EXTRA NO TOUCH
	@Override
	public void disabledPeriodic() {}
	
	@Override
	public void teleopInit() {

		motor = new SparkMax(2, true);

		log = new logMotors(motor);

	}

	@Override
	public void teleopPeriodic() {

		log.writeLogs();
		System.out.println("Writing...");

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}