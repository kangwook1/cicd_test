package com.appcenter.practice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProfileImageConfig implements WebMvcConfigurer {

    @Value("${profile.upload.path}")
    private String uploadFolder;

    //외부 폴더에 있는 이미지에 접근하기 위한 설정, handler url로 요청시 접근 가능하다.
    //프론트는 백엔드로부터 파일이름을 받고 요청을 날리면 된다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/profile/**")
                .addResourceLocations("file:/"+ uploadFolder);

    }
}
