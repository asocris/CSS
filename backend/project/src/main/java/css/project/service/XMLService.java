package css.project.service;

import java.util.Hashtable;
import java.util.Map;

import css.project.ExpressionEvaluation.ExpressionEvaluation;
import css.project.Utils.Tuple;
import css.project.XMLparser.XMLParser;
import css.project.bigNumber.BigNumber;
import org.springframework.stereotype.Service;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.computePostFixPolishNotation;

@Service
public class XMLService {

    public String computeExpression(String XMLexpression) {
        try {
            Tuple<String,Hashtable<String, BigNumber>> result =  XMLParser.GetExpressionFromXML(XMLexpression);

            BigNumber n = ExpressionEvaluation.expressionEvaluation(computePostFixPolishNotation(result.first),
                    result.second, true);
            System.out.println(n);
            return ExpressionEvaluation.getResponse().toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
