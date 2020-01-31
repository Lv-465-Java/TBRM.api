package com.softserve.rms.validator;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;

public class ResourceTemplateAndParameterValidator {

    /**
     * Method generates String for {@link ResourceTemplate} table name field.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */

    public String generateTableOrColumnName(String name) {
        return name.toLowerCase().replaceAll("[-!$%^&*()_+|~=`\\[\\]{}:\";'<>?,. ]", "_");
    }
}