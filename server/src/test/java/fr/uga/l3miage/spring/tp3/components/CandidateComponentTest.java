package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.CandidateNotFoundException;
import fr.uga.l3miage.spring.tp3.models.CandidateEntity;
import fr.uga.l3miage.spring.tp3.repositories.CandidateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.swing.text.html.Option;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class CandidateComponentTest {

    @Autowired
    private CandidateComponent candidateComponent;

    @MockBean
    private CandidateRepository candidateRepository;

    // Test pour vérifier le comportement lorsque le candidat n'est pas trouvé dans le repository.
    // Il vérifie que l'exception CandidateNotFoundException est lancée.
    @Test
    void getCandidateByIdThrowException(){

        // Simulation du comportement du repository : retourne une Optional vide
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Appel de la méthode à tester et vérification qu'une exception est lancée
        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getCandidatById(1L));
    }

    // Test pour vérifier le comportement lorsque le candidat est trouvé dans le repository.
    // Il vérifie qu'aucune exception n'est lancée.
    @Test
    void getCandidateByIdDontThrow(){

        // Création d'une instance de candidat
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("")
                .build();

        // Simulation du comportement du repository : retourne une Optional contenant le candidat créé
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));

        // Appel de la méthode à tester et vérification qu'aucune exception n'est lancée
        assertDoesNotThrow(()->candidateComponent.getCandidatById(1L));
    }
}
