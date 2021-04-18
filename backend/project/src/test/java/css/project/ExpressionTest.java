package css.project;

import css.project.Utils.Tuple;
import css.project.XMLparser.XMLParser;
import css.project.bigNumber.BigNumber;
import css.project.exception.custom.ArithmeticAppException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.*;
import static css.project.bigNumber.BigNumberMathOps.compare;
import static org.junit.jupiter.api.Assertions.*;

public class ExpressionTest {
    @BeforeEach
    void Setup(){
        BigNumber.updateBASE(100000);
    }

    @Test
    void runCorrectExpresions() {
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

        assertEquals(expressionEvaluation(computePostFixPolishNotation(expression), values).toLong(),
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
                expressionEvaluation(computePostFixPolishNotation(expression), values),
                new BigNumber("5" + "0".repeat(996) + "1022")
        ), 0);

    }

    @Test
    void runWrongExpresions() {
        String expression1 = "a+b^c-(d+e)^(0.5)*f-(f/(g+h))";
        Hashtable<String, BigNumber> values = new Hashtable<>();
        long a = 1, b = 2, c = 15, d = 100000, e = 23, f = 1000000000000L, g = 9, h = 1;
        values.put("a",new BigNumber(a));
        values.put("b",new BigNumber(b));
        values.put("c",new BigNumber(c));
        values.put("d",new BigNumber(d));
        values.put("e",new BigNumber(e));
        values.put("f",new BigNumber(f));
        values.put("g",new BigNumber(g));
        values.put("h",new BigNumber(h));

        ArithmeticAppException exception = assertThrows(ArithmeticAppException.class,
                () -> expressionEvaluation(computePostFixPolishNotation(expression1), values));
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
                () -> expressionEvaluation(computePostFixPolishNotation(expression2), values));
        expectedMessage = "Division by 0";
        assertTrue(exception.getMessage().contains(expectedMessage));

    }

    @Test
    void run2()
    {
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
                "<variableValue> a </variableValue>" +
                "<value>134454</value>" +
                "   <variableValue> b </variableValue>" +
                "       <value> 23412 </value>" +
                "<variableValue> d  " +
                "</variableValue>" +
                "<value> 2312321553235235 " +
                "</value>";
        System.out.println(expression);
        System.out.println("-------");
        Tuple<String,Hashtable<String,BigNumber>> result =  XMLParser.GetExpressionFromXML(expression);
        System.out.println(result.first);

        for (String key: result.second.keySet()) {
            System.out.println(key + " = " + result.second.get(key)  );

        }
    }
}
