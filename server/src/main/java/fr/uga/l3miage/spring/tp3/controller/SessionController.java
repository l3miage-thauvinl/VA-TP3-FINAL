package fr.uga.l3miage.spring.tp3.controller;

import fr.uga.l3miage.spring.tp3.endpoints.SessionEndpoints;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.ExamResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import fr.uga.l3miage.spring.tp3.services.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@Controller
@RestController
@RequiredArgsConstructor
public class SessionController implements SessionEndpoints {
    private final SessionService sessionService;


    @ResponseBody
    @Override
    public SessionResponse createSession(SessionCreationRequest request) {
        return sessionService.createSession(request);
    }

    @Override
    public Set<ExamResponse> changeState(Long idSession) {
        return sessionService.changeState(idSession);
    }
}
