package fr.uga.l3miage.spring.tp3.exceptions.technical;

import fr.uga.l3miage.spring.tp3.enums.SessionStatus;

public class SessionChangeStateException extends Exception{
    private SessionStatus sessionStatus;
    public SessionChangeStateException(String message, SessionStatus sessionStatus) {
        super(message);
        this.sessionStatus = sessionStatus;
    }
}
