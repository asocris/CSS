package css.project;

import css.project.ExpressionEvaluation.ExpressionEvaluation;
import css.project.Utils.Tuple;
import css.project.XMLparser.XMLParser;
import css.project.bigNumber.BigNumber;
import css.project.exception.AppException;
import css.project.exception.custom.ArithmeticAppException;
import css.project.exception.custom.ParsingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Hashtable;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.*;
import static css.project.bigNumber.BigNumberMathOps.compare;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTest {
    @BeforeEach
    void Setup(){
        BigNumber.updateBASE(10);
    }

    @Test
    void When_Evaluate_After_ComputePostfixPolishNotation_Given_ValidInputExpression_Then_CheckValid_Result() throws Exception {
        String expression = "a+((b)^(c))-((d+e))^(0.5)*(f)-(f/(g+h))";
        Hashtable<String, BigNumber> values = new Hashtable<>();
        long a = 1, b = 2, c = 15, d = 100000, e = 23, f = 100, g = 9, h = 1;
        values.put("a", new BigNumber(a));
        values.put("b", new BigNumber(b));
        values.put("c", new BigNumber(c));
        values.put("d", new BigNumber(d));
        values.put("e", new BigNumber(e));
        values.put("f", new BigNumber(f));
        values.put("g", new BigNumber(g));
        values.put("h", new BigNumber(h));

        assertEquals(expressionEvaluation(computePostFixPolishNotation(expression), values, false).toLong(),
              a + (long)(Math.pow(b, c)) - (long)(Math.sqrt(d + e)) * f - ((f / (g + h))));

        expression = "a+e*b^c-d^(0.5)";
        a = 1023; b = 10; c = 1000; d = 1; e = 5;
        values.put("a", new BigNumber(a));
        values.put("b", new BigNumber(b));
        values.put("c", new BigNumber(c));
        values.put("d", new BigNumber(d));
        values.put("e", new BigNumber(e));
        System.out.println("-----------------");
        assertEquals(compare(
                expressionEvaluation(computePostFixPolishNotation(expression), values, false),
                new BigNumber("5" + "0".repeat(996) + "1022")
        ), 0);

    }

    @Test
    void When_Evaluate_After_ComputePostfixPolishNotation_Given_ValidInputExpression_And_InvalidExpresionResult_Then_ThrowException() {
        String expression1 = "a+b^c-(d+e)^(0.5)*f-(f/(g+h))";
        Hashtable<String, BigNumber> values = new Hashtable<>();
        long a = 1, b = 2, c = 15, d = 100000, e = 23, f = 1000000000000L, g = 9, h = 1;
        values.put("a", new BigNumber(a));
        values.put("b", new BigNumber(b));
        values.put("c", new BigNumber(c));
        values.put("d", new BigNumber(d));
        values.put("e", new BigNumber(e));
        values.put("f", new BigNumber(f));
        values.put("g", new BigNumber(g));
        values.put("h", new BigNumber(h));

        ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
                () -> expressionEvaluation(computePostFixPolishNotation(expression1), values, false));
        String expectedMessage = "Substract result Negative";
        assertTrue(exception.getMessage().contains(expectedMessage));

        String expression2 = "a+e*b^c-d^(0.5)+c/a";
        a = 0; b = 10; c = 1000; d = 1; e = 5;
        values.put("a",new BigNumber(a));
        values.put("b",new BigNumber(b));
        values.put("c",new BigNumber(c));
        values.put("d",new BigNumber(d));
        values.put("e",new BigNumber(e));
        exception = assertThrows(ArithmeticAppException.class,
                () -> expressionEvaluation(computePostFixPolishNotation(expression2), values, false));
        expectedMessage = "Division by 0";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void When_IsCharacter_And_Given_Character_Then_True()
    {
        //arrange
        String input = "a";
        //act
        var result = ExpressionEvaluation.isVariable(input);
        //assert
        assertTrue(result == true);
    }

    @Test
    void When_IsCharacter_And_Given_Sqrt_Then_True()
    {
        //arrange
        String input = "(0.5)";
        //act
        var result = ExpressionEvaluation.isVariable(input);
        //assert
        assertTrue(result == true);
    }

    @Test
    void When_IsCharacter_And_Given_NonCharacter_Then_False()
    {
        //arrange
        String input = "105+";
        //act
        var result = ExpressionEvaluation.isVariable(input);
        //assert
        assertTrue(result == false);
    }

    @Test
    void When_ParseInput_Then_List_With_Input_Items()
    {
        //arrange
        String input = "(a+b)+f^(0.5)";
        var expectedResult = new ArrayList<String>();
        expectedResult.add("(");
        expectedResult.add("a");
        expectedResult.add("+");
        expectedResult.add("b");
        expectedResult.add(")");
        expectedResult.add("+");
        expectedResult.add("f");
        expectedResult.add("^");
        expectedResult.add("(0.5)");

        //act
        var result = ExpressionEvaluation.parseInput(input);

        //assert
        int k = 0;
        for (var e: expectedResult) {
            assertTrue(e.equals(result.get(k)));
            k++;
        }
    }

    @Test
    void When_ComputePostfixPolishNotation_And_Given_An_InvalidCharacter_Then_Exception()
    {
        //arrange
        String input = "(a+b)+z^$+c";

        //act&assert
        var exception = assertThrows(ParsingException.class,() -> computePostFixPolishNotation(input));
        var  expectedMessage = "Imput expresion has invalid character: $ at position: 9";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void When_ComputePostfixPolishNotation_And_Given_ValidInputExpression_Then_Valid_PostfixPolishNotation() throws Exception {
        //arrange
        String input = "a - b * c /  (d + e)^f^g";

        var expectedResult = new ArrayList<String>();
        expectedResult.add("a");
        expectedResult.add("b");
        expectedResult.add("c");
        expectedResult.add("*");
        expectedResult.add("d");
        expectedResult.add("e");
        expectedResult.add("+");
        expectedResult.add("f");
        expectedResult.add("^");
        expectedResult.add("g");
        expectedResult.add("^");
        expectedResult.add("/");
        expectedResult.add("-");

        //act
        var result = computePostFixPolishNotation(input);

        //assert
        int k = 0;
        for (var e: expectedResult) {
            assertTrue(e.equals(result.get(k)));
            k++;
        }
    }

    @Test
    void When_Evaluate_After_XMLParse_And_ComputePostfixPolishNotation_And_Given_ValidInputExpression_Then_CheckValid_Result() throws Exception {
        String expression = "<expression>\n" +
                "\t<variable> a </variable>\n" +
                "\t  <operation> * </operation>\n" +
                "\t<variable> b </variable>\n" +
                "\t<operation> + </operation>\n" +
                "\t<variable> c </variable>\n" +
                "</expression>\n" +
                "\t<operation> ^ </operation>\n" +
                "\t<variable> (0.5) </variable>\n" +
                "\t<operation> + </operation>\n" +
                "\t<variable> d </variable>\n" +
                "<variableValue> a </variableValue>\n" +
                "<value>134454</value>\n" +
                "   <variableValue> b </variableValue>\n" +
                "       <value> 23412 </value>\n" +
                "<variableValue> c  \n" +
                "</variableValue>\n" +
                "<value> 123 \n" +
                "</value>\n" +
                "<variableValue> d  \n" +
                "</variableValue>\n" +
                "<value> 2312321553235235 \n" +
                "</value>\n";
        System.out.println(expression);
        System.out.println("-------");
        Tuple<String,Hashtable<String,BigNumber>> result =  XMLParser.GetExpressionFromXML(expression);
        System.out.println(result.first);

        for (String key: result.second.keySet()) {
            System.out.println(key + " = " + result.second.get(key)  );
        }

        long a = 23412, b = 134454, c = 123, d = 2312321553235235L;
        assertEquals(compare(
                expressionEvaluation(computePostFixPolishNotation(result.first), result.second, true),
                (long) (Math.sqrt(a * b + c) + d)
        ), 0);

        System.out.println(ExpressionEvaluation.getResponse().toString());
        AppException exception = assertThrows(ParsingException.class,
                () -> computePostFixPolishNotation(expression));
        String expectedMessage = "Imput expresion has invalid character";
        System.out.println(exception.getMessage());
        assertTrue(exception.getMessage().contains(expectedMessage));

    }
}
