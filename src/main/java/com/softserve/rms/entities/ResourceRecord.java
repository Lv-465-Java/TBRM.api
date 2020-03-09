package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResourceRecord {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

//    private ResourceTemplate resourceTemplate;

    private User user;

    private String photosNames;

    private Map<String, Object> parameters;


//    private List<ResourceParameter> resourceParameters;
//    private List<Object> parameters;
}
