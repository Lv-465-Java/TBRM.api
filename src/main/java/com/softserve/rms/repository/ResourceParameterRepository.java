package com.softserve.rms.repository;

import com.softserve.rms.entities.ResourceParameter;
import com.softserve.rms.entities.ResourceTemplate;
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
     * @return list of  {@link ResourceParameter}
     * @author Andrii Bren
     */
    List<ResourceParameter> findAllByResourceTemplateId(Long id);

    /**
     * Method finds {@link Optional <ResourceParameter>} by name.
     *
     * @param name of {@link ResourceParameter}
     * @param id   of {@link ResourceTemplate}
     * @return {@link Optional<ResourceParameter>}
     * @author Halyna Yatseniuk
     */
    Optional<ResourceParameter> findByNameAndResourceTemplateId(String name, Long id);

    /**
     * Method finds {@link Optional <ResourceParameter>} by column name.
     *
     * @param columnName of {@link ResourceParameter}
     * @param id         of {@link ResourceTemplate}
     * @return {@link Optional<ResourceParameter>}
     * @author Halyna Yatseniuk
     */
    Optional<ResourceParameter> findByColumnNameAndResourceTemplateId(String columnName, Long id);
}
