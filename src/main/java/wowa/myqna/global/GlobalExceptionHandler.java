package wowa.myqna.global;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import wowa.myqna.global.exception.ApplicationCustomException;


@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationCustomException.class)
    public String handlePdfUploadException(ApplicationCustomException e, Model model) {

        model.addAttribute("message", e.getMessage());

        return "error/error-page";
    }
}
