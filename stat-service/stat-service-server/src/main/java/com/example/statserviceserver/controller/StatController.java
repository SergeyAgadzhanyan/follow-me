package com.example.statserviceserver.controller;

import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import com.example.statserviceserver.service.StatService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @GetMapping("/stats")
    List<StatDtoGet> getStat(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                             @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                             @RequestParam(required = false) List<String> uris,
                             @RequestParam(required = false) Boolean unique) {
        return statService.getStat(start, end, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    void addStat(@RequestBody @Valid StatDtoCreate statDtoCreate) {
        statService.addStat(statDtoCreate);
    }
}
