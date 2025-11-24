package wowa.myqna.global;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wowa.myqna.global.exception.ApplicationCustomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_FIELD = "message";
    private static final String ERROR_PAGE = "error/error-page";

    @ExceptionHandler(ApplicationCustomException.class)
    public String handlePdfUploadException(ApplicationCustomException e, Model model) {

        model.addAttribute(ERROR_FIELD, e.getMessage());

        return ERROR_PAGE;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public String handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, Model model) {
        List<String> messages = new ArrayList<>();

        ex.getBindingResult().getAllErrors().forEach(error -> {
            String msg = Optional.ofNullable(error.getDefaultMessage())
                    .orElse("유효하지 않은 값입니다.");
            messages.add(msg);
        });

        model.addAttribute(ERROR_FIELD, messages);

        return ERROR_PAGE;
    }
}
