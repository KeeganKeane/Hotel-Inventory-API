package jp.co.rakuten.inventoryapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger configuration for swagger2 integration into the app.
 *
 * @author md.syduzzaman
 */
@Configuration
@EnableSwagger2
public class SpringFoxConfig extends WebMvcConfigurationSupport {

    @Value("${project.version:0.0.1}")
    private String version;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("inventory-api")
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .build()
                .pathMapping("/")
                .apiInfo(apiInfo());

    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .version(version)
                .title("Inventory API")
                .description("This is the core API for Hotel availability and reservation booking in a specified database. Inventory searches, reservation searches, " +
                        "inventory creations, reservation bookings, inventory deletions and reservation deletions can be made. ")
                .license("Rakuten Inc")
                .licenseUrl("www.rakuten.com")
                .build();
    }
}