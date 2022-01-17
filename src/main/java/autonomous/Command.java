package autonomous;

import utils.Calc;

public class Command {

    public enum CommandType {

        MOVE, ROTATE, SPLINE;
        
    }

    private CommandType type;
    
    // Input for constructor is in inches for moving, decimal percent rotations if rotating
    // however, it is internally stored in rotations of the drive motor's output shaft
    private double value;

    // decimal percent of the max motor speed
    private double speed;

    private double chordLength;
    private double arcHeight;

    public Command(CommandType type, double value, double speed) {

        this.type = type;
        this.value = ((type == CommandType.ROTATE) ? Calc.robotRotationsToDrive(value) : Calc.inchesToDrive(value));
        this.speed = speed;
        chordLength = 0.0;
        arcHeight = 0.0;

    }

    // Command constructor for spline paths only
    public Command(CommandType type, double chordLength, double arcHeight, double speed) {

        this.type = type;
        this.chordLength = chordLength;
        this.arcHeight = arcHeight;
        this.speed = speed;
        value = 0.0;

    }

    public CommandType getType() {

        return type;

    }
    
    public double getValue() {

        return value;

    }

    public double getSpeed() {

        return speed;

    }

    public double getChordLength() {

        return chordLength;

    }

    public double getArcHeight() {

        return arcHeight;

    }

}