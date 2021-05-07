package css.project;

import css.project.controller.ComputeController;
import css.project.controller.TestController;
import css.project.controller.XMLController;
import css.project.dto.ExpressionDto;
import css.project.service.ComputeService;
import css.project.service.TestService;
import css.project.service.XMLService;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class TestRemaining {
    @Test
    void testTestController() {
        assertEquals(new TestController(new TestService()).readTest().getMsg(), "test");
        new TestController(new TestService()).readTest().setMsg("2");
    }

    @Test
    void testComputeController() {
        assertEquals(new TestController(new TestService()).readTest().getMsg(), "test");
        String exp = "a+b-c";
        Map<String, String> variables = new HashMap<>();
        variables.put("a", "5");
        variables.put("b", "5");
        variables.put("c", "5");
        ExpressionDto e = new ExpressionDto();
        e.setExpression(exp);
        e.setVariables(variables);
        assertTrue(new ComputeController(new ComputeService()).computeExpression(e)
                .contains("Final response : \n" +
                        "5"));

        variables.put("c", "1000");
        assertEquals(new ComputeService().computeExpression(exp, variables), "Substract result Negative");
    }

    @Test
    void testXMLController() {
        assertEquals(new TestController(new TestService()).readTest().getMsg(), "test");
        String exp = "<expression>\n" +
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
                "   <value>15</value>\n" +
                "<variableValue> b </variableValue>\n" +
                "   <value> 15 </value>\n" +
                "<variableValue> c  \n" +
                "</variableValue>\n" +
                "   <value> 15 \n" +
                "   </value>\n" +
                "<variableValue> d  \n" +
                "</variableValue>\n" +
                "   <value> 21 \n" +
                "   </value>\n";
        assertTrue(new XMLController(new XMLService()).computeExpression(exp)
                .contains("Final response : \n" +
                        "36"));
        exp = "<expression>\n" +
                "\t<variable> a </variable>\n" +
                "\t<operation> + </operation>\n" +
                "\t<variable> b </variable>\n" +
                "\t<operation> - </operation>\n" +
                "\t<variable> c </variable>\n" +
                "<variableValue> a </variableValue>\n" +
                "   <value>15</value>\n" +
                "<variableValue> b </variableValue>\n" +
                "   <value> 15 </value>\n" +
                "<variableValue> c  \n" +
                "</variableValue>\n" +
                "   <value> 100000 \n" +
                "   </value>\n" +
                "<variableValue> d  \n" +
                "</variableValue>\n" +
                "   <value> 21 \n" +
                "   </value>\n";
        assertEquals(new XMLController(new XMLService()).computeExpression(exp), "Substract result Negative");

    }

}
