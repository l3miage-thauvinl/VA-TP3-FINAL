package fr.uga.l3miage.spring.tp3.services;

import fr.uga.l3miage.spring.tp3.components.ExamComponent;
import fr.uga.l3miage.spring.tp3.components.SessionComponent;
import fr.uga.l3miage.spring.tp3.enums.SessionStatus;
import fr.uga.l3miage.spring.tp3.exceptions.handlers.SessionChangeStateExceptionHandler;
import fr.uga.l3miage.spring.tp3.exceptions.rest.CreationSessionRestException;
import fr.uga.l3miage.spring.tp3.exceptions.rest.SessionChangeStateRestException;
import fr.uga.l3miage.spring.tp3.exceptions.technical.ExamNotFoundException;
import fr.uga.l3miage.spring.tp3.mappers.ExamMapper;
import fr.uga.l3miage.spring.tp3.mappers.SessionMapper;
import fr.uga.l3miage.spring.tp3.models.EcosSessionEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationEntity;
import fr.uga.l3miage.spring.tp3.models.EcosSessionProgrammationStepEntity;
import fr.uga.l3miage.spring.tp3.models.ExamEntity;
import fr.uga.l3miage.spring.tp3.request.SessionCreationRequest;
import fr.uga.l3miage.spring.tp3.responses.ExamResponse;
import fr.uga.l3miage.spring.tp3.responses.SessionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionMapper sessionMapper;
    private final ExamMapper examMapper;
    private final ExamComponent examComponent;
    private final SessionComponent sessionComponent;

    public SessionResponse createSession(SessionCreationRequest sessionCreationRequest){
        try {
            EcosSessionEntity ecosSessionEntity = sessionMapper.toEntity(sessionCreationRequest);
            EcosSessionProgrammationEntity programmation = sessionMapper.toEntity(sessionCreationRequest.getEcosSessionProgrammation());
            Set<EcosSessionProgrammationStepEntity> stepEntities = sessionCreationRequest.getEcosSessionProgrammation()
                    .getSteps()
                    .stream()
                    .map(sessionMapper::toEntity)
                    .collect(Collectors.toSet());

            Set<ExamEntity> exams = examComponent.getAllById(sessionCreationRequest.getExamsId());

            ecosSessionEntity.setExamEntities(exams);
            programmation.setEcosSessionProgrammationStepEntities(stepEntities);
            ecosSessionEntity.setEcosSessionProgrammationEntity(programmation);

            ecosSessionEntity.setStatus(SessionStatus.CREATED);

            return sessionMapper.toResponse(sessionComponent.createSession(ecosSessionEntity));
        }catch (RuntimeException | ExamNotFoundException e){
            throw new CreationSessionRestException(e.getMessage());
        }
    }
/*
 Faire passer l'état d'une session de l'état `EVAL_STARTED` à `EVAL_ENDED`. (4h)
        - Vérification métier :
            - Vérifier que la dernière étape est déjà passée.
            - L'état précédent était bien `EVAL_STARTED`.
        - Code attendu :
            - 200 OK
            - 409 CONFLIT
        - Réponse :
            - Les copies des candidats
            - Si le cas est 409, un message comportant :
                - URI
                - Message d'erreur
                - État actuel de la session.
 */
    public Set<ExamResponse> changeState(Long idSession) {
        EcosSessionEntity ecosSessionEntity = sessionComponent.getSessionById(idSession);
        try{
            if(sessionTermine(ecosSessionEntity)&&ecosSessionEntity.getStatus()==SessionStatus.EVAL_STARTED)
                ecosSessionEntity.setStatus(SessionStatus.EVAL_ENDED);

            return ecosSessionEntity.getExamEntities()
                    .stream()
                    .map(examMapper::toResponse)
                    .collect(Collectors.toSet());

        }catch (Exception e){
            throw new SessionChangeStateRestException(e.getMessage(), ecosSessionEntity.getStatus());
        }


    }
    private boolean sessionTermine(EcosSessionEntity ecosSessionEntity){
        final boolean[] t = {true};
        ecosSessionEntity.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities().forEach(val-> {
            if (val.getDateTime().isAfter(LocalDateTime.now()))
                t[0] = false;
        });
        return t[0];
    }
}
