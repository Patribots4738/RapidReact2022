package autonomous;

import wrappers.*;
import utils.*;

import java.util.ArrayList;

public class AutoDrive {

	PIDMotorGroup leftMotors;
	PIDMotorGroup rightMotors;

	// details how close we can get to the specified end position of a command
	// before calling it complete and moving to the next one
	private double acceptableError = 0.5;

	// the positions the left and right motorgroups should
	// be at when the currently running command has been completed,
	// these values will change often during the operation of the class.
	// the order is left motor, right motor for index 0 and 1
	private double[] completePositions;

	// true if it's running commands, false if it's not
	private boolean running;

	private ArrayList<Command> commandQueue;

	public AutoDrive(PIDMotorGroup leftMotors, PIDMotorGroup rightMotors) {

		this.leftMotors = leftMotors;
		this.rightMotors = rightMotors;

		running = false;

		completePositions = new double[2];
		completePositions[0] = leftMotors.getPosition();
		completePositions[1] = rightMotors.getPosition();
		

		commandQueue = new ArrayList<Command>();

	}

	// resets the command queue and stops execution of currently running commands
	public void reset() {

		running = false;

		leftMotors.resetEncoder();
		rightMotors.resetEncoder();

		completePositions = new double[2];
		completePositions[0] = 0.0;
		completePositions[1] = 0.0;

		commandQueue = new ArrayList<Command>();

	}

	// sets the path for the robot to follow, should be called before executing any commands
	public void setPath(AutoPath path) {

		commandQueue = path.getCommandQueue();

	}

	// adds the commands of the path to the current ones
	public void addPath(AutoPath path) {

		for(int i = 0; i < path.getCommandQueue().size(); i++) {

			commandQueue.add(path.getCommandQueue().get(i));

		}

	}

	// add commands to the command queue, in order
	public void addCommands(Command... commands) {

		for(int i = 0; i < commands.length; i++) {

			commandQueue.add(commands[i]);

		}

	}

	// returns a boolean detailing if the command at the supplied index could be removed
	private boolean removeCommand(int index) {

		if(index < commandQueue.size()){

			commandQueue.remove(index);

			return true;

		} else {

			return false;

		}

	}

	public boolean queueIsEmpty() {

		return commandQueue.size() == 0;

	}

	public ArrayList<Command> getQueue() {

		return commandQueue;

	}

	public void jumpstart() {

		// set running to false to jumpstart the auto
		running = false;

	}

	public int getQueueLength() {

		return commandQueue.size();

	}

	// this command will be called once to start executing a command
	public void executeCommand(Command command) {

		Command.CommandType commandType = command.getType();

		double value = command.getValue();
		double speed = command.getSpeed();
		double chordLength = command.getChordLength();
		double arcHeight = command.getArcHeight();
		
		if (commandType == Command.CommandType.MOVE) {

			// set left motor speed
			completePositions[0] += value;
			
			// sets the right motor to rotate opposite of the left
			completePositions[1] -= value;

			leftMotors.setPosition(completePositions[0], -speed, speed);
			rightMotors.setPosition(completePositions[1], -speed, speed);

		} else if (commandType == Command.CommandType.ROTATE) {

			//set right motor speed
			completePositions[0] += value;

			completePositions[1] += value;

			leftMotors.setPosition(completePositions[0], -speed, speed);
			rightMotors.setPosition(completePositions[1], -speed, speed);

			// And now, "The last command type" - Ben 2022
		} else if (commandType == Command.CommandType.SPLINE) { 

			double radius = (chordLength * chordLength) / (8 * arcHeight) + (arcHeight / 2);

			double arcLength = 2 * radius * Math.asin(chordLength / (2 * radius));

			double outerScalar = 1 + Constants.ROBOT_WHEEL_SPACING / 2 / radius;
			double innerScalar = 1 - Constants.ROBOT_WHEEL_SPACING / 2 / radius;
			double leftSpeed = speed;
			double rightSpeed = speed;

			// decide which one based on sign of height
			if (arcHeight > 0) {
				
				completePositions[0] += Calc.inchesToDrive(arcLength * outerScalar);
				completePositions[1] -= Calc.inchesToDrive(arcLength * innerScalar);

				leftSpeed *= outerScalar;
				rightSpeed *= innerScalar;

			} else { 
				
				completePositions[0] += Calc.inchesToDrive(arcLength * innerScalar);
				completePositions[1] -= Calc.inchesToDrive(arcLength * outerScalar);

				leftSpeed *= innerScalar;
				rightSpeed *= outerScalar;
			}

			leftMotors.setPosition(completePositions[0], -leftSpeed, leftSpeed);
			rightMotors.setPosition(completePositions[1], -rightSpeed, rightSpeed);

		}

	}
 
	// this command will be called continously in robot
	public void executeQueue() {

		// checks if there aren't any commands to save cpu cycles
		if(queueIsEmpty()) {

			running = false;
			return;

		} else if(!running) {

			// starts executing commands if this is being called while commands aren't executing
			executeCommand(commandQueue.get(0));
			running = true;
			return;

		}

		double leftWheelPosition = leftMotors.getPosition();
		double rightWheelPosition = rightMotors.getPosition();
		
		double leftError = Math.abs(completePositions[0] - leftWheelPosition);
		double rightError = Math.abs(completePositions[1] - rightWheelPosition);

		if(leftError <= acceptableError && rightError <= acceptableError) {

			removeCommand(0);

			if(queueIsEmpty()) {
				
				return;

			}

			executeCommand(commandQueue.get(0));

			running = true;

		}

	}

} 