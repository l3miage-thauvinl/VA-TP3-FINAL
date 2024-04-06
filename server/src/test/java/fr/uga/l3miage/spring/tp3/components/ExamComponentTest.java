package fr.uga.l3miage.spring.tp3.components;

import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
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
    @BeforeEach
    void clear(){
        examRepository.deleteAll();
    }
    @Test
    void findAllByIdThrowException() {
        when(examRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ExamNotFoundException.class, ()->examComponent.getAllById(Set.of(1L)));
    }
    @Test
    void findAllByIdDontThrowException(){
        ExamEntity examEntity = ExamEntity
                .builder()
                .id(1L)
                .build();
        when(examRepository.findAllById(anySet())).thenReturn(List.of(examEntity));
        assertDoesNotThrow(()->examComponent.getAllById(Set.of(1L)));
    }
}
