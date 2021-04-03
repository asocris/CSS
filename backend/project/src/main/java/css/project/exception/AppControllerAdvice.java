package css.project.exception;

import static css.project.exception.ExceptionUtils.create;
import static css.project.exception.ExceptionUtils.formatException;
import css.project.exception.custom.ArithmeticAppException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AppControllerAdvice {
  
  
  @ExceptionHandler(ArithmeticAppException.class)
  public ResponseEntity handleArithmeticException(final ArithmeticAppException e) {
    log.error(formatException(e));
    return create(e, HttpStatus.CONFLICT);
  }
  
}
