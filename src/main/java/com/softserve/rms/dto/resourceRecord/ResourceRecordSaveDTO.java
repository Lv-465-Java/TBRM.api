package com.softserve.rms.dto.resourceRecord;

import com.softserve.rms.constants.ValidationErrorConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceRecordSaveDTO implements Serializable {

    @NotEmpty(message = ValidationErrorConstants.EMPTY_RESOURCE_RECORD_NAME)
    private String name;
    private String description;
    private Long userId;
    private Map<String, Object> parameters;
}
