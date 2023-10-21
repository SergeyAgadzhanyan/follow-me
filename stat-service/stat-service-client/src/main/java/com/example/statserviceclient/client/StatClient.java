package com.example.statserviceclient.client;

import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component

public class StatClient extends BaseClient {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public StatClient(@Value("${stat.server.url}") String serverUrl) {
        super(new RestTemplateBuilder().uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl)).requestFactory(SimpleClientHttpRequestFactory::new).build());
    }

    public List<StatDtoGet> getStats(String path, Map<String, Object> parameters) {
        ResponseEntity<Object> response = get(path, parameters);
        try {
            String bodyString = objectMapper.writeValueAsString(response.getBody());
            return Arrays.asList(objectMapper.readValue(bodyString, StatDtoGet[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<Object> saveStat(String path, StatDtoCreate statDtoCreate) {
        return save(path, statDtoCreate);
    }
}
