package com.example.mainservice.service;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.BadRequestException;
import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.exception.Messages;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CommentMapper;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.*;
import com.example.mainservice.utils.EventUtils;
import com.example.statserviceclient.client.StatClient;
import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.mainservice.utils.EventUtils.DATE_TME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private final ParticipationRequestRepository requestRepository;

    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (!checkUpdatedData(newEventDto.getEventDate(), false))
            throw new BadRequestException(Messages.BAD_REQUEST.getMessage());
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        Event event = eventRepository.save(eventMapper.mapToEvent(user, category, LocalDateTime.now(), newEventDto));
        return eventMapper.mapToDto(event, 0L, 0L);
    }

    public Set<EventFullDto> getEventByInitiatorId(Long id, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        Set<Event> events = new LinkedHashSet<>(eventRepository.findByInitiatorId(id, pageable));
        return getEventsDto(events);
    }

    public EventFullDto getEventByIdAndInitiatorId(Long evenId, Long userId) {
        Event event = eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        return getEventDto(event);
    }

    public EventFullDto updateEventByIdAndInitiatorId(Long evenId, Long userId, UpdateEventRequest updateEventRequest) {
        if (!checkUpdatedData(updateEventRequest.getEventDate(), false))
            throw new BadRequestException(Messages.BAD_REQUEST.getMessage());
        Event event = eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        if (event.getState() == StatEnum.PUBLISHED)
            throw new ConflictException(Messages.DB_CONFLICT.getMessage());
        return updateEvent(event, updateEventRequest);
    }

    public EventFullDto updateEventById(Long eventId, UpdateEventRequest updateEventRequest) {
        if (!checkUpdatedData(updateEventRequest.getEventDate(), true))
            throw new BadRequestException(Messages.BAD_REQUEST.getMessage());
        Event event = eventRepository.findById(eventId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        if (updateEventRequest.getStateAction() != null && event.getState() == StatEnum.PUBLISHED)
            throw new ConflictException(Messages.DB_CONFLICT.getMessage());
        return updateEvent(event, updateEventRequest);
    }

    public Set<EventFullDto> getEventsByAmin(List<Long> users, List<String> states,
                                             List<Long> categories, String rangeStart,
                                             String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        List<StatEnum> statEnums = null;
        if (states != null && !states.isEmpty())
            statEnums = states.stream().map(StatEnum::valueOf).collect(Collectors.toList());
        Set<Event> events = new LinkedHashSet<>(eventRepository.findByAdmin(users, statEnums, categories,
                rangeStart == null ? LocalDateTime.now() :
                        LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                rangeEnd == null ? null :
                        LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), pageable));
        return getEventsDto(events);

    }

    public Set<EventFullDto> getEventsByFilter(String text, List<Long> categories, Boolean paid, String rangeStart,
                                               String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                               HttpServletRequest httpServletRequest) {
        saveState(httpServletRequest);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sort == null || sort.equals("EVENT_DATE") ? "createdOn" : "views").descending());
        Set<Event> events = new LinkedHashSet<>(eventRepository.findByFilters(text, categories, paid,
                rangeStart == null ? LocalDateTime.now().plusSeconds(1) :
                        LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                rangeEnd == null ? null :
                        LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), onlyAvailable, pageable));
        if (events.isEmpty()) throw new BadRequestException(Messages.BAD_REQUEST.getMessage());

        return getEventsDto(events);
    }

    public EventFullDto getEventById(Long eventId, HttpServletRequest httpServletRequest) {
        saveState(httpServletRequest);
        Event event = eventRepository.findByIdAndState(eventId, StatEnum.PUBLISHED).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        return getEventDto(event);
    }

    private void saveState(HttpServletRequest httpServletRequest) {
        statClient.saveStat("/hit", new StatDtoCreate("main-service", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
    }

    private List<ParticipationRequest> getRequests(Set<Event> events) {
        return requestRepository.findByEventIdInAndStatus(events.stream().map(Event::getId).collect(Collectors.toList()), RequestStatus.CONFIRMED);
    }

    private List<StatDtoGet> getStats(Set<Event> events) {
        return statClient.getStats("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                Map.of("start", getDateStart(events).format(DATE_TME_FORMATTER),
                        "end", LocalDateTime.now().plusSeconds(1).format(DATE_TME_FORMATTER),
                        "uris", events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.joining(",")),
                        "unique", "true"));
    }

    private Map<String, Long> getHitsMap(Set<Event> events) {
        List<StatDtoGet> stats = getStats(events);
        return stats.stream().collect(Collectors.toMap(s -> s.getUri().replace("/events/", ""), StatDtoGet::getHits));
    }

    public Set<EventFullDto> getEventsDto(Set<Event> events) {
        List<Long> eventIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        List<EventRequestShort> requests = requestRepository.findShortByIdsAndStatus(eventIds, RequestStatus.CONFIRMED);
        Map<Long, Long> requestMap = requests
                .stream()
                .collect(Collectors.toMap(EventRequestShort::getEventId, EventRequestShort::getCount));
        Map<String, Long> hits = getHitsMap(events);
        return events.stream().map(e -> eventMapper.mapToDto(e,
                requestMap.get(e.getId()),
                hits.get(e.getId().toString()))).collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public List<CommentDto> getCommentsByEventId(Long eventId, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return commentRepository.findAllByEventId(eventId, pageable).stream()
                .map(commentMapper::mapToCommentDto).collect(Collectors.toList());
    }

    private EventFullDto getEventDto(Event event) {
        Long requests = (long) getRequests(Set.of(event)).size();
        List<StatDtoGet> stats = getStats(Set.of(event));
        Long views = stats.isEmpty() ? 0L : stats.get(0).getHits();
        return eventMapper.mapToDto(event, requests, views);
    }

    private EventFullDto updateEvent(Event event, UpdateEventRequest updateEventUserRequest) {
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        }
        Event updatedEvent = eventRepository.save(EventUtils.update(event, updateEventUserRequest, category));
        return getEventDto(updatedEvent);
    }

    private Boolean checkUpdatedData(LocalDateTime data, Boolean isAdmin) {
        if (data == null) return true;
        return (data.isAfter(LocalDateTime.now().plusHours(isAdmin ? 1 : 2)));
    }

    private LocalDateTime getDateStart(Set<Event> events) {
        return events.stream().map(Event::getPublishedOn).min(LocalDateTime::compareTo).orElseThrow(() -> new BadRequestException(Messages.BAD_REQUEST.getMessage()));
    }
}
