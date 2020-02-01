package com.softserve.rms.util;

import org.springframework.stereotype.Component;

/**
 * This Class is used to convert string permission
 * to integer mask.
 */
@Component
public class PermissionMapper {
    /**
     * Method converts string permission
     * to integer mask.
     *
     * @param permission in string form
     * @return integer mask
     */
    public Integer getMask(String permission) {
        Integer mask = 0;
        if (permission.equalsIgnoreCase("read")) {
            mask = 1;
        } else if (permission.equalsIgnoreCase("write")) {
            mask = 2;
        }
        return mask;
    }
}