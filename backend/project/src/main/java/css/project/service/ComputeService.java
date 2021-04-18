package css.project.service;

import java.util.Hashtable;
import java.util.Map;

import css.project.ExpressionEvaluation.ExpressionEvaluation;
import css.project.bigNumber.BigNumber;
import org.springframework.stereotype.Service;

import static css.project.ExpressionEvaluation.ExpressionEvaluation.computePostFixPolishNotation;

@Service
public class ComputeService {
  
    public String computeExpression(String expression, Map<String, String> variables) {
      try {
        Hashtable<String, BigNumber> variables2 = new Hashtable<>();
        for (String s : variables.keySet())
          variables2.put(s, new BigNumber(variables.get(s)));
        BigNumber n = ExpressionEvaluation.expressionEvaluation(computePostFixPolishNotation(expression), variables2);
        System.out.println(n);
        return ExpressionEvaluation.getResponse().toString();
      } catch (Exception e) {
        return e.getMessage();
      }
    }

}
