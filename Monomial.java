/**
 * A class for a single Monomial
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
public class Monomial
{
    /** A variable for the Monomial's coefficient */
    public double coefficient;
    /** A variable for the Monomial's exponent */
    public int exponent;
    
    /** 
     * A constructor method for a Monomial
     * 
     * @param coefficient a double for the coefficient of the Monomial
     * @param exponent an int for the exponent of the Monomial
     */
    public Monomial(double coefficient, int exponent){
        this.coefficient = coefficient;
        this.exponent = exponent;
    }
    
    /**
     * A method to get the String representation of the Monomial
     * 
     * @return the String representation of the Monomial
     */
    public String toString(){
        if(this.exponent == 0 && this.coefficient == 1.0) return "1";
        else if(this.exponent == 0 && this.coefficient != 1.0) return Double.toString(this.coefficient);
        else if(this.exponent == 1){
            return Double.toString(this.coefficient) + "x";
        }
        else{
            return Double.toString(this.coefficient) + "x^" + Integer.toString(this.exponent);
        }
    }
    
    /**
     * A method to get the result of adding one Monomial to another
     * 
     * @param m a Monomial to be added to another
     * @return the resulting Monomial from adding the two together
     */
    public Monomial add(Monomial m){
        return new Monomial(this.coefficient + m.coefficient, this.exponent);
    }
    
    /**
     * A method to get the result of subtracting one Monomial from another
     * 
     * @param m a Monomial to be subtracted from another
     * @return the resulting Monomial from subtracting the given one from the other
     */
    public Monomial subtract(Monomial m){
        return new Monomial(this.coefficient - m.coefficient, this.exponent);
    }
    
    /**
     * A method to get the result of multiplying one Monomial with another
     * 
     * @param m a Monomial to be multiplyied with another
     * @return the resulting Monomial from multiplying the two
     */
    public Monomial multiply(Monomial m){
        return new Monomial(this.coefficient * m.coefficient, this.exponent + m.exponent);
    }
    
    /**
     * A method to get the result of dividing one Monomial from another
     * 
     * @param m a monomial to be divided from the other
     * @return the resulting Monomial from dividing the given one from the other
     */
    public Monomial divide(Monomial m){
        if(m.coefficient == 0) return null;
        return new Monomial(this.coefficient / m.coefficient, this.exponent - m.exponent);
    }
}