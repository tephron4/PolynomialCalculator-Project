/**
 * A class for a Polynomial
 *
 * @author (Tobias Ephron)
 * @version (1)
 */
import java.util.*;
public class Polynomial implements Token
{
    /** A variable to keep track of the different Monomials in the Polynomial */
    public TreeMap<Integer,Double> monos = new TreeMap<Integer,Double>(Collections.reverseOrder());
    
    /** An empty Polynomial constructor */
    public Polynomial(){
        
    }
    
    /**
     * A method to get the evaluation order (only needed because it is in Token)
     * 
     * @return an integer representing the order
     */
    public int order(){return 0;};
    
    /**
     * A method to add a Monomial to the Polynomial
     * 
     * @param coef a double for the coefficient of the Monomial
     * @param exp an int for the exponent of the Monomial
     */
    public void addMono(double coef, int exp){
        if(this.monos.containsKey(exp)){
            this.monos.replace(exp, this.monos.get(exp) + coef);
        }
        else{
            this.monos.put(exp,coef);
        }
    }
    
    /** 
     * A method to add a Monomial to the Polynomial
     * 
     * @param m the Monomial to be added
     */
    public void addMono(Monomial m){
        this.addMono(m.coefficient,m.exponent);
    }
    
    /**
     * A method to get the result of adding one Polynomial to another
     * 
     * @param p a Polynomial to be added to another
     * @return the resulting Polynomial from adding the two together
     */
    public Polynomial add(Polynomial p){
        Polynomial acc = new Polynomial();
        Set<Integer> keySet = this.monos.keySet();
        Iterator<Integer> itr = keySet.iterator();
        while(itr.hasNext()){
            int key = itr.next();
            double value = this.monos.get(key);
            acc.addMono(value,key);
        }
        Set<Integer> keySet2 = p.monos.keySet();
        Iterator<Integer> itr2 = keySet2.iterator();
        while(itr2.hasNext()){
            int key = itr2.next();
            double value = p.monos.get(key);
            acc.addMono(value,key);
        }
        acc.clean();
        return acc;
    }
    
    /**
     * A method to get the result of subtracting one Polynomial from another
     * 
     * @param p a Polynomial to be subtracted from another
     * @return the resulting Polynomial from subtracting the given one from the other
     */
    public Polynomial subtract(Polynomial p){
        Polynomial acc = new Polynomial();
        Set<Integer> keySet = p.monos.keySet();
        Iterator<Integer> itr = keySet.iterator();
        while(itr.hasNext()){
            int key = itr.next();
            double value = p.monos.get(key);
            Monomial m1 = new Monomial(value,key);
            if(!this.monos.containsKey(key)){
                acc.addMono(-1.0*value,key);
            }
            else{
                Monomial m2 = new Monomial(this.monos.get(key),key);
                acc.addMono(m2.subtract(m1));
            }
        }
        Set<Integer> keySet2 = this.monos.keySet();
        Iterator<Integer> itr2 = keySet2.iterator();
        while(itr2.hasNext()){
            int key = itr2.next();
            if(!acc.monos.containsKey(key)){
                acc.addMono(this.monos.get(key),key);
            }
        }
        acc.clean();
        return acc;
    }
    
    /**
     * A method to get the result of multiplying one Polynomial with another
     * 
     * @param p a Polynomial to be multiplyied with another
     * @return the resulting Polynomial from multiplying the two
     */
    public Polynomial multiply(Polynomial p){
        Polynomial acc = new Polynomial();
        Set<Integer> keySet = this.monos.keySet();
        Iterator<Integer> itr = keySet.iterator();
        while(itr.hasNext()){
            int key = itr.next();
            Monomial m1 = new Monomial(this.monos.get(key),key);
            Set<Integer> keySet2 = p.monos.keySet();
            Iterator<Integer> itr2 = keySet2.iterator();
            while(itr2.hasNext()){
                int key2 = itr2.next();
                Monomial m2 = new Monomial(p.monos.get(key2),key2);
                acc.addMono(m1.multiply(m2));
            }
        }
        acc.clean();
        return acc;
    }
    
    /**
     * A method to get the result of dividing one Polynomial from another
     * 
     * @param p a Polynomial to be divided from the other
     * @return the resulting Polynomial from dividing the given one from the other
     */
    public Polynomial divide(Polynomial p){
        Polynomial acc = new Polynomial();
        Polynomial tracker = this;
        Monomial first = new Monomial(p.monos.get(p.monos.firstKey()),p.monos.firstKey());
        while(tracker.monos.size() != 0){
            int key = tracker.monos.firstKey();
            double value = tracker.monos.get(key);
            Monomial f = new Monomial(value,key);
            Monomial res = f.divide(first);
            if(res == null || res.exponent < 0){
                return null;
            }
            acc.addMono(res);
            Polynomial np = new Polynomial();
            np.addMono(res);
            np = np.multiply(p);
            tracker = tracker.subtract(np);
            tracker.clean();
        }
        acc.clean();
        return acc;
    }
    
    /**
     * A method to get rid of the Monomials in the Polynomial that have 0.0 as a coefficient
     */
    public void clean(){
        List<Integer> keys = new ArrayList<Integer>();
        Set<Integer> keySet = this.monos.keySet();
        Iterator<Integer> itr = keySet.iterator();
        while(itr.hasNext()){
            int key = itr.next();
            if(this.monos.get(key) == 0.0){
                keys.add(key);
            }
        }
        for(int k: keys){
            keySet.remove(k);
        }
    }

    /**
     * A method to get the String representation of the Polynomial
     * 
     * @return the String representation of the Polynomial
     */
    public String toString(){
        this.clean();
        String res = "";
        Set<Integer> keySet = this.monos.keySet();
        Iterator<Integer> itr = keySet.iterator();
        while(itr.hasNext()){
            int key = itr.next();
            Monomial m = new Monomial(this.monos.get(key),key);
            res += m.toString();
            if(itr.hasNext()) res += " + ";
        }
        return res;
    }   
}