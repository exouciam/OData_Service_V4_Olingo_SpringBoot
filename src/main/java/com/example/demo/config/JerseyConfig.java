package com.example.demo.config;

import com.example.demo.service.DomainODataJPAServiceFactory;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import org.apache.olingo.odata2.api.ODataServiceFactory;
import org.apache.olingo.odata2.core.rest.ODataRootLocator;
import org.apache.olingo.odata2.core.rest.app.ODataApplication;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.io.IOException;

/**
 * Register our ServiceFactory with Olingo’s runtime
 * Register Olingo’s entry point with the JAX-RS runtime.
 * Define Odata path
 */
@Component
@ApplicationPath("/odata")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig(DomainODataJPAServiceFactory serviceFactory, EntityManagerFactory emf) {
        ODataApplication app = new ODataApplication();
        app
                .getClasses()
                .forEach(c -> {
                    if (!ODataRootLocator.class.isAssignableFrom(c)) {
                        register(c);
                    }
                });

        register(new DomainRootLocator(serviceFactory));
        register(new EntityManagerFilter(emf));
    }

    /**
     * Instantiate ODataJPAServcieFactory implementation
     */
    @Path("/")
    public static class DomainRootLocator extends ODataRootLocator {
        private DomainODataJPAServiceFactory serviceFactory;

        public DomainRootLocator(DomainODataJPAServiceFactory serviceFactory) {
            this.serviceFactory = serviceFactory;
        }

        @Override
        public ODataServiceFactory getServiceFactory() {
            return this.serviceFactory;
        }
    }

    @Provider
    public static class EntityManagerFilter implements ContainerRequestFilter,
            ContainerResponseFilter {

        public static final String EM_REQUEST_ATTRIBUTE =
                EntityManagerFilter.class.getName() + "_ENTITY_MANAGER";
        private final EntityManagerFactory emf;

        @Context
        private HttpServletRequest httpRequest;

        public EntityManagerFilter(EntityManagerFactory emf) {
            this.emf = emf;
        }

        @Override
        public void filter(ContainerRequestContext ctx) throws IOException {
            EntityManager em = this.emf.createEntityManager();
            httpRequest.setAttribute(EM_REQUEST_ATTRIBUTE, em);
            if (!"GET".equalsIgnoreCase(ctx.getMethod())) {
                em.getTransaction().begin();
            }
        }

        @Override
        public void filter(ContainerRequestContext requestContext,
                           ContainerResponseContext responseContext) throws IOException {
            EntityManager em = (EntityManager) httpRequest.getAttribute(EM_REQUEST_ATTRIBUTE);
            if (!"GET".equalsIgnoreCase(requestContext.getMethod())) {
                EntityTransaction t = em.getTransaction();
                if (t.isActive() && !t.getRollbackOnly()) {
                    t.commit();
                }
            }

            em.close();
        }
    }


}