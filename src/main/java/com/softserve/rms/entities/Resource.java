package com.softserve.rms.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    private Long id;

    private String name;

    private String description;

    private ResourceTemplate resourceTemplate;

    private User user;
    private HashMap<String, Object> parameters;

//    private List<ResourceParameter> resourceParameters;
//    private List<Object> parameters;
}
