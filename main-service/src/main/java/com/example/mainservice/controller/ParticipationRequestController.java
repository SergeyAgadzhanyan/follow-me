package com.example.mainservice.controller;

import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.service.ParticipationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addParticipationRequest(@PathVariable Long userId, @RequestParam Long eventId) {
        return participationRequestService.addParticipationRequest(userId, eventId);
    }

    @GetMapping()
    List<ParticipationRequestDto> getParticipationRequest(@PathVariable Long userId) {
        return participationRequestService.getRequestByRequesterId(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    ParticipationRequestDto cancelRequest(@PathVariable Long userId, @PathVariable Long requestId) {
        return participationRequestService.cancelRequest(userId, requestId);
    }
}
