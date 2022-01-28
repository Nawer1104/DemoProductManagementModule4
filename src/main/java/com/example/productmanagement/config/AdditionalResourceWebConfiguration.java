package com.example.productmanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@EnableSpringDataWebSupport
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        Path imageUploadDir = Paths.get("./images");
        String imageUploadPath = imageUploadDir.toFile().getAbsolutePath();

        registry.addResourceHandler("/images/**").addResourceLocations("file:/" + imageUploadPath + "/");
    }
}
