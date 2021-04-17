package css.project.dto;

import java.util.Map;
import lombok.Data;

@Data
public class ExpressionDto {
  
  private String expression;
  
  private Map<String, String> variables;
  
}
