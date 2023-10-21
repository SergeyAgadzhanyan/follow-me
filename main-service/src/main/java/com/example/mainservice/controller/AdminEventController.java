package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.UpdateEventRequest;
import com.example.mainservice.service.CommentService;
import com.example.mainservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;
    private final CommentService commentService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEventById(eventId, updateEventRequest);
    }

    @GetMapping
    public Set<EventFullDto> getEventsByAmin(@RequestParam(required = false) List<Long> users,
                                             @RequestParam(required = false) List<String> states,
                                             @RequestParam(required = false) List<Long> categories,
                                             @RequestParam(required = false) String rangeStart,
                                             @RequestParam(required = false) String rangeEnd,
                                             @RequestParam(defaultValue = "0") int from,
                                             @RequestParam(defaultValue = "10") int size) {
        return eventService.getEventsByAmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @DeleteMapping("/{eventId}/comment/{commentId}")
    public void deleteComment(@PathVariable Long eventId,
                              @PathVariable Long commentId) {
        commentService.deleteCommentByAdmin(eventId, commentId);
    }
}
