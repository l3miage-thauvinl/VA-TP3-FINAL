package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.controller.SessionController;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class SessionControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private SessionController sessionController;
    @SpyBean
    private SessionComponent sessionComponent;
    @SpyBean
    private ExamComponent examComponent;

    /*public SessionResponse createSession(SessionCreationRequest request) */
    @Test
    void createSessionDontThrowException(){
        final HttpHeaders headers = new HttpHeaders();

        // Création d'objets de requête et d'entités pour le test
        SessionCreationRequest request = SessionCreationRequest
                .builder()
                .examsId(Set.of())
                .build();

        ResponseEntity<SessionResponse> rep = testRestTemplate.exchange("/api/sessions/create", HttpMethod.POST, new HttpEntity<>(request,headers), SessionResponse.class);
        assertThat(rep.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }
}













