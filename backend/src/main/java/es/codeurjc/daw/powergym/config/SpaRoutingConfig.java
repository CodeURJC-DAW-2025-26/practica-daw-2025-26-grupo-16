package es.codeurjc.daw.powergym.config;

import java.io.IOException;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

@Configuration
public class SpaRoutingConfig implements WebMvcConfigurer {

    private static final String SPA_ROUTE = "/new";
    private static final String SPA_INDEX = "/static" + SPA_ROUTE + "/index.html";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController(SPA_ROUTE).setViewName("forward:" + SPA_ROUTE + "/index.html");
        registry.addViewController(SPA_ROUTE + "/").setViewName("forward:" + SPA_ROUTE + "/index.html");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(SPA_ROUTE + "/**")
            .addResourceLocations("classpath:/static" + SPA_ROUTE + "/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver() {
                @Override
                protected Resource getResource(String resourcePath, Resource location) throws IOException {
                    if (resourcePath == null || resourcePath.isBlank() || "/".equals(resourcePath)) {
                        return new ClassPathResource(SPA_INDEX);
                    }

                    String normalizedPath = resourcePath.startsWith("/") ? resourcePath.substring(1) : resourcePath;
                    Resource requestedResource = location.createRelative(normalizedPath);
                    boolean isStaticAsset = resourcePath.contains(".");

                    return isStaticAsset && requestedResource.exists() && requestedResource.isReadable()
                        ? requestedResource
                        : new ClassPathResource(SPA_INDEX);
                }
            });
    }
}
