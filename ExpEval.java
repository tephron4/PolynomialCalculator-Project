/**
 * A class for evaluating an expression
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class ExpEval
{
    /**
     * A method for evaluating two polynomials using a given Operator
     * 
     * @param a a Polynomial
     * @param b a Polynomial
     * @param o a Token representing an Operator
     * @return the Polynomial result of the evaluation
     */
    public Polynomial evaluate(Polynomial a, Polynomial b, Token o){
        if(o instanceof AddOperator) return a.add(b);
        else if(o instanceof SubOperator) return a.subtract(b);
        else if(o instanceof MulOperator) return a.multiply(b);
        else if(o instanceof DivOperator) return a.divide(b);
        else return null;
    }
}