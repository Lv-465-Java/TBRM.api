package com.softserve.rms.util;

import com.softserve.rms.dto.template.ResourceTemplateDTO;
import com.softserve.rms.entities.ResourceTemplate;

public class Validator {

    public static final String COORDINATES_PATTERN = "^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)$";

    /**
     * Method generates String for {@link ResourceTemplate} table name field.
     *
     * @param name of {@link ResourceTemplateDTO}
     * @return {@link ResourceTemplateDTO}
     * @author Halyna Yatseniuk
     */

    public String generateTableOrColumnName(String name) {
        return name.toLowerCase().replaceAll("[-!$%^&*()/_+|~=`\\[\\]{}:\";'<>?,. ]", "_");
    }
}