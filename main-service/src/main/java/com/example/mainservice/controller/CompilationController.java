package com.example.mainservice.controller;

import com.example.mainservice.model.CompilationDto;
import com.example.mainservice.service.CompilationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/compilations")
public class CompilationController {
    private final CompilationService compilationService;

    @GetMapping()
    public List<CompilationDto> getCompilation(@RequestParam(required = false) Boolean pinned,
                                               @RequestParam(defaultValue = "0") int from,
                                               @RequestParam(defaultValue = "10") int size) {
        return compilationService.getCompilation(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getCompilationById(@PathVariable Long compId) {
        return compilationService.getCompilationById(compId);
    }
}
