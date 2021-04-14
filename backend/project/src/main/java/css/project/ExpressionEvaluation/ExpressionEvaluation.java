package css.project.ExpressionEvaluation;

import css.project.bigNumber.BigNumber;
import css.project.bigNumber.BigNumberMathOps;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import static css.project.bigNumber.BigNumberMathOps.*;
import static java.lang.Integer.min;

public class ExpressionEvaluation {
    private static List<String> ParseInput(String inputExpression)
    {
        List<String> result = new ArrayList<String>();
        int position = 0;

        while( position < inputExpression.length())
        {
            result.add( String.valueOf( inputExpression.charAt(position) ) );

            if( inputExpression.charAt(position) == '^' )
            {
                position++;
                int copyPosition = position;
                boolean isSqrtCase = inputExpression.charAt( copyPosition ) == '('
                        && inputExpression.charAt( copyPosition + 1 ) == '0'
                        && inputExpression.charAt( copyPosition + 2 ) == '.'
                        && inputExpression.charAt( copyPosition + 3 ) == '5'
                        && inputExpression.charAt( copyPosition + 4 ) == ')';

                if( isSqrtCase )
                {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i <= 4; i++)
                        sb.append( inputExpression.charAt( copyPosition + i ) );
                    result.add( sb.toString() );
                    position = copyPosition + 5;
                }
                else
                    continue;
            }
            else
            {
                position++;
            }
        }
        return result;
    }

    public static boolean IsVariable ( String input )
    {
        if( input.length() == 1
                && (input.charAt(0) >= 'a' && input.charAt(0) <= 'z'
                || input.charAt(0) >= 'A' && input.charAt(0) <= 'Z'))
            return true;

        if( input.length() > 1) //sqrt variabile (0.5) case
            return true;

        return false;
    }

    public static List<String> ComputePostFixPolishNotation(String inputExpression)
    {
        List<String> inputCharacters = ParseInput(inputExpression);
        List<String> postFix = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        Hashtable<String, Integer> opOrder = new Hashtable<String, Integer>();

        opOrder.put("^",3);
        opOrder.put("*",2);
        opOrder.put("/",2);
        opOrder.put("+",1);
        opOrder.put("-",1);

        int position = 0;

        while ( position < inputCharacters.size() )
        {
            String element = inputCharacters.get(position);

            if( IsVariable(element) )
            {
                postFix.add(element);
                position++;
                continue;
            }

            if( element.equals("(") ) {
                stack.add(element);
                position++;
                continue;
            }

            if( element.equals(")") ){

                while ( !stack.peek().equals("(") )
                {
                    String op = stack.pop();
                    postFix.add(op);
                }
                stack.pop();

                position++;
                continue;
            }
            if(element.equals("+")
                    || element.equals("-")
                    || element.equals("*")
                    || element.equals("/")
                    || element.equals("^")) {

                if( !stack.empty() && !stack.peek().equals("(") )
                    while ( !stack.empty() && opOrder.get(element) <= opOrder.get(stack.peek()) ) {
                        postFix.add(stack.pop());
                        if(stack.empty()) break;
                        if(stack.peek().equals("("))
                            break;
                    }

                stack.add(element);
                position++;
            }
        }
        while( !stack.empty() )
            postFix.add( stack.pop() );

        return postFix;
    }

    public static boolean IsBigNumber( String value)
    {
        for (int i = 0; i < min(value.length(),2); i++ )
            if( !(value.charAt(i) <= '9' &&  value.charAt(i) >= '0') )
                return false;

        return true;
    }

    public static BigNumber ExpressionEvaluation( List<String> expression, Hashtable<String,BigNumber> values )
    {
        BigNumber result = new BigNumber();
        Stack<String> stack = new Stack<String>();
        int position = 0;

        while ( position < expression.size() )
        {
            if(IsVariable( expression.get(position) ) && !expression.get(position).equals("(0.5)"))
                stack.push(values.get(expression.get(position)).toString());
            else
                if (IsVariable( expression.get(position) ))
                stack.push( expression.get(position) );
            else
            {
                String var1 = stack.pop();
                String var2 = stack.pop();

                BigNumber var2Value = new BigNumber(var2);
                if( var1.equals("(0.5)") )
                {
                    result = sqrt(var2Value);
                }
                else {
                    BigNumber var1Value = new BigNumber(var1);

                    switch (expression.get(position)) {
                        case "+":
                            result = add(var1Value, var2Value);
                            break;
                        case "-":
                            result = substract(var2Value, var1Value);
                            break;
                        case "*":
                            result = multiply(var1Value, var2Value);
                            break;
                        case "^":
                            result = pow(var2Value, var1Value);
                            break;
                        case "/":
                            result = divideQutient(var2Value, var1Value);
                            break;
                        default:
                            break;

                    }

                    stack.push( result.toString() );
                    System.out.println(var1 + " = " + var1Value + " , " + var2 + " = " + var2Value
                            + " operator = " + expression.get(position) );

                }

                System.out.println("Result = " + result);
            }
            position++;
        }

     return new BigNumber(stack.pop());

    }
}
