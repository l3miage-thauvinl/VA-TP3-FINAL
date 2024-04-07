package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CandidateNotFoundRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import org.junit.jupiter.api.Test;
import org.mockito.exceptions.misusing.InvalidUseOfMatchersException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateServiceTest {

    @Autowired
    private CandidateService candidateService;

    @MockBean
    private CandidateComponent candidateComponent;

    // Test pour vérifier que la méthode getCandidateAverage retourne un double correctement.
    @Test
    void getCandidateAverageReturnDouble() throws CandidateNotFoundException {

        // Création d'une instance de candidat et d'un examen pour le test
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .id(1L)
                .email("")
                .build();
        ExamEntity examEntity = ExamEntity
                .builder()
                .weight(1)
                .build();
        CandidateEvaluationGridEntity candidateEvaluationGridEntity2 = CandidateEvaluationGridEntity
                .builder()
                .grade(15)
                .build();
        candidateEvaluationGridEntity2.setExamEntity(examEntity);
        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity2));

        // Simulation du comportement du composant candidateComponent : retourne le candidat créé
        when(candidateComponent.getCandidatById(anyLong())).thenReturn(candidateEntity);

        // Appel de la méthode à tester et vérification qu'aucune exception n'est lancée
        assertDoesNotThrow(()->candidateService.getCandidateAverage(1L));

    }

    // Test pour vérifier que la méthode getCandidateAverage lance une exception CandidateNotFoundRestException lorsqu'elle ne trouve pas le candidat.
    @Test
    void getCandidateAverageDontReturnDouble() throws CandidateNotFoundRestException, CandidateNotFoundException {

        // Simulation du comportement du composant candidateComponent : lance une exception CandidateNotFoundException
        when(candidateComponent.getCandidatById(anyLong())).thenThrow(CandidateNotFoundException.class);

        // Appel de la méthode à tester et vérification qu'une exception CandidateNotFoundRestException est lancée
        assertThrows(CandidateNotFoundRestException.class, ()->candidateService.getCandidateAverage(1L));
    }
}
