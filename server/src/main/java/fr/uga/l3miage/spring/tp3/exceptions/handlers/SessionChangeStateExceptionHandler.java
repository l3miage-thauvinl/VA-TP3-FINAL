package fr.uga.l3miage.spring.tp3.exceptions.handlers;

import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionChangeStateRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.SessionChangeStateException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
@ControllerAdvice
public class SessionChangeStateExceptionHandler {
    /*
    @ExceptionHandler(CreationSessionRestException.class)
    public ResponseEntity<String> handle(HttpServletRequest httpServletRequest, Exception exception){
        return ResponseEntity.badRequest().body(exception.getMessage());
    }
     */
    @ExceptionHandler(SessionChangeStateRestException.class)
    public ResponseEntity<String> handle(HttpServletRequest httpServletRequest, Exception exception){
        return  ResponseEntity.badRequest().body(exception.getMessage());
    }
}
