package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class EventRequestShort {
    private Long eventId;
    private Long count;
}