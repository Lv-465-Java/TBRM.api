package com.softserve.rms.util;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PermissionChecker {
    /**
     * Methods checks read permissions on input entity.
     *
     * @param item entity to check
     * @return optional of checked item
     * @author Artur Sydor
     */
    @PreAuthorize("hasPermission(#item, 'read') or hasRole('MANAGER')")
    public <T> Optional<T> checkReadPermission(T item) {
        return Optional.of(item);
    }
}