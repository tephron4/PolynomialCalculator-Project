/**
 * A class representing a right parentheses ")"
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class RightParentheses implements Operator
{
    /** A variable for the evaluation order */
    private int order = 3;
    
    /**
     * A constructor method for an RightParentheses
     */
    public RightParentheses(){
        this.order = order;
    }
    
    /**
     * A method for getting the evaluation order
     * 
     * @return an int representing the evaluation order
     */
    public int order(){ return this.order; }
}
