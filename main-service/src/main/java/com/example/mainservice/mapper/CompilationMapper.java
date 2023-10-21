package com.example.mainservice.mapper;

import com.example.mainservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;

    public Compilation toCompilation(NewCompilationDto newCompilationDto, Set<Event> events) {
        return new Compilation(null, events,
                newCompilationDto.isPinned(),
                newCompilationDto.getTitle());
    }

    public CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> events) {
        return new CompilationDto(
                events,
                compilation.getId(), compilation.getPinned(), compilation.getTitle());
    }

    public Compilation toCompilationFromUpdateDto(Compilation compilation, UpdateCompilationRequest updateDto,
                                                  Set<Event> events) {
        return new Compilation(compilation.getId(), events,
                updateDto.getPinned() == null ? compilation.getPinned() : updateDto.getPinned(),
                updateDto.getTitle() == null || updateDto.getTitle().isBlank() ? compilation.getTitle() : updateDto.getTitle());
    }
}
