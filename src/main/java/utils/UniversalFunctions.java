package utils;

import edu.wpi.first.wpilibj.DriverStation;

public class UniversalFunctions {

    /**
     * @return true for red alliance, false for blue alliance
     */
    public static boolean getAlliance() {

        DriverStation driverStation = DriverStation.getInstance();

        if (driverStation.getAlliance() == DriverStation.Alliance.Red) {

            return true;

        } else {

            return false;

        }

    }


}