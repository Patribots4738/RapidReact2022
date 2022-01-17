package frc.robot;

import edu.wpi.first.wpilibj.RobotBase;

// this class is not to be messed with, leave it be

public final class Main {

	private Main() {}

	public static void main(String... args) {

		RobotBase.startRobot(Robot::new);

	}

}