package fr.uga.l3miage.spring.tp3.controllers;

import fr.uga.l3miage.spring.tp3.components.CandidateComponent;
import fr.uga.l3miage.spring.tp3.controller.CandidateController;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import javassist.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.HashMap;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@AutoConfigureTestDatabase
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CandidateComponent candidateComponent;

    // Test pour vérifier que la méthode getCandidateAverage ne lance pas d'exception
    @Test
    void getCandidateAverageDontThrow(){
        final HttpHeaders headers = new HttpHeaders();
        final HashMap<String,Long> urlParam = new HashMap<>();

        // Création d'un candidat et d'un examen pour le test
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
        candidateRepository.save(candidateEntity);

        urlParam.put("idCandidate", 1L);

        // Appel de l'API pour obtenir la moyenne du candidat
        ResponseEntity<Double> rep = testRestTemplate.exchange("/api/candidates/{idCandidate}/average", HttpMethod.GET, new HttpEntity<>(null, headers), Double.class,urlParam);

        // Vérification que le code de statut de la réponse est OK (200)
        assertThat(rep.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    // Test pour vérifier que la méthode getCandidateAverage lance une exception NotFoundException
    @Test
    void getCandidateAverageThrow(){
        final HttpHeaders headers = new HttpHeaders();
        final HashMap<String, Object> urlParam = new HashMap<>();
        urlParam.put("idCandidate", "Le candidat n'a pas été trouvé");

        // Appel de l'API pour obtenir la moyenne du candidat avec un ID non valide
        ResponseEntity<NotFoundException> rep = testRestTemplate.exchange("/api/candidates/{idCandidate}", HttpMethod.GET, new HttpEntity<>(null, headers),NotFoundException.class, urlParam);

        // Vérification que le code de statut de la réponse est NOT_FOUND (404)
        assertThat(rep.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
