/**
 * An Operator representing the division function
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class DivOperator implements Operator
{
    /** A variable for the evaluation order */
    private int order = 2;
    
    /**
     * A constructor method for an DivOperator
     */
    public DivOperator(){
        this.order = order;
    }
    
    /**
     * A method for getting the evaluation order
     * 
     * @return an int representing the evaluation order
     */
    public int order(){ return this.order; }
}
