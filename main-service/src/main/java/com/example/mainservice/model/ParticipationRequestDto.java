package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data

public class ParticipationRequestDto {
    private Long id;
    private String created;
    private Long event;
    private Long requester;
    private String status;
}
