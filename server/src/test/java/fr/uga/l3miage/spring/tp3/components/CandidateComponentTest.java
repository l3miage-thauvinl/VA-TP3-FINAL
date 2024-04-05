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
    /*
        public CandidateEntity getCandidatById(Long id)
     */
    @Test
    void getCandidateByIdThrowException(){
        //given
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.empty());
        //when-then
        assertThrows(CandidateNotFoundException.class, ()->candidateComponent.getCandidatById(1L));
    }
    @Test
    void getCandidateByIdDontThrow(){
        //given
        CandidateEntity candidateEntity = CandidateEntity
                .builder()
                .email("")
                .build();
        when(candidateRepository.findById(anyLong())).thenReturn(Optional.of(candidateEntity));
        //when-then
        assertDoesNotThrow(()->candidateComponent.getCandidatById(1L));
    }
}
