package wrappers;

public class UltraSonic{

    edu.wpi.first.wpilibj.AnalogInput PWMInput;

    public UltraSonic(int port) {

        PWMInput = new edu.wpi.first.wpilibj.AnalogInput(port);

    }

    public double getRaw() {

        return PWMInput.getAverageVoltage();

    }

}
