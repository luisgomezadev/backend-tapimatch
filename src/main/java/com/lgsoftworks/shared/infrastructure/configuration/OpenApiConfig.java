package com.lgsoftworks.shared.infrastructure.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "TapiMatch API",
                version = "1.0.0",
                description = "Documentación de la API para el sistema de reservas de canchas sintéticas.",
                contact = @Contact(
                        name = "Luis Gómez",
                        email = "luisgomez24g@gmail.com",
                        url = "https://luisgomezdev.vercel.app/"
                ),
                license = @License(
                        name = "MIT",
                        url = "https://opensource.org/licenses/MIT"
                )
        )
)
public class OpenApiConfig {

}

