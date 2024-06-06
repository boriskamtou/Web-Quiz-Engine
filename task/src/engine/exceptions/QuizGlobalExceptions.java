package engine.exceptions;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class QuizGlobalExceptions extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<?> userAlreadyExistException() {
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(QuizNotFoundException.class)
    public ResponseEntity<?> quizNotFoundException() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(UnAuthorizeActionException.class)
    public ResponseEntity<?> unAuthorizeActionException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
