package wrappers;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;


public class Dashboard {

    private ShuffleboardTab tab;

    public Dashboard(String tabName) {

        tab = Shuffleboard.getTab(tabName);

    }

    public class slider{

        private NetworkTableEntry entry;

        public slider(String label, double minimum, double maximum, double incrment){

            entry = tab.add(label, 0).withWidget(BuiltInWidgets.kNumberSlider).withProperties(Map.of("min", minimum, "max", maximum, "Block increment", incrment)).getEntry();
        
        }

        public slider(String label){

            entry = tab.add(label, 0).withWidget(BuiltInWidgets.kNumberSlider).getEntry();
        
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

            entry = tab.add(label, 0).withWidget(BuiltInWidgets.kTextView).getEntry();

        }

        public String getValue(){

            return entry.getString("No Value Found");

        }

        public void setValue(String value){

            entry.setValue(value);

        }

    }
*/
    public class toggleButton{

        private NetworkTableEntry entry;

        public toggleButton(String label){

            entry = tab.add(label, false).withWidget(BuiltInWidgets.kToggleButton).getEntry();

        }

        public boolean getValue(){

            return entry.getBoolean(false);

        }

        public void setValue(Boolean value){

            entry.setBoolean(value);

        }
    }

    public class boxChooser{

        private SendableChooser<Integer> chooser;

        public boxChooser(String label, String ... options){

            chooser = new SendableChooser<>();

            chooser.setDefaultOption(options[0], 0);

            for(int i = 1; i < options.length; i++){

                chooser.addOption(options[i], i);

            }

        // chooser.setDefaultOption(options[0], 0);

            tab.add(label, chooser).withWidget(BuiltInWidgets.kComboBoxChooser);

        }

        public int getValue(){

            return chooser.getSelected();

        }
    }

    public class graph{

        private NetworkTableEntry entry;

        public graph(String title){

            double[] x = {};
            entry = tab.add(title, x).withWidget(BuiltInWidgets.kGraph).getEntry();

        }

        public graph(String title, double visibleTime){

            double[] x = {};
            entry = tab.add(title, x).withWidget(BuiltInWidgets.kGraph).withProperties(Map.of("Visible time", visibleTime)).getEntry();

        }

        public void addData(double ... data){

            entry.setDoubleArray(data);

        }

    }

    public class boolBox{
        
        private NetworkTableEntry entry;

        public boolBox(String label){

            entry = tab.add(label, false).withWidget(BuiltInWidgets.kBooleanBox).getEntry();

        }

        public void setValue(Boolean value){

            entry.setBoolean(value);

        }

    }

    public class textView{

        private NetworkTableEntry entry;

        public textView(String label){

            entry = tab.add(label, "").withWidget(BuiltInWidgets.kTextView).getEntry();

        }

        public void setValue(String value){

            entry.setString(value);

        }
    }

}
