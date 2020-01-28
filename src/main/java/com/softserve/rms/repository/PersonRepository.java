package com.softserve.rms.repository;

import com.softserve.rms.entities.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    /**
     * Save person
     *
     * @param person to save.
     * @return Person saved}
     */
    Person save(Person person);

    List<Person> findByEmail(String email);
}
