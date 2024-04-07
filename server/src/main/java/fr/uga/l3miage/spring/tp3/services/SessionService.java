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

    /**
     * Cette méthode change l'état d'une session en fonction de certaines conditions et renvoie un ensemble de réponses d'examen.
     *
     * @param idSession L'identifiant de la session à modifier
     * @return Un ensemble de réponses d'examen après avoir changé l'état de la session
     * @throws SessionChangeStateRestException Si une erreur survient lors du changement d'état de la session
     */
    public Set<ExamResponse> changeState(Long idSession) {
        // Récupérer l'entité de la session à partir du composant de session
        EcosSessionEntity ecosSessionEntity = sessionComponent.getSessionById(idSession);

        // Vérifier si la session est terminée et si son état est EVAL_STARTED
        if (sessionTermine(ecosSessionEntity) && ecosSessionEntity.getStatus() == SessionStatus.EVAL_STARTED)
            // Changer l'état de la session en EVAL_ENDED
            ecosSessionEntity.setStatus(SessionStatus.EVAL_ENDED);
        else throw new SessionChangeStateRestException("On ne peut pas changer le status de cette session", ecosSessionEntity.getStatus());

        // Mapper les entités d'examen en réponses d'examen et les collecter dans un ensemble
        return ecosSessionEntity.getExamEntities()
                .stream()
                .map(examMapper::toResponse)
                .collect(Collectors.toSet());

    }

    /**
     * Cette méthode vérifie si la session est terminée en fonction de la date et l'heure actuelles.
     *
     * @param ecosSessionEntity L'entité de la session à vérifier
     * @return true si la session est terminée, sinon false
     */
    private boolean sessionTermine(EcosSessionEntity ecosSessionEntity) {
        // Initialiser un tableau avec un seul élément, qui contient la valeur true par défaut
        final boolean[] t = {true};
        // Parcourir les étapes de programmation de la session
        ecosSessionEntity.getEcosSessionProgrammationEntity().getEcosSessionProgrammationStepEntities().forEach(val -> {
            // Si la date et l'heure de l'étape sont après la date et l'heure actuelles, mettre la valeur du tableau à false
            if (val.getDateTime().isAfter(LocalDateTime.now()))
                t[0] = false;
        });
        // Renvoyer la valeur du tableau, qui indique si la session est terminée ou non
        return t[0];
    }

}
