package com.softserve.rms.util;

import com.softserve.rms.entities.ResourceTemplate;
import org.springframework.stereotype.Component;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.Permission;

import java.util.List;

/**
 * Class for formatting string
 *
 * @author Marian Dutchyn
 */
@Component
public class Formatter {

    /**
     * Method returns formatted Sid name
     *
     * @param name full name of {@link Sid}
     * @return username or role
     */
    public String sidFormatter(String name) {
        String result;
        result = name.startsWith("Grant")
                ? name.substring(20, name.length() - 1) : name.substring(13, name.length() - 1);
        return result;
    }

    /**
     * Method returns formatted permission name
     *
     * @param name full name of {@link Permission}
     * @return permisson
     */
    public String permissionFormatter(String name) {
        String result;
        result = name.substring(31).equals("R") ? "READ" : "WRITE";
        return result;
    }

    /**
     * Method returns formatted error message string
     *
     * @param list of {@link ResourceTemplate}
     * @return String of error
     * @author Halyna Yatseniuk
     */
    public String errorMessageFormatter(List<ResourceTemplate> list) {
        StringBuilder stringBuilder = new StringBuilder();
        for (ResourceTemplate template : list) {
            stringBuilder.append(template.getName().concat(", "));
        }
        int length = stringBuilder.length();
        return stringBuilder.delete(length - 2, length).toString();
    }
}