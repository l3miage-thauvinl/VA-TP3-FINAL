package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
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
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class SessionComponentTest {
    @Autowired
    private SessionComponent sessionComponent;
    //@MockBean
    //private EcosSessionProgrammationComponent

    @MockBean
    private EcosSessionRepository ecosSessionRepository;
    @MockBean
    private EcosSessionProgrammationRepository ecosSessionProgrammationRepository;
    @MockBean
    private EcosSessionProgrammationStepRepository ecosSessionProgrammationStepRepository;

    @Test
    void createSessionReturnEntity(){
        EcosSessionEntity ecosSessionEntity = EcosSessionEntity
                .builder()
                .build();
        EcosSessionProgrammationEntity ecosSessionProgrammationEntity = EcosSessionProgrammationEntity
                .builder().build();
        ecosSessionEntity.setEcosSessionProgrammationEntity(ecosSessionProgrammationEntity);

        when(ecosSessionRepository.save(any(EcosSessionEntity.class))).thenReturn(ecosSessionEntity);
        EcosSessionEntity rep = sessionComponent.createSession(ecosSessionEntity);
        assertThat(rep).isEqualTo(ecosSessionEntity);
    }
}
