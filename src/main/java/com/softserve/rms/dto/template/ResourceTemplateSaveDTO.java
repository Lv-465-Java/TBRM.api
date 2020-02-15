package com.softserve.rms.dto.template;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceTemplateSaveDTO {
    private String name;
    private String description;
}