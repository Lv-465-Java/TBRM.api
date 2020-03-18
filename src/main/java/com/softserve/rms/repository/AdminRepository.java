package com.softserve.rms.repository;

import com.softserve.rms.entities.Role;
import com.softserve.rms.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<User, Long> {

    @Modifying
    @Query("update User u set u.role = ?1 , u.enabled = true where u.id = ?2")
    void updateUserRoleById(Role role, Long id);

    @Modifying
    @Query("update User u set u.role = 5, u.enabled = false where u.id = ?1")
    void deleteRoleById(Long id);

    Page<User> getAllByEnabled(boolean enabled, Pageable pageable);

}
