package com.softserve.rms.repository;

import com.softserve.rms.entities.Role;
import org.hibernate.metamodel.model.convert.spi.JpaAttributeConverter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    @PreAuthorize("hasRole('ADMIN')")
    Role findByName(String name);
}
