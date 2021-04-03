package css.project.exception;

import static java.text.MessageFormat.format;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

class ExceptionUtils {
  
  private static final String messageKey = "Message";
  
  
  static String formatException(Exception e) {
    return format(">>> Exception: {0} message: {1} \n Trace: {2}", e.getClass(), e.getMessage(), e.getStackTrace());
  }
  
  
  static ResponseEntity<Object> create(AppException ex, HttpStatus httpStatus) {
    Map<String, String> body = new HashMap<>();
    body.put(messageKey, ex.getMessage());
    return new ResponseEntity<>(body, httpStatus);
  }
  
  
}
