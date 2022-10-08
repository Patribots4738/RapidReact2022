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
    
    public DynamicBangBang(double maxIncrement, double minIncrement, double acceptableError){
        this.maxIncrement = maxIncrement;
        this.minIncrement = minIncrement;
        this.acceptableError = acceptableError;
    }
    
    public double getCommand(double desired, double actual)
    {
        if (Math.signum(desired) != Math.signum(actual) && (actual > acceptableError || actual < -acceptableError)) {

            desired = 0;

        }
        if (Math.abs(desired) < acceptableError) {
            
            steepness = downConstant;
        
        } else {
            
            steepness = upConstant;
        
        }
        return getCommand(desired - actual);
    }
    
    private double getCommand(double difference) 
    { 
        
        if (Math.abs(difference) < acceptableError) {
           
            return newSpeed;
        }

        int sign = (int)Math.signum(difference);
        double increment = steepness*(Math.pow(Math.abs(difference), 2));
        
        increment = Math.min(maxIncrement, increment);
        increment = Math.max(minIncrement, increment);
        newSpeed += sign * increment;
        
        if (Math.abs(newSpeed) >= 1) {
            
            newSpeed = Math.signum(newSpeed);

        }
        
        return newSpeed;
    }
}
