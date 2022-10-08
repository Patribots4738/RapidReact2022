package utils;

import interfaces.*;
import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Date;
import java.text.SimpleDateFormat;

public class logMotors {

    public String filename;
    public PIDMotor motors[];
    public FileWriter filedesc;

    /**
     * @param m motor object to log
     */
    public logMotors( PIDMotor... m) {

        filename = "/tmp/motor.log";
        this.motors = m;

        try {

            File fd = new File(this.filename);

            if (fd.delete()) {

                System.out.println("Delete file:" + fd.getName());

            } else {

                System.out.println("File does not exist:" + fd.getName() + " - no need to delete");

            }

            fd.createNewFile();
            this.filedesc = new FileWriter(this.filename);

            String buf="timestamp,";
            int i = 0;

            while (i < this.motors.length) {
                
                buf += String.format("%d position,%d speed", i, i);
                i++;
            
            }

            buf += "\n";
            System.out.println(buf);
            this.filedesc.write(buf);

        } catch(IOException e) {

            System.out.println("An error occured");
            e.printStackTrace();

        }

    }

    public void writeLogs () {

        String buf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ss.SSS").format(new Date());
        int i = 0;

        while (i < this.motors.length) {
            buf += String.format(",%s,%s", this.motors[i].getPosition(), this.motors[i].getSpeed());
            i++;
        }

        buf += "\n";

        try {

            this.filedesc.write(buf);
            this.filedesc.flush();

        } catch(IOException e) {

            System.out.println("An error occured");
            e.printStackTrace();

        }

    }

}
