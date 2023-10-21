package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCompilationRequest {
    private Set<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
