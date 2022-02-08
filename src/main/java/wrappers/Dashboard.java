package wrappers;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


public class Dashboard {

    public static class slider{

        private NetworkTableEntry entry;

        public slider(String label, double minimum, double maximum){

            entry = Shuffleboard.getTab("Data").add(label, 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", minimum, "max", maximum)).getEntry();
        
        }

        public slider(String label){

            entry = Shuffleboard.getTab("Data").add(label, 0).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
        
        }

        public double getValue(){

            return entry.getDouble(0);

        }

        public void setValue(double value){

            entry.setDouble(value);

        }

    }
/*
    public static class text{

        private NetworkTableEntry entry;

        public text(String label){

            entry = Shuffleboard.getTab("Data").add(label, 0).withWidget(BuiltInWidgets.kTextView).getEntry();

        }

        public String getValue(){

            return entry.getString("No Value Found");

        }

        public void setValue(String value){

            entry.setValue(value);

        }

    }
*/
    public static class toggleButton{

        private NetworkTableEntry entry;

        public toggleButton(String label){

            entry = Shuffleboard.getTab("Data").add(label, false).withWidget(BuiltInWidgets.kToggleButton).getEntry();

        }

        public boolean getValue(){

            return entry.getBoolean(false);

        }

        public void setValue(Boolean value){

            entry.setBoolean(value);

        }
    }

    public static class boxChooser{

    private SendableChooser<Integer> chooser;

    public boxChooser(String label, String ... options){

        chooser = new SendableChooser<>();

        chooser.setDefaultOption(options[0], 0);

        for(int i = 1; i < options.length; i++){

            chooser.addOption(options[i], i);

        }

       // chooser.setDefaultOption(options[0], 0);

        Shuffleboard.getTab("Data").add(label, chooser).withWidget(BuiltInWidgets.kComboBoxChooser);

    }

    public int getValue(){

        return chooser.getSelected();

    }
}

}
