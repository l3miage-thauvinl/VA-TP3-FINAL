package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionProgrammationStepRepository;
import fr.uga.l3miage.spring.tp3.repositories.EcosSessionRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {

    @Autowired
    private SessionComponent sessionComponent;

    @MockBean
    private EcosSessionRepository ecosSessionRepository;

    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;

    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    // Test pour vérifier que la méthode createSession retourne l'entité EcosSessionEntity correcte.
    @Test
    void createSessionReturnEntity(){
        // Création d'une entité EcosSessionEntity et d'une entité EcosSessionProgrammationEntity pour le test
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .build();
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = EcosSessionProgrammationEntity
                .builder().build();
        ecosSessionEntity.setEcosSessionProgrammationEntity(ecosSessionProgrammationEntity);

        // Définition du comportement attendu du mock ecosSessionRepository.save()
        when(ecosSessionRepository.save(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);

        // Appel de la méthode createSession du component sous test
        EcosSessionEntity rep = sessionComponent.createSession(ecosSessionEntity);

        // Vérification que l'entité retournée par la méthode createSession correspond à celle attendue
        assertThat(rep).isEqualTo(ecosSessionEntity);
    }

    // Test pour vérifier que la méthode getSessionById retourne l'entité EcosSessionEntity correcte.
    @Test
    void getSessionByIdReturnEntity(){
        // Création d'une entité de session pour le test
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .id(1L)
                .build();
        // Création d'une entité de programmation de session pour l'entité de session
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = EcosSessionProgrammationEntity
                .builder().build();
        // Configuration de l'entité de session avec l'entité de programmation de session
        ecosSessionEntity.setEcosSessionProgrammationEntity(ecosSessionProgrammationEntity);

        // Définition du comportement attendu du mock ecosSessionRepository.findById()
        when(ecosSessionRepository.findById(anyLong())).thenReturn(Optional.of(ecosSessionEntity));

        // Appel de la méthode à tester avec l'identifiant de session spécifié
        EcosSessionEntity rep = sessionComponent.getSessionById(1L);

        // Vérification que l'entité retournée correspond à celle attendue
        assertThat(rep).isEqualTo(ecosSessionEntity);
    }


}
