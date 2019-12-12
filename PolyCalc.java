/**
 * A calculator for Polynomials that supports user imput of an expression or a file path to a file to be evaluated.
 *
 * @author (Tobias Ephron)
 * @version (1 - Non-GUI)
 */
import java.util.*;
import java.io.*;
public class PolyCalc
{
    /**
     * An enum for the states of the finite state machine
     */
    enum FSMParserState{
        WC(false), WP(true), WD(false), WV(true),
        WH(true), WE(false), WO(true), ERR(false);
        
        /** A variable to keep track of if a state is an accepted state */
        private final boolean isAccept;
        
        /** A contructor method */
        FSMParserState(boolean isAccept){ this.isAccept = isAccept;}
        
        /**
         * A method for getting the value of if the state is accepted
         * 
         * @return whether or not a state is accepted
         */
        public boolean isAccept(){ return isAccept;}
    }
    
    /** A variable to keep a memory of variables */
    private TreeMap<Character,Polynomial> polynomials;
    
    /**
     * A PolyCalc constructor
     */
    public PolyCalc(){
        this.polynomials = new TreeMap<Character,Polynomial>();
    }
    
    /** 
     * The main method
     * 
     * @param args an array of Strings
     */
    public static void main(String[] args){
        PolyCalc pc = new PolyCalc();
        Scanner s = new Scanner(System.in);
        String result = "";
        System.out.println("Welcome to the Polynomial Calculator");
        boolean running = true;
        while(running){
            boolean expres = false;
            boolean file = false;
            System.out.println("");
            System.out.println("Would you like to give an expression or file? (exp/file/exit)");
            String ans = s.nextLine().toLowerCase();
            if(ans.equals("exp") || ans.equals("expression")){
                expres = true;
            }
            else if(ans.equals("file")){
                file = true;
            }
            else if(ans.equals("exit") || ans.equals("quit")){
                running = false;
                continue;
            }
            else{
                System.out.println("");
                System.out.println("Please give a valid input");
                continue;
            }
            while(expres){
                Scanner s3 = new Scanner(System.in);
                System.out.println("");
                System.out.println("What expression would you like to evaluate?");
                String e = s3.nextLine();
                if(e.toLowerCase().equals("exit") || e.toLowerCase().equals("quit")){
                    expres = false;
                    continue;
                }
                System.out.println("");
                System.out.println(e + " = " + pc.evalLine(e));
            }
            while(file){
                pc = new PolyCalc();
                System.out.println("");
                System.out.println("What is the file path for the file you'd like to evaluate?");
                String fPath = s.next();
                if(fPath.toLowerCase().equals("exit") || fPath.toLowerCase().equals("quit")){
                    file = false;
                    continue;
                }
                File f = new File(fPath);
                try{
                    if(f.exists()){
                        if(f.isFile()){
                            try{
                                BufferedReader r = new BufferedReader(new FileReader(fPath));
                                String ln;
                                while((ln = r.readLine()) != null){
                                    System.out.println("");
                                    System.out.print(ln + " = ");
                                    System.out.println(pc.evalLine(ln));
                                }
                            }
                            catch(IOException e){
                                System.out.println("");
                                System.err.println(e);
                                continue;
                            }
                        }
                        else{
                            System.out.println("");
                            System.out.println("This file path is not a file");
                        }
                    }
                    else{
                        System.out.println("");
                        System.out.println("This file does not exist");
                        continue;
                    }
                }
                catch(SecurityException e){
                    System.out.println("");
                    System.err.println(e); // no permissions
                    System.out.println("");
                    System.out.println("Please give a valid file path");
                    continue;
                }
                boolean again = true;
                while(again){
                    System.out.println("");
                    System.out.println("Would you like to evaluate another file? (y/n)");
                    String a = s.next().toLowerCase();
                    if(a.charAt(0) == 'y'){
                        again = false;
                    }
                    else if(a.charAt(0) == 'n'){
                        again = false;
                        file = false;
                    }
                    else{
                        System.out.println("");
                        System.out.println("Please give a valid input");
                    }
                }   
            }
            boolean goAgain = true;
            while(goAgain){
                System.out.println("");
                System.out.println("Would you like to do another evaluation? (y/n)");
                String b = s.next().toLowerCase();
                if(b.charAt(0) == 'y'){
                    goAgain = false;
                }
                else if(b.charAt(0) == 'n'){
                    goAgain = false;
                    running = false;
                }
                else{
                    System.out.println("");
                    System.out.println("Please give a valid input");
                }
            }
        }
        System.out.println("");
        System.out.println("Thanks for using the Polynomial Calculator!");
    }
    
