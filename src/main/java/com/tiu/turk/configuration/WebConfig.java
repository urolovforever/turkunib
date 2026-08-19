package com.tiu.turk.configuration;

import com.tiu.turk.configuration.PathCookieLocaleResolver;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig
implements WebMvcConfigurer {
    @Value(value="${app.upload.dir}")
    private String uploadDir;

    @Bean
    public PathCookieLocaleResolver localeResolver() {
        PathCookieLocaleResolver resolver = new PathCookieLocaleResolver();
        resolver.setCookieMaxAge(Duration.ofDays(365L));
        return resolver;
    }

    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = "file:" + (String)(this.uploadDir.endsWith("/") ? this.uploadDir : this.uploadDir + "/");
        registry.addResourceHandler(new String[]{"/uploads/**"}).addResourceLocations(new String[]{location}).setCachePeriod(Integer.valueOf(3600));
        // Cache static CSS/JS/images so browsers don't re-download them on every page load (1 day).
        registry.addResourceHandler(new String[]{"/resources/**"}).addResourceLocations(new String[]{"classpath:/static/resources/"}).setCachePeriod(Integer.valueOf(86400));
    }
}

