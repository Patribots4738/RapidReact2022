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
	public void teleopInit() {}

	@Override
	public void teleopPeriodic() {

		i++;

		System.out.println("number" + i);

	}

	@Override
	public void testInit() {}
	
	@Override
	public void testPeriodic() {}

}