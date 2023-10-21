package com.example.mainservice.controller;

import com.example.mainservice.model.*;
import com.example.mainservice.service.CommentService;
import com.example.mainservice.service.EventService;
import com.example.mainservice.service.ParticipationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody
                                 @Valid NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    public Set<EventFullDto> getEventsByInitiatorId(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndInitiatorId(@PathVariable Long eventId, @PathVariable Long userId) {
        return eventService.getEventByIdAndInitiatorId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEventByIdAndInitiatorId(eventId, userId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestByEventIdAndInitiatorId(@PathVariable Long eventId,
                                                                                        @PathVariable Long userId) {
        return participationRequestService.getParticipationRequestByEventIdAndInitiatorId(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                                       @RequestBody @Valid EventRequestStatusUpdateRequest e) {
        return participationRequestService.updateStatus(userId, eventId, e);
    }

    @PostMapping(("/{eventId}/comment"))
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto addComment(@PathVariable Long userId,
                                 @PathVariable Long eventId,
                                 @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return commentService.addComment(eventId, userId, commentCreateDto);
    }

    @PatchMapping("/{eventId}/comment/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @PathVariable Long commentId,
                                    @RequestBody @Valid CommentCreateDto commentCreateDto) {
        return commentService.updateComment(userId, eventId, commentId, commentCreateDto);
    }

    @DeleteMapping("/{eventId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long eventId,
                              @PathVariable Long commentId) {
        commentService.deleteCommentByUser(userId, eventId, commentId);
    }

}
