package com.softserve.rms.entities;

import lombok.*;
import org.springframework.stereotype.Component;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Component
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}