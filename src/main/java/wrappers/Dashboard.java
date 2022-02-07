package wrappers;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;


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

    public static class text{

        private NetworkTableEntry entry;

        public text(String label){

            entry = Shuffleboard.getTab("Data").add(label, 0).withWidget(BuiltInWidgets.kTextView).getEntry();

        }

        public String getValue(){

            return entry.getString("no value found");

        }

        public void setValue(String value){

            entry.setString(value);

        }

    }

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
}
