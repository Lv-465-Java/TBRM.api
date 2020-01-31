package com.softserve.rms.repository;


import com.softserve.rms.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findPersonByEmail(String email);
}
