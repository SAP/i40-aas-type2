package com.sap.i40aas.datamanager.webService.swagger;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SpringFoxConfig implements WebMvcConfigurer {

  @Value(value = "${api_version}")
  private String apiVersion;


  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
      .select()
      .apis(RequestHandlerSelectors.basePackage("com.sap.i40aas"))
      .paths(PathSelectors.any())
      .build()
      .apiInfo(apiInfo());
  }


  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {

    registry
      .addResourceHandler("swagger-ui.html")
      .addResourceLocations("classpath:/META-INF/resources/");

    registry
      .addResourceHandler("/webjars/**")
      .addResourceLocations("classpath:/META-INF/resources/webjars/");
  }

  private ApiInfo apiInfo() {
    return new ApiInfo(
      "AAS-Service Data-manager API",
      "The API specification of type-2 AAS",
      apiVersion,
      "Terms of service",
      new Contact("SAP Dev Team", "www.sap.com", "https://github.com/SAP/i40-aas-type2"),
      "License of API", "API license URL", Collections.emptyList()
    );
  }
}
