package com.softserve.rms.repository;


import com.softserve.rms.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Person, Long> {
    Person findPersonByEmail(String email);
}
