package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceParameterRepository extends JpaRepository<ResourceParameter, Long> {

    /**
     * Method finds all {@link ResourceParameter} by template id.
     *
     * @param id {@link ResourceParameter} id
     * @return list of Optional {@link ResourceParameter}
     * @author Andrii Bren
     */
    Optional<List<ResourceParameter>> findAllByResourceTemplateId(Long id);
}