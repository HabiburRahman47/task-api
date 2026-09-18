package task_api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskApiOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Task API")
                        .version("v1.0.0")
                        .description("REST API for task management system.")
                        .contact(new Contact()
                                .name("Task API Team"))
                );
    }
}
