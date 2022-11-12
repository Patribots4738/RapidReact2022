package utils;

import java.io.File;  // Import the File class
import java.io.FileWriter;   // Import the FileWriter class
import java.io.IOException;  // Import the IOException class to handle errors
import java.util.Date;
import java.text.SimpleDateFormat;

public class LogWriter {

    public String[] headers;
    public FileWriter filedesc;
    public String filePath = "/tmp/log.csv";

    public LogWriter(String filePath) {

        this.filePath = filePath;
        init();

    }

    public LogWriter() {

        init();

    }
    
    private void init() {

        try {

            File logFile = new File(filePath);

            if (logFile.delete()) {

                System.out.println("Delete file:" + logFile.getName());

            } else {

                System.out.println("File does not exist:" + logFile.getName() + " - no need to delete");

            }

            logFile.createNewFile();

            this.filedesc = new FileWriter(filePath);
        } catch(IOException e) {

            System.out.println("An error occured");
            e.printStackTrace();

        }

    }
    
    public void header(String ... headers) {

        this.headers = headers;

        try {

            String buf= "timestamp" ;

            for(String item: headers) {

                buf += " , " + item;

            }

            buf += "\n";

            this.filedesc.write(buf);

        } catch(IOException e) {

            System.out.println("An error occured");
            e.printStackTrace();

        }

    }
    
    /**
     * @param format format of prints. log will not be formated. Ex: "%.2f"
     * @param logItems numbers (double) to log
     */
    public void write(String format, double ... logItems) {

        String buf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS").format(new Date());

        String print = "";

        for(int i = 0; i < logItems.length; i++){

            buf += " , " + logItems[i];
            if(i < headers.length) {

                print += headers[i] + ": " + String.format(format,logItems[i]) + "   ";

            } else {

                print += String.format(format,logItems[i]) + "   ";

            }

        }

       // System.out.println(print);

        buf += "\n";

        try {

            this.filedesc.write(buf);
            this.filedesc.flush();

        }

        catch(IOException e) {

            System.out.println("An error occured");
            e.printStackTrace();

        }

    }
    
    public void writeLog(double ... logItems) {
        
        write("%.2f",logItems);
        
    }
}