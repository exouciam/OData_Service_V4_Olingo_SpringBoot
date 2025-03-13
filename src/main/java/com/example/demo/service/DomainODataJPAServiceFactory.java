package com.example.demo.service;

import com.example.demo.config.JerseyConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.olingo.odata2.api.processor.ODataContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAContext;
import org.apache.olingo.odata2.jpa.processor.api.ODataJPAServiceFactory;
import org.apache.olingo.odata2.jpa.processor.api.exception.ODataJPARuntimeException;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

/**
 * Initialize the OData JPA context.
 * This context acts as an adapter between OData and JPA,
 * enabling database interactions using OData requests
 */
@Component
public class DomainODataJPAServiceFactory extends ODataJPAServiceFactory {

    /**
     * Set up the JPA context for OData operations
     * It ensures that the JPA entity manager and persistence unit are properly
     * configured for handling OData requests
     *
     * @return
     * @throws ODataJPARuntimeException
     */
    @Override
    public ODataJPAContext initializeODataJPAContext() throws ODataJPARuntimeException {

        // retrieve current OData JPA context
        // - it manages JPA-related setting such as the entity manager,
        // persistence unit name and transaction handling
        ODataJPAContext oDataJPAContext = getODataJPAContext();

        // represents the current OData request
        // - contains metadata like HTTP header, query parameters and request details
        ODataContext oDataRequest = oDataJPAContext.getODataContext();

        // extract the raw HTTP request object
        // - allows to retrieve additional parameters such as Entity Manager
        HttpServletRequest httpServletRequest = (HttpServletRequest) oDataRequest.getParameter(
                ODataContext.HTTP_SERVLET_REQUEST_OBJECT);

        // EntityManager: core JPA interface used to interact with the database
        // - ensures that each request gets the correct entity manager instance,
        // supporting transaction management
        EntityManager entityManager = (EntityManager) httpServletRequest
                .getAttribute(JerseyConfig.EntityManagerFilter.EM_REQUEST_ATTRIBUTE);

        oDataJPAContext.setEntityManager(entityManager);
        oDataJPAContext.setPersistenceUnitName("default");

        // transaction management is handled by Spring
        oDataJPAContext.setContainerManaged(true);
        return oDataJPAContext;
    }
}