    /**
     * A method to evaluate a line
     * 
     * @param line a String representing the line to be evaluated
     * @return a String of either the Polynomial result or an error message
     */
    public String evalLine(String line){
        String acc = "";
        String error = "Unable to evaluate";
        String poly = "";
        int i = 0;
        boolean equals = false;
        while(i < line.length() && !equals){
            char c = line.charAt(i++);
            if(!Character.isWhitespace(c)){
                if(c == '='){
                    equals = true;
                    int k;
                    for(k=0;k<line.length();k++){
                        if(line.charAt(k) == '=') return error;
                        if(!Character.isWhitespace(line.charAt(k))){
                            if(!this.polynomials.containsKey(line.charAt(k))){
                                this.polynomials.put(line.charAt(k),null);
                            }
                            break;
                        }
                    }
                    for(int j=i;j<line.length();j++){
                        poly += line.charAt(j);
                    }
                    poly = this.getPolys(poly);
                    if(poly == null) return error;
                    ArrayList<Token> helper1 = this.fsmParser(poly);
                    if(helper1 == null){
                        return error;
                    }
                    ArrayList<Token> helper2 = this.toPostFix(helper1);
                    if(helper2 == null){
                        return error;
                    }
                    Polynomial helper3 = this.postFixEval(helper2);
                    if(helper3 == null){
                        return error;
                    }
                    this.polynomials.put(line.charAt(k),helper3);
                    return helper3.toString();
                }
                else if(Character.isDigit(c) || c == 'x' || c == '.' || c == '-' || (this.polynomials.containsKey(c) && !line.contains("="))){
                    try{
                        poly = this.getPolys(line);
                    }
                    catch(NullPointerException e){
                        return error;
                    }
                    ArrayList<Token> helper1 = this.fsmParser(poly);
                    if(helper1 == null){
                        return error;
                    }
                    ArrayList<Token> helper2 = this.toPostFix(helper1);
                    if(helper2 == null){
                        return error;
                    }
                    Polynomial helper3 = this.postFixEval(helper2);
                    if(helper3 == null){
                        return error;
                    }
                    return helper3.toString();
                }
            }
        }
        return error;
    }
    
    /** 
     * A method to get the polynomials in the string that are stored in the polynomials ArrayList
     * 
     * @param p a String to be checked for stored polynomials
     * @return a String of the resulting polynomial
     */
    public String getPolys(String p){
        String acc = "";
        for(int i=0;i<p.length();i++){
            char c = p.charAt(i);
            if(!Character.isWhitespace(c)){
                if(c != 'x' && !Character.isDigit(c) &&
                    c != '.' && c != '^' && this.isOperator(c) == null){
                        try{
                            if(this.polynomials.containsKey(c)){
                                acc += "(";
                                acc += this.polynomials.get(c).toString();
                                acc += ")";
                            }
                            else{
                                acc += c;
                            }
                        }
                        catch(NullPointerException e){
                            return null;
                        }
                }
                else{
                    acc += c;
                }
            }
        }
        return acc;
    }
    
