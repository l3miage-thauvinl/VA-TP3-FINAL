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
    @BeforeEach
    public void clear() {
        candidateRepository.deleteAll();
        testCenterRepository.deleteAll();
        candidateRepository.deleteAll();
    }
    /*
    Fonctions à tester :

    Set<CandidateEntity> findAllByTestCenterEntityCode(TestCenterCode code);

     */
    @Test
    void findAllByTestCenterEntityCodeReturnEntity(){
        //given
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

        //when
        candidateRepository.save(candidateEntity);
        Set<CandidateEntity> reponses = candidateRepository.findAllByTestCenterEntityCode(c);

        //then
        assertThat(reponses).hasSize(1);
    }
    @Test
    void findAllByTestCenterEntityCodeDontReturnEntity(){
        //given
        TestCenterCode c = TestCenterCode.GRE;
        Set<CandidateEntity> reponses = candidateRepository.findAllByTestCenterEntityCode(c);
        //then
        assertThat(reponses).hasSize(0);
    }

    /*
    Set<CandidateEntity> findAllByCandidateEvaluationGridEntitiesGradeLessThan(double grade);
     */
    @Test
    void findAllByCandidateEvalReturnEntity(){
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .firstname("jean")
                .email("test@gmail.com")
                .build();
        candidateRepository.save(candidateEntity);

        CandidateEvaluationGridEntity candidateEvaluationGridEntity = CandidateEvaluationGridEntity
                .builder()
                .grade(10)
                .candidateEntity(candidateEntity)
                .build();
        candidateEvaluationGridRepository.save(candidateEvaluationGridEntity);

        candidateEntity.setCandidateEvaluationGridEntities(Set.of(candidateEvaluationGridEntity));
        //when
        candidateRepository.save(candidateEntity);
        Set<CandidateEntity> reponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(12);

        //then
        //assertThat(rep).hasSize(1);
        assertThat(reponses).hasSize(1);
    }
    @Test
    void findAllByCandidateEvalDontReturnEntity(){
        //given
        Set<CandidateEntity> reponses = candidateRepository.findAllByCandidateEvaluationGridEntitiesGradeLessThan(12);

        //then
        assertThat(reponses).hasSize(0);
    }
    /*
    Set<CandidateEntity> findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate localDate);
     */
    @Test
    void findAllByHasExtraTimeFalseAndBirthDateBeforeReturnEntity(){
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("")
                .hasExtraTime(false)
                .birthDate(LocalDate.of(2000, 1, 12))
                .build();

        //when
        candidateRepository.save(candidateEntity);
        Set<CandidateEntity> reponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2000,4,5));

        //then
        assertThat(reponses).hasSize(1);
    }
    @Test
    void findAllByHasExtraTimeFalseAndBirthDateBeforeDontReturnEntity(){
        //given
        Set<CandidateEntity> reponses = candidateRepository.findAllByHasExtraTimeFalseAndBirthDateBefore(LocalDate.of(2024, 04, 05));
        //then
        assertThat(reponses).hasSize(0);
    }

}
