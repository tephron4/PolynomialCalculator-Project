/**
 * An Operator representing the addition function
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class AddOperator implements Operator
{
    /** A variable for the evaluation order */
    private int order = 1;
    
    /**
     * A constructor method for an AddOperator
     */
    public AddOperator(){
        this.order = order;
    }
    
    /**
     * A method for getting the evaluation order
     * 
     * @return an int representing the evaluation order
     */
    public int order(){ return this.order; }
}