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

import java.util.Hashtable;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.*;
import static css.project.bigNumber.BigNumberMathOps.compare;
import static org.junit.jupiter.api.Assertions.*;

public class XMLParserTests {

    @Test
    void When_InvalidXMLExpresion_Then_Exception() {
        String expression = "<expression>\n" +
                "\t<variable> a </variable>\n" +
                "\t  <operation> * </operation>\n" +
                "\t<variable> b </variable>\n" +
                "\t<operation> + </operator>\n" +
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

        var exception = assertThrows(ParsingException.class,() -> XMLParser.GetExpressionFromXML(expression));
        var  expectedMessage = "A tag is not properly closed or written";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void When_ValidXMLExpresion_Then_An_Expression_Is_Created() {
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

        var result = XMLParser.GetExpressionFromXML(expression);

        assertTrue(result.first.equals("(a*b+c)^(0.5)+d"));
    }
}