    /**
     * A finite state machine parser to parse a given String into an ArrayList of Tokens
     * 
     * @param input the String to be parsed
     * @return an ArrayList of Tokens parsed from the given String
     */
    public ArrayList<Token> fsmParser(String input){
        ArrayList<Token> l = new ArrayList<Token>();
        Polynomial p = new Polynomial();
        FSMParserState curState = FSMParserState.WC;
        String coef = "";
        String exp = "0";
        int i = 0, iLen = input.length();
        // Character.digit(c,10);
        while(curState != FSMParserState.ERR && i < iLen){
            char c = input.charAt(i++);
            switch (curState){
                case WC: // Waiting for coefficient
                    if(!Character.isWhitespace(c)){ // ignore whitespace
                        if(Character.isDigit(c)){
                            coef += Integer.toString(Character.digit(c,10));
                            curState = FSMParserState.WP;
                        }
                        else if(c == '.'){
                            coef += ".";
                            curState = FSMParserState.WD;
                        }
                        else if(c == 'x'){
                            if(coef.equals("-") || coef.equals("")) coef += "1";
                            curState = FSMParserState.WH;
                        }
                        else if(c == '('){
                            l.add(new LeftParentheses());
                        }
                        else if(isOperator(c) != null && ((i >= 2 && input.charAt(i-2) == ')') || (i >= 3 && input.charAt(i-3) == ')'))){
                            Operator a = isOperator(c);
                            l.add(a);
                        }
                        else if(c == '-'){
                            char next = ' ';
                            for(int j=i;j<input.length();j++){
                                next = input.charAt(j);
                                if(!Character.isWhitespace(next)){
                                    break;
                                }
                            }
                            if(Character.isDigit(next)){
                                coef += "-";
                            }
                            else if(i == 1 && next == '('){
                                p.addMono(-1.0,0);
                                l.add(p);
                                l.add(new MulOperator());
                                p = p.subtract(p);
                            }
                            else curState = FSMParserState.ERR;
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WP:
                    if(!Character.isWhitespace(c)){        
                        if(c == '.'){
                            coef += ".";
                            curState = FSMParserState.WD;
                        }
                        else if(Character.isDigit(c)){
                            coef += Integer.toString(Character.digit(c,10));
                        }
                        else if(c == 'x'){
                            curState = FSMParserState.WH;
                        }
                        else if(isOperator(c) != null){
                            Operator a = isOperator(c);
                            p.addMono(Double.parseDouble(coef),0);
                            l.add(p);
                            if(a instanceof LeftParentheses){
                                l.add(new MulOperator());
                            }
                            l.add(a);
                            p = p.subtract(p); 
                            curState = FSMParserState.WC;
                            coef = "";
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WD:
                    if(!Character.isWhitespace(c)){
                        if(Character.isDigit(c)){
                            curState = FSMParserState.WV;
                            coef += Integer.toString(Character.digit(c,10));
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WV:
                    if(!Character.isWhitespace(c)){
                        if(c == 'x'){
                            curState = FSMParserState.WH;
                        }
                        else if(Character.isDigit(c)){
                            coef += Integer.toString(Character.digit(c,10));
                        }
                        else if(isOperator(c) != null){
                            Operator a = isOperator(c);
                            p.addMono(Double.parseDouble(coef),0);
                            l.add(p);
                            if(a instanceof LeftParentheses){
                                l.add(new MulOperator());
                            }
                            l.add(a);
                            p = p.subtract(p);
                            curState = FSMParserState.WC;
                            coef = "";
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WH:
                    if(!Character.isWhitespace(c)){      
                        if(c == '^'){
                            curState = FSMParserState.WE;
                        }
                        else if(isOperator(c) != null){
                            Operator a = isOperator(c);
                            p.addMono(Double.parseDouble(coef),1);
                            l.add(p);
                            if(a instanceof LeftParentheses){
                                l.add(new MulOperator());
                            }
                            l.add(a);
                            p = p.subtract(p);
                            curState = FSMParserState.WC;
                            coef = "";
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WE:
                    if(!Character.isWhitespace(c)){        
                        if(Character.isDigit(c)){
                            if(exp.equals("0")) exp = Integer.toString(Character.digit(c,10));
                            else{
                                exp += Integer.toString(Character.digit(c,10));
                            }
                            curState = FSMParserState.WO;
                        }
                        else if(isOperator(c) instanceof SubOperator){
                            exp = "-";
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
                case WO:
                    if(!Character.isWhitespace(c)){        
                        if(Character.isDigit(c)){
                            exp += Character.digit(c,10);
                        }   
                        else if(isOperator(c) != null){
                            Operator a = isOperator(c);
                            if(!coef.equals("")) p.addMono(Double.parseDouble(coef),Integer.parseInt(exp));
                            else p.addMono(1,Integer.parseInt(exp));
                            l.add(p);
                            if(a instanceof LeftParentheses){
                                l.add(new MulOperator());
                            }
                            l.add(a);
                            p = p.subtract(p);
                            coef = "";
                            exp = "0";
                            curState = FSMParserState.WC;
                        }
                        else{
                            curState = FSMParserState.ERR;
                        }
                    }
                    break;
            }
        }
        if(input.length() >= 1 && i >= 1 && !(input.charAt(i-1) == ')' && curState == FSMParserState.WC) && !curState.isAccept()) return null;
        else{
            if(curState != FSMParserState.WC){
                if(curState == FSMParserState.WH){
                    if(coef.equals("")) p.addMono(1,1);
                    else{
                        p.addMono(Double.parseDouble(coef),1);
                    }
                }
                else if(curState == FSMParserState.WP || curState == FSMParserState.WV){
                    p.addMono(Double.parseDouble(coef),0);
                }
                else{
                    if(!coef.equals("")) p.addMono(Double.parseDouble(coef),Integer.parseInt(exp));
                    else{
                        p.addMono(1,Integer.parseInt(exp));
                    }
                }
                l.add(p);
                p = p.subtract(p);
                coef = "";
                exp = "0";
            }
            //System.out.println(l);
            return l;
        }
    }
    
    /**
     * A method to check if a given Character is an Operator
     * 
     * @param oper a Character
     * @return either the Operator that the Character represents or null
     */
    public Operator isOperator(char oper){
        if(oper == '+') return new AddOperator();
        else if(oper == '-') return new SubOperator();
        else if(oper == '*') return new MulOperator();
        else if(oper == '/') return new DivOperator();
        else if(oper == '(') return new LeftParentheses();
        else if(oper == ')') return new RightParentheses();
        else return null;
    }
    
    /**
     * A method to turn an ArrayList of Tokens in infix notation to that of postfix notation
     * 
     * @param inFix an ArrayList of Tokens in infix notation
     * @return an ArrayList of Tokens in postfix notation or null
     */
    public ArrayList<Token> toPostFix(ArrayList<Token> inFix){
        ArrayList<Token> postFix = new ArrayList<Token>();
        //System.out.println(postFix);
        Deque<Token> opers = new ArrayDeque<Token>();
        for(int i=0;i<inFix.size();i++){
            Token t = inFix.get(i);
            if(t instanceof Operator){
                if(t.order() == 3){
                    while(opers.peekFirst() != null && opers.peekFirst().order() != 0){
                        postFix.add(opers.removeFirst());
                    }
                    if(opers.peekFirst() == null) return null;
                    else opers.removeFirst();
                }
                else if(opers.peekFirst() != null && t.order() != 0){
                    while(opers.peekFirst() != null && t.order() <= opers.peekFirst().order()){
                        postFix.add(opers.removeFirst());
                    }
                    opers.addFirst(t);
                }
                else{
                    opers.addFirst(t);
                }
            }
            else{
                postFix.add(t);
            }
        }
        while(opers.peekFirst() != null){
            if(opers.peekFirst() instanceof LeftParentheses) return null;
            else postFix.add(opers.removeFirst());
        }
        //System.out.println(postFix);
        return postFix;
    }

    /**
     * A method to evaluate an equation in postfix notation
     * 
     * @param postFix an ArrayList of Tokens in postfix notation
     * @return the resulting polynomial from the evaluation or null
     */
    public Polynomial postFixEval(ArrayList<Token> postFix){
        ExpEval e = new ExpEval();
        Deque<Polynomial> polys = new ArrayDeque<Polynomial>();
        for(int i=0;i<postFix.size();i++){
            Token t = postFix.get(i);
            if(t instanceof Operator){
                Polynomial a = polys.removeFirst();
                Polynomial b = polys.removeFirst();
                Polynomial c = e.evaluate(b,a,t);
                if(c == null) return null;
                polys.addFirst(c);
            }
            else{
                Polynomial p = (Polynomial) t;
                polys.addFirst(p);
            }
        }
        if(polys.size() != 1) return null;
        else return polys.removeFirst();
    }
     
    /**
     * A tester method for the polynomial calculator
     */
    public void polyCalcTest(){
        System.out.print('\u000C');
        Polynomial a = new Polynomial();
        Polynomial b = new Polynomial();
        a.addMono(2,3);
        a.addMono(4,2);
        a.addMono(3,2);
        System.out.println("a = " + a.toString());
        b.addMono(4,5);
        b.addMono(3,2);
        b.addMono(6,2);
        System.out.println("b = " + b.toString());
        Polynomial c = a.add(b);
        System.out.println("c = a + b = " + c.toString());
        System.out.println("c - b = " + c.subtract(b).toString());
        Polynomial d = a.multiply(b);
        System.out.println("d = a * b = " + d.toString());
        Polynomial e = d.divide(b);
        if(e == null){
            System.out.println("Does not evenly divide");
        }
        else{
            System.out.println("e = d / b = " + e.toString());
        }
    }
}
