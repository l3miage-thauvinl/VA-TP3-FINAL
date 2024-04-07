package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationCreationRequest;
import fr.uga.l3miage.spring.tp3.request.SessionProgrammationStepCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionServiceTest {
    @Autowired
    private SessionService sessionService;
    @MockBean
    private SessionComponent sessionComponent;
    @MockBean
    private ExamComponent examComponent;
    @SpyBean
    private SessionMapper sessionMapper;

    // Test pour vérifier que la méthode createSession ne lance pas d'exception.
    @Test
    void createSessionDontThrow() throws ExamNotFoundException {

        // Création d'objets de requête et d'entités pour le test
        SessionCreationRequest sessionCreationRequest = SessionCreationRequest
                .builder().build();
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder().build();
        SessionProgrammationCreationRequest sessionProgrammationCreationRequest = SessionProgrammationCreationRequest
                .builder().build();
        SessionProgrammationStepCreationRequest sessionProgrammationStepCreationRequest = SessionProgrammationStepCreationRequest
                .builder().build();
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(1L)
                .build();

        sessionProgrammationCreationRequest.setSteps(Set.of(sessionProgrammationStepCreationRequest));
        sessionCreationRequest.setExamsId(Set.of(examEntity.getId()));
        sessionCreationRequest.setEcosSessionProgrammation(sessionProgrammationCreationRequest);

        // Définition du comportement attendu des mocks
        when(sessionComponent.createSession(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        when(examComponent.getAllById(Set.of(anyLong()))).thenReturn(Set.of(examEntity));

        // Appel de la méthode à tester et vérification des résultats
        SessionResponse sessionResponseExpected = sessionMapper.toResponse(ecosSessionEntity);
        SessionResponse sessionResponseActual = sessionService.createSession(sessionCreationRequest);
        assertThat(sessionResponseActual).isEqualTo(sessionResponseExpected);
    }

    // Test pour vérifier que la méthode createSession lance une exception CreationSessionRestException lorsqu'une exception ExamNotFoundException est lancée.
    @Test
    void createSessionThrowCreateSessionRestException() throws ExamNotFoundException {

        // Définition du comportement attendu du mock examComponent.getAllById()
        when(examComponent.getAllById(Set.of(anyLong()))).thenThrow(ExamNotFoundException.class);

        // Appel de la méthode à tester et vérification qu'une exception CreationSessionRestException est lancée
        assertThrows(CreationSessionRestException.class, () -> sessionService.createSession(any(SessionCreationRequest.class)));
    }
}
