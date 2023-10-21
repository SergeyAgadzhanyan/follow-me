package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LocationDto {
    @NotNull
    @Min(value = -90)
    @Max(value = 90)
    private Float lat;
    @NotNull
    @Min(value = -180)
    @Max(value = 180)
    private Float lon;
}
