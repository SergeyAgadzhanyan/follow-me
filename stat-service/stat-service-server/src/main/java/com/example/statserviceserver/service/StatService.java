package com.example.statserviceserver.service;

import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import com.example.statserviceserver.exception.BadRequestException;
import com.example.statserviceserver.mapper.StatMapper;
import com.example.statserviceserver.storage.StatRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class StatService {
    private final StatRepository statRepository;
    private final StatMapper statMapper;

    public List<StatDtoGet> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (!end.isAfter(start)) throw new BadRequestException();
        if ((uris == null || uris.isEmpty()) && (unique == null || !unique))
            return statRepository.findByDate(start, end);
        if (uris == null || uris.isEmpty()) return statRepository.findByUniqAndDate(start, end);
        if (unique == null || !unique) return statRepository.findByDateAndUri(start, end, uris);
        return statRepository.findByUniqAndDateAndUri(start, end, uris);
    }

    public void addStat(StatDtoCreate statDtoCreate) {
        statRepository.save(statMapper.mapFromDto(statDtoCreate));
    }

}
