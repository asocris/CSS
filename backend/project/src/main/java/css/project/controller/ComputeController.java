package css.project.controller;

import css.project.dto.ExpressionDto;
import css.project.service.ComputeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compute")
@RequiredArgsConstructor
public class ComputeController {
  
  private final ComputeService computeService;
  
  @PostMapping
  public String computeExpression(@RequestBody ExpressionDto expressionDto) {
    return computeService.computeExpression(expressionDto.getExpression(), expressionDto.getVariables());
  }
  
  
}
