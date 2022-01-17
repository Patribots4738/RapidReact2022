package wrappers;

import interfaces.*;
import edu.wpi.first.wpilibj.Spark;

public class PWMSpark implements Motor{

	edu.wpi.first.wpilibj.Spark spark;

	public PWMSpark(int port){

		spark = new Spark(port);

	}

	public void setSpeed(double speed){

		spark.set(speed);

	}

}