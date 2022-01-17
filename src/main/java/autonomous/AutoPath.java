package autonomous;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import autonomous.Command.CommandType;

public class AutoPath {

    private File file;

    private ArrayList<Command> commandQueue;

    private Scanner reader;

    private String raw;

    // the base path for the json files will be home/lvuser/deploy/autopaths
    // then add /filename.json to the end
    public AutoPath(String filePath) {
        
        // creates a blank raw string for later safety
        raw = "";

        // attempts to find the file and create a scanner to read it,
        try {

            file = new File(filePath);

            reader = new Scanner(file); 

            while(reader.hasNextLine()) {

                raw += reader.nextLine();

            }

        } catch (Exception e) {

            System.out.print("File not found: " + filePath);

        }
        
        commandQueue = this.getCommands();

    }

    // this is magic regex stuff for reading the json files, it works, don't worry about it
    private double[][] getDoubleArray() {

        // this next line is black magic, don't touch
        Pattern pattern = Pattern.compile("\\[[^\\[\\]]*?\\]");
        Matcher matches = pattern.matcher(raw);

        int matchCount = 0;
        
        while (matches.find()) {

            matchCount++;

        }

        matches.reset();

        double[][] commandValArray = new double[matchCount][];
        
        int index = 0;

        while (matches.find()) {

            String group = matches.group();
            String[] splitCommands = group.substring(1, group.length() - 1).split(",");

            double[] commandValues = new double[splitCommands.length];

            for (int i = 0; i < splitCommands.length; i++) {

                commandValues[i] = Double.parseDouble(splitCommands[i]);

            }
/*
            double[] commandValues = new double[3];
            commandValues[0] = Double.parseDouble(splitCommands[0]);
            commandValues[1] = Double.parseDouble(splitCommands[1]);
            commandValues[2] = Double.parseDouble(splitCommands[2]);
*/
            commandValArray[index] = commandValues;

            index++;

        }

        return commandValArray;

    }

    // this filters out the commands from the double array granted by parsing the files
    private ArrayList<Command> getCommands() {

        double[][] commandVals = this.getDoubleArray();

        ArrayList<Command> commandQueue = new ArrayList<Command>();

        for(int i = 0; i < commandVals.length; i++) {

            //Command.CommandType type = (commandVals[i][0] == 0) ? CommandType.ROTATE : CommandType.MOVE;

            Command.CommandType type;

            switch ((int)(commandVals[i][0])) {

                case 0:
                    type = CommandType.ROTATE;
                    commandQueue.add(new Command(type, commandVals[i][1], commandVals[i][2]));
                    break;
                case 1:
                    type = CommandType.MOVE;
                    commandQueue.add(new Command(type, commandVals[i][1], commandVals[i][2]));
                    break;
                case 2: 
                    type = CommandType.SPLINE;
                    commandQueue.add(new Command(type, commandVals[i][1], commandVals[i][2], commandVals[i][3]));
                    break;
                
            }

        }

        return commandQueue;

    }

    public ArrayList<Command> getCommandQueue() {

        return commandQueue;

    }

    // this finds and returns the filepaths of all the paths currently stored in the robot code
    public static String[] getAutoPaths() {

        ArrayList<String> temp = new ArrayList<String>();

        try {

            Files.list(Paths.get("/home/lvuser/deploy/autopaths/")).forEach(path -> {

                if (path.toString().contains("json")) {

                    temp.add(path.toString());

                }

            });

        } catch (Exception e) {

            System.err.print("Files not found!");

        }

        return temp.toArray(new String[0]);

    }


}