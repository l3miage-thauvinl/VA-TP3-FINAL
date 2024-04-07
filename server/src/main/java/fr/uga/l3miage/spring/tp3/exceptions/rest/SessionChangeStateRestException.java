package fr.uga.l3miage.spring.tp3.exceptions.rest;


import fr.uga.l3miage.spring.tp3.enums.SessionStatus;

public class SessionChangeStateRestException extends RuntimeException{
    private SessionStatus sessionStatus;
    public SessionChangeStateRestException(String message, SessionStatus sessionStatus) {
        super(message);
        this.sessionStatus = sessionStatus;
    }
}
