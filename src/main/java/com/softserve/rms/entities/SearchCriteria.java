package com.softserve.rms.entities;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
}