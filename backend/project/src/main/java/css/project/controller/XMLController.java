package css.project.controller;

import css.project.dto.ExpressionDto;
import css.project.service.ComputeService;
import css.project.service.XMLService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xml")
@RequiredArgsConstructor
public class XMLController {

    private final XMLService xmlService;

    @PostMapping
    public String computeExpression(@RequestBody String expression) {
        return xmlService.computeExpression(expression);
    }


}
