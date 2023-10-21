package com.example.mainservice.mapper;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.model.Event;
import com.example.mainservice.model.ParticipationRequest;
import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.mainservice.utils.EventUtils.DATE_TME_FORMATTER;

@Component
public class ParticipationRequestMapper {
    public ParticipationRequest mapToParticipationRequest(User user, Event event) {
        return new ParticipationRequest(null, LocalDateTime.now(), event, user,
                event.getParticipantLimit() == 0 || !event.getRequestModeration() ? RequestStatus.CONFIRMED : RequestStatus.PENDING);
    }

    public ParticipationRequestDto mapToParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(participationRequest.getId(),
                participationRequest.getCreated().format(DATE_TME_FORMATTER),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus().toString());
    }
}
