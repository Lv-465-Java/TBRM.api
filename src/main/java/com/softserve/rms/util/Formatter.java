package com.softserve.rms.util;

import org.springframework.security.acls.model.Permission;
import org.springframework.security.acls.model.Sid;
import org.springframework.stereotype.Component;

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
}
