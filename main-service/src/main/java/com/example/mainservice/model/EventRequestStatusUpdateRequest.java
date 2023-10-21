package com.example.mainservice.model;

import com.example.mainservice.enums.RequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Set;

@AllArgsConstructor
@Data
public class EventRequestStatusUpdateRequest {
    @NotNull
    private Set<Long> requestIds;
    @NotNull
    private RequestStatus status;
}