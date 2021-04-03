package css.project.controller;


import css.project.dto.TestResponse;
import css.project.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {
  
  private final TestService testService;
  
  @GetMapping
  public TestResponse readTest() {
    testService.test();
    return new TestResponse("test");
  }
  
}
