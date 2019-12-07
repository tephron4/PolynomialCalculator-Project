/**
 * A class representing a left parentheses "("
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class LeftParentheses implements Operator
{
    /** A variable for the evaluation order */
    private int order = 0;
    
    /**
     * A constructor method for an LeftParentheses
     */
    public LeftParentheses(){
        this.order = order;
    }
    
    /**
     * A method for getting the evaluation order
     * 
     * @return an int representing the evaluation order
     */
    public int order(){ return this.order; }
}