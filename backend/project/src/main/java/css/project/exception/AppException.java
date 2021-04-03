package css.project.exception;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppException extends RuntimeException {
  
  private String message;
  
  
  public AppException(String message) {
    this.message = message;
  }
  
}

