package com.softserve.rms.dto.search;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class SearchCriteriaDTO {
    private String key;
    private Object value;
//    private Object entityName;
}