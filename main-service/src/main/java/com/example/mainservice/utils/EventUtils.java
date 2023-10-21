package com.example.mainservice.utils;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.exception.Messages;
import com.example.mainservice.model.Category;
import com.example.mainservice.model.Event;
import com.example.mainservice.model.Location;
import com.example.mainservice.model.UpdateEventRequest;

import java.time.format.DateTimeFormatter;


public class EventUtils {
    public static final DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event update(Event e, UpdateEventRequest m, Category category) {
        StatEnum stat = e.getState();
        if (m.getStateAction() != null) {
            switch (m.getStateAction()) {
                case PUBLISH_EVENT:
                    if (stat == StatEnum.CANCELED) throw new ConflictException(Messages.DB_CONFLICT.getMessage());
                    stat = StatEnum.PUBLISHED;
                    break;
                case SEND_TO_REVIEW:
                    stat = StatEnum.PENDING;
                    break;
                default:
                    stat = StatEnum.CANCELED;
            }
        }

        return Event.builder()
                .id(e.getId())
                .title(m.getTitle() == null || m.getTitle().isBlank() ? e.getTitle() : m.getTitle())
                .location(m.getLocation() == null ? e.getLocation() : new Location(m.getLocation().getLat(), m.getLocation().getLon()))
                .annotation(m.getAnnotation() == null || m.getAnnotation().isBlank() ? e.getAnnotation() : m.getAnnotation())
                .createdOn(e.getCreatedOn())
                .category(category == null ? e.getCategory() : category)
                .eventDate(m.getEventDate() == null ? e.getEventDate() : m.getEventDate())
                .description(m.getDescription() == null || m.getDescription().isBlank() ? e.getDescription() : m.getDescription())
                .initiator(e.getInitiator())
                .paid(m.getPaid() == null ? e.getPaid() : m.getPaid())
                .participantLimit(m.getParticipantLimit() == null ? e.getParticipantLimit() : m.getParticipantLimit())
                .publishedOn(e.getPublishedOn())
                .requestModeration(m.getRequestModeration() == null ? e.getRequestModeration() : m.getRequestModeration())
                .state(stat)
                .build();
    }


}
