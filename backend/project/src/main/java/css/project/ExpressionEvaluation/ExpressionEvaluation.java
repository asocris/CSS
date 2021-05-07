package css.project.ExpressionEvaluation;

import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ParsingException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Stack;

import static css.project.bigNumber.BigNumberMathOps.*;

public class ExpressionEvaluation {
    @Getter
    public static StringBuilder response;

    public static List<String> parseInput(String inputExpression) {
        List<String> result = new ArrayList<String>();
        int position = 0;

        while ( position < inputExpression.length()) {
            result.add( String.valueOf( inputExpression.charAt(position) ) );

            if ( inputExpression.charAt(position) == '^' ) {
                position++;
                int copyPosition = position;
                boolean isSqrtCase = inputExpression.charAt( copyPosition ) == '('
                        && inputExpression.charAt( copyPosition + 1 ) == '0'
                        && inputExpression.charAt( copyPosition + 2 ) == '.'
                        && inputExpression.charAt( copyPosition + 3 ) == '5'
                        && inputExpression.charAt( copyPosition + 4 ) == ')';

                if ( isSqrtCase ) {
                    StringBuilder sb = new StringBuilder();
                    for(int i = 0; i <= 4; i++)
                        sb.append( inputExpression.charAt( copyPosition + i ) );
                    result.add( sb.toString() );
                    position = copyPosition + 5;
                }
            }
            else
                position++;
        }
        return result;
    }

    public static boolean isVariable (String input) {
        if (input.length() == 1
                && (input.charAt(0) >= 'a' && input.charAt(0) <= 'z'
                    || input.charAt(0) >= 'A' && input.charAt(0) <= 'Z'))
            return true;

        if (input.equals("(0.5)")) //sqrt variabile (0.5) case
            return true;

        return false;
    }

    public static List<String> computePostFixPolishNotation(String inputExpressionNotTrimmed) throws Exception {
        String inputExpression = inputExpressionNotTrimmed.replaceAll("\\s+","")
                .replaceAll("\\n+","")
                .replaceAll("\\r+","");
        List<String> inputCharacters = parseInput(inputExpression);
        List<String> postFix = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        Hashtable<String, Integer> opOrder = new Hashtable<>();

        opOrder.put("^",3);
        opOrder.put("*",2);
        opOrder.put("/",2);
        opOrder.put("+",1);
        opOrder.put("-",1);

        int position = 0;

        while ( position < inputCharacters.size() ) {
            String element = inputCharacters.get(position);

            if (isVariable(element) ) {
                postFix.add(element);
            }

            else if (element.equals("(") ) {
                stack.add(element);
            }

            else if (element.equals(")") ) {
                while ( !stack.peek().equals("(") ) {
                    String op = stack.pop();
                    postFix.add(op);
                }
                stack.pop();
            }
            else if (element.equals("+")
                    || element.equals("-")
                    || element.equals("*")
                    || element.equals("/")
                    || element.equals("^")) {

                if ( !stack.empty() && !stack.peek().equals("(") )
                    while ( !stack.empty() && opOrder.get(element) <= opOrder.get(stack.peek()) ) {
                        postFix.add(stack.pop());
                        if(stack.empty()) break;
                        if(stack.peek().equals("("))
                            break;
                    }

                stack.add(element);
            }
            else throw new ParsingException("Imput expresion has invalid character: " + element + " at position: " + (position + 1));
            position++;
        }
        while ( !stack.empty() )
            postFix.add( stack.pop() );

        return postFix;
    }

//    public static boolean isBigNumber( String value) {
//        for (int i = 0; i < min(value.length(),2); i++ )
//            if ( !(value.charAt(i) <= '9' &&  value.charAt(i) >= '0') )
//                return false;
//        return true;
//    }

    public static BigNumber expressionEvaluation( List<String> expression, Hashtable<String, BigNumber> values, boolean isXML) {
        BigNumber result = new BigNumber();
        Stack<String> stack = new Stack<>();
        response = new StringBuilder();
        int position = 0;

        while ( position < expression.size() ) {
            if (isVariable( expression.get(position) ) && !expression.get(position).equals("(0.5)"))
                stack.push(values.get(expression.get(position)).toString());
            else
                if (isVariable( expression.get(position) )) // remaining is (0.5)
                    stack.push( expression.get(position) );
                else {
                    String var2 = stack.pop();
                    String var1 = stack.pop();

                    BigNumber var1Value = new BigNumber(var1);
                    if (var2.equals("(0.5)")) {
                        result = sqrt(var1Value);
                        if (!isXML)
                            response.append("sqrt(").append(var1Value).append(")").append("\n");
                        else
                            response.append("<operation>sqrt<\\operation>\n<value>")
                                    .append(var1Value).append("<\\value>").append("\n");
                        System.out.println("sqrt(" + var1Value + ")");
                    }
                    else {
                        BigNumber var2Value = new BigNumber(var2);

                        switch (expression.get(position)) {
                            case "+":
                                result = add(var1Value, var2Value);
                                break;
                            case "-":
                                result = substract(var1Value, var2Value);
                                break;
                            case "*":
                                result = multiply(var1Value, var2Value);
                                break;
                            case "^":
                                result = pow(var1Value, var2Value);
                                break;
                            case "/":
                                result = divideQutient(var1Value, var2Value);
                                break;
                            default:
                                break;
                        }
                        if (!isXML)
                            response.append(var1Value).append(" ").append(expression.get(position))
                                .append(" ").append(var2Value).append("\n");
                        else
                            response.append("<value>").append(var1Value).append("<\\value>\n")
                                    .append("<operation>").append(expression.get(position)).append("<\\operation>\n")
                                    .append("<value>").append(var2Value).append("<\\value>\n");

                        System.out.println(var1Value + " " + expression.get(position) + " " + var2Value);
                    }
                    stack.push( result.toString() );
                    if (!isXML)
                        response.append("Result = ").append(result).append("\n");
                    else
                        response.append("<result>").append(result).append("<\\result>\n\n");

                    System.out.println("Result = " + result);
                }
            position++;
        }
        response.append("Final response : \n").append(stack.peek());
        return new BigNumber(stack.pop());
    }
}
