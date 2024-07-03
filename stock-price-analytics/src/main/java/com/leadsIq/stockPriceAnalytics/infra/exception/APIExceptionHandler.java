package com.leadsIq.stockPriceAnalytics.infra.exception;

import static com.leadsIq.stockPriceAnalytics.domain.util.Constants.ENTITY_NOT_FOUND_ERROR_MESSAGE;
import static com.leadsIq.stockPriceAnalytics.domain.util.Constants.UNEXPECTED_ERROR_MESSAGE;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.leadsIq.stockPriceAnalytics.domain.exception.ClientException;
import com.leadsIq.stockPriceAnalytics.domain.exception.EntityNotFoundException;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@ControllerAdvice
@Slf4j
public class APIExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorResponse> handleClientException(ClientException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(RuntimeException ex) {
        log.error(UNEXPECTED_ERROR_MESSAGE, ex);
        return ResponseEntity.internalServerError()
            .body(new ErrorResponse(UNEXPECTED_ERROR_MESSAGE));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.warn(ENTITY_NOT_FOUND_ERROR_MESSAGE, ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ex.getMessage()));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status,
        WebRequest request) {
        val errors = new HashSet<ErrorResponse>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            val fieldName = ((FieldError) error).getField();
            val errorMessage = error.getDefaultMessage();
            val stringBuilder = new StringBuilder();
            errors.add(new ErrorResponse(
                stringBuilder.append(fieldName).append(" ").append(errorMessage).toString()));
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
