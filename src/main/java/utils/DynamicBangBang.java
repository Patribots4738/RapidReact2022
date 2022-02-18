package utils;

public class DynamicBangBang
{
    private double maxIncrement;
    private double acceptableError;
    private double newSpeed = 0;
    private final double steepness = 0.2;
    //private int slowDown = 0 ;
    
    public DynamicBangBang(double maxIncrement, double acceptableError){
        this.maxIncrement = maxIncrement;
        this.acceptableError = acceptableError;
    }
    
    public double getCommand(double desired, double actual)
    {
        /*if((actual < 0 - acceptableError && actual > 0 + acceptableError)
        &&(Math.signum(-desired) == Math.signum(actual + acceptableError) 
        ||(Math.signum(-desired) == Math.signum(actual - acceptableError)))
        ) {
            System.out.println("IMPACTING: " + slowDown);
            if(actual > 0 - acceptableError && actual < 0 + acceptableError) {
                slowDown++;   
            }
            if (slowDown < 1000) { 
                return(getCommand(0, actual));
            }  
            slowDown = 0;
        }
        System.out.println(((actual < 0 - acceptableError && actual > 0 + acceptableError)
        &&(Math.signum(-desired) == Math.signum(actual + acceptableError) 
        ||(Math.signum(-desired) == Math.signum(actual - acceptableError))))
        ? "True" : "False");*/
        return getCommand(desired - actual);
    }
    
    private double getCommand(double difference) 
    { 
        if(Math.abs(difference) < acceptableError){
            return newSpeed;
        }
        int sign = (int)Math.signum(difference);
        double increment = steepness*(Math.pow(Math.abs(difference), 2));
        increment = sign * Math.min(maxIncrement, increment);
        newSpeed += increment;
        
        if(Math.abs(newSpeed) >= 1){
            newSpeed = Math.signum(newSpeed);
        }
        
        //System.out.println(String.format("New Speed: %.4f; Increment: %.4f", newSpeed, increment));
        return newSpeed;
    }
}
