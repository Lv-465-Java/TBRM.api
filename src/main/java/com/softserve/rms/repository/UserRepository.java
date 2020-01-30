package com.softserve.rms.repository;


import com.softserve.rms.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<Person, Long> {
    Person findPersonByEmail(String email);
}