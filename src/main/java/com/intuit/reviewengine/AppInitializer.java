package com.intuit.reviewengine;

import com.intuit.reviewengine.AppConfig;
import com.intuit.reviewengine.config.PropertySourcesConfig;
import com.intuit.reviewengine.config.RequestLoggerFilter;
import com.intuit.reviewengine.config.WebAppConfig;

import org.springframework.core.env.PropertySource;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class AppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) throws ServletException {
        // Create the 'root' Spring application context
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        PropertySource applicationProperties = PropertySourcesConfig.applicationProperties();
        if (applicationProperties != null) {
            rootContext.getEnvironment().getPropertySources().addFirst(applicationProperties);
        }
        rootContext.register(AppConfig.class);

        // Manage the lifecycle of the root application context
        container.addListener(new ContextLoaderListener(rootContext));

        // Create the dispatcher servlet's Spring application context
        AnnotationConfigWebApplicationContext dispatcherContext =
                new AnnotationConfigWebApplicationContext();
        dispatcherContext.register(WebAppConfig.class);

        // Register and map the dispatcher servlet
        ServletRegistration.Dynamic dispatcher =
                container.addServlet("dispatcher", new DispatcherServlet(dispatcherContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
        
        System.setProperty("logback.configurationFile", "/Users/kchopperla/Downloads/ReviewService/src/main/resources/logback.xml");

        FilterRegistration.Dynamic requestFilter = container.addFilter("RequestLoggerfilter", RequestLoggerFilter.class);
        requestFilter.addMappingForUrlPatterns(null, true, "/*");
    }

}