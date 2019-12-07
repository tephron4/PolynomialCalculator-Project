/**
 * An Operator representing the multiplication function
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class MulOperator implements Operator
{
    /** A variable for the evaluation order */
    private int order = 2;
    
    /**
     * A constructor method for an MulOperator
     */
    public MulOperator(){
        this.order = order;
    }
    
    /**
     * A method for getting the evaluation order
     * 
     * @return an int representing the evaluation order
     */
    public int order(){ return this.order; }
}