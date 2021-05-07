package css.project;

import css.project.XMLparser.XMLParser;
import css.project.exception.custom.ParsingException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class XMLParserTests {

    @Test
    void When_InvalidXMLExpresion_Then_ParsingException() {
        //arrange
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

        //act&assert
        var exception = assertThrows(ParsingException.class,() -> XMLParser.GetExpressionFromXML(expression));
        var  expectedMessage = "A tag is not properly closed or written";
        assertTrue(exception.getMessage().contains(expectedMessage));
    }

    @Test
    void When_ValidXMLExpresion_Then_An_Expression_Is_Created() {
        //arrange
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
        //act
        var result = XMLParser.GetExpressionFromXML(expression);
        //assert
        assertTrue(result.first.equals("(a*b+c)^(0.5)+d"));
    }
}
