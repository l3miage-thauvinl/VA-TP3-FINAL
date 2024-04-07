package fr.uga.l3miage.spring.tp3.repositories;

import fr.uga.l3miage.spring.tp3.enums.TestCenterCode;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.models.CandidateEvaluationGridEntity;
import fr.uga.l3miage.spring.tp3.models.TestCenterEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, properties = "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect")
public class CandidateRepositoryTest {

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private TestCenterRepository testCenterRepository;

    @Autowired
    private CandidateEvaluationGridRepository candidateEvaluationGridRepository;

    // Méthode exécutée avant chaque test pour nettoyer les données
    @BeforeEach
    public void clear() {
        candidateRepository.deleteAll();
        testCenterRepository.deleteAll();
        candidateRepository.deleteAll();
    }

    // Test pour vérifier que la méthode findAllByTestCenterEntityCode retourne l'entité CandidateEntity correcte pour un code de centre de test donné
    @Test
    void findAllByTestCenterEntityCodeReturnEntity(){
        // Création d'un centre de test et d'un candidat pour le test
        TestCenterCode c = TestCenterCode.GRE;
        TestCenterEntity testCenterEntity = TestCenterEntity
                .builder()
                .code(c)
                .build();
        testCenterRepository.save(testCenterEntity);

        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("")
                .testCenterEntity(testCenterEntity)
                .build();

        // Enregistrement du candidat dans le repository
        candidateRepository.save(candidateEntity);

        // Appel de la méthode findAllByTestCenterEntityCode avec le code du centre de test
        Set<CandidateEntity> reponses = candidateRepository.findAllByTestCenterEntityCode(c);

        // Vérification que l'entité retournée correspond à celle attendue
        assertThat(reponses).hasSize(1);
    }

    // Test pour vérifier que la méthode findAllByTestCenterEntityCode ne retourne pas d'entité CandidateEntity pour un code de centre de test inexistant
    @Test
    void findAllByTestCenterEntityCodeDontReturnEntity(){
        // Tentative de recherche avec un code de centre de test inexistant
        TestCenterCode c = TestCenterCode.GRE;
        Set<CandidateEntity> reponses = candidateRepository.findAllByTestCenterEntityCode(c);

        // Vérification que aucun résultat n'est retourné
        assertThat(reponses).hasSize(0);
    }
}
