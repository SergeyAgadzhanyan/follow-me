package com.example.statservicedto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class StatDtoGet {
    private String app;
    private String uri;
    private Long hits;
}
