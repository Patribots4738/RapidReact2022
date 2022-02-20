package utils;

public class DynamicBangBang
{
    private double maxIncrement;
    private double minIncrement;
    private double acceptableError;
    private double newSpeed = 0;
    private double steepness;
    private final double upConstant = 0.15;
    private final double downConstant = 0.1;
    // private int slowDown = 0 ;
    
    public DynamicBangBang(double maxIncrement, double minIncrement, double acceptableError){
        this.maxIncrement = maxIncrement;
        this.minIncrement = minIncrement;
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
        if (Math.signum(desired) != Math.signum(actual) && (actual > acceptableError || actual < -acceptableError)) {

            // System.out.println("Switch Sign");
            desired = 0;

        }
        if(Math.abs(desired) < acceptableError){
            //System.out.println("Slow Down");
            steepness = downConstant;
        }else{
            //System.out.println("Speed Up");
            steepness = upConstant;
        }
        return getCommand(desired - actual);
    }
    
    private double getCommand(double difference) 
    { 
        
        if(Math.abs(difference) < acceptableError){
            return newSpeed;
        }
        int sign = (int)Math.signum(difference);
        double increment = steepness*(Math.pow(Math.abs(difference), 2));
        increment = Math.min(maxIncrement, increment);
        increment = Math.max(minIncrement, increment);
        
        newSpeed += sign * increment;
        
        if(Math.abs(newSpeed) >= 1){
            newSpeed = Math.signum(newSpeed);
        }
        
        // System.out.println(String.format("New Speed: %.4f; Increment: %.4f", newSpeed, increment));
        return newSpeed;
    }
}
