package com.example.mainservice.service;

import com.example.mainservice.exception.Messages;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CompilationMapper;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CompilationRepository;
import com.example.mainservice.storage.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final EventService eventService;

    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {

        Set<Event> events = (newCompilationDto.getEvents() == null) ? Set.of() :
                new LinkedHashSet<>(eventRepository.findAllById(newCompilationDto.getEvents()));
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        Compilation c = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(c, getEventsShortDto(c));
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        compilationRepository.deleteById(compId);
    }

    public List<CompilationDto> getCompilation(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(pageable).getContent() :
                compilationRepository.findAllByPinned(pinned, pageable);
        return compilations.stream()
                .map(c -> compilationMapper.toCompilationDto(c, getEventsShortDto(c))).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        return compilationMapper.toCompilationDto(compilation, getEventsShortDto(compilation));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(() -> new NotFoundException(Messages.RESOURCE_NOT_FOUND.getMessage()));
        Set<Event> events = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            events = new LinkedHashSet<>(eventRepository.findAllById(updateCompilationRequest.getEvents()));
        }
        Compilation updatedComp = compilationMapper.toCompilationFromUpdateDto(compilation, updateCompilationRequest, events);
        Compilation savedCompilation = compilationRepository.save(updatedComp);
        return compilationMapper.toCompilationDto(savedCompilation, getEventsShortDto(savedCompilation));
    }

    private List<EventShortDto> getEventsShortDto(Compilation compilation) {
        Set<Event> events = compilation.getEvents();
        return events.stream()
                .map(e -> eventMapper.mapToShortDtoFromEvent(e, null, null))
                .collect(Collectors.toList());
    }
}
