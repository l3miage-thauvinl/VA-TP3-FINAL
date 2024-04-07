package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.repositories.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@AutoConfigureTestDatabase
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ExamComponentTest {

    @Autowired
    private ExamComponent examComponent;

    @MockBean
    private ExamRepository examRepository;

    // Méthode pour nettoyer la base de données avant chaque test
    @BeforeEach
    void clear(){
        examRepository.deleteAll();
    }

    // Test pour vérifier le comportement lorsque l'examen avec l'identifiant donné n'est pas trouvé dans le repository.
    // Il vérifie que l'exception ExamNotFoundException est lancée.
    @Test
    void findAllByIdThrowException() {
        when(examRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ExamNotFoundException.class, ()->examComponent.getAllById(Set.of(1L)));
    }

    // Test pour vérifier le comportement lorsque tous les examens avec les identifiants donnés sont trouvés dans le repository.
    // Il vérifie qu'aucune exception n'est lancée.
    @Test
    void findAllByIdDontThrowException(){
        // Création d'une instance d'examen pour le test avec un identifiant spécifié
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(1L)
                .build();

        // Définition du comportement attendu du mock examRepository.findAllById()
        when(examRepository.findAllById(anySet())).thenReturn(List.of(examEntity));

        // Appel de la méthode examComponent.getAllById() avec un ensemble contenant l'identifiant de l'examen créé
        // Vérification que cette méthode ne lance pas d'exception
        assertDoesNotThrow(()->examComponent.getAllById(Set.of(1L)));
    }
}
