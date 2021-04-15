package css.project;

import css.project.ExpressionEvaluation.ExpressionEvaluation;
import css.project.XMLparser.XMLParser;
import css.project.bigNumber.BigNumber;
import org.junit.jupiter.api.Test;

import java.util.Hashtable;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.*;

public class ExpressionTest {

    @Test
    void run()
    {
        String expression = "a-b^(0.5)";
        Hashtable<String, BigNumber> values = new Hashtable<>();
        values.put("a",new BigNumber("1000"));
        values.put("b",new BigNumber("2"));
        values.put("c",new BigNumber("3"));
        values.put("d",new BigNumber("2"));
        values.put("e",new BigNumber("10"));


        System.out.println(ExpressionEvaluation( ComputePostFixPolishNotation(expression), values ));
    }

    @Test
    void run2()
    {
        String expression = "<expression>\n" +
                "\t\t<variable> a </variable>\n" +
                "\t\t  <operation> * </operation>\n" +
                "\t\t<variable> b </variable>\n" +
                "\t\t<operation> + </operation>\n" +
                "\t\t<variable> c </variable>\n" +
                "\t</expression>\n" +
                "\t\t<operation> ^ </operation>\n" +
                "\t\t<variable> (0.5) </variable>\n" +
                "\t\t<operation> + </operation>\n" +
                "\t\t<expression>\n" +
                "\t\t<operation> / </operation>\n" +
                "\t\t<variable> (0.5) </variable>\n" +
                "\t\t<operation> - </operation>\n" +
                "\t\t<variable> d </variable>\n" +
                "\t\t<expression> </expression>\n" +
                "\t\t</expression>\n" +
                "\t<operation> + </operation>";
        System.out.println(XMLParser.GetExpressionFromXML(expression));
    }
}
