package io.jrocket.infra.config;

import io.jrocket.infra.util.NoSessionRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionTrackingMode;
import java.util.HashSet;
import java.util.Set;

public class WebAppInitializer implements WebApplicationInitializer {

    private final static Logger LOGGER = LoggerFactory.getLogger(WebAppInitializer.class);

    @Override
    public void onStartup(ServletContext container) {
        addSpringWebSupport(container);
    }

    private void addSpringWebSupport(ServletContext container) {
        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationConfig.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", new DispatcherServlet(rootContext));
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        // Add filter
//        container.addFilter("springSecurityFilterChain", new NoSessionRequestFilter()).addMappingForUrlPatterns(null, false, "/*");

    }


}
