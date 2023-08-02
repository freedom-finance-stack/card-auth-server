package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.Getter;
import lombok.Setter;

import static io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP;

/**
 * The {@code SwaggerConfiguration} class is a Spring configuration class that sets up and
 * configures the Swagger OpenAPI documentation for the ACS (Access Control Server) module. It
 * defines the API information, including title, version, description, and contact details. It also
 * configures the security scheme for the API, using bearer token authentication.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "swagger")
public class SwaggerConfiguration {

    private String appName;

    private String appDescription;

    private String appVersion;

    private String appLicense;

    private String appLicenseUrl;

    private String contactName;

    private String contactUrl;

    private String contactMail;

    /**
     * Creates and configures the OpenAPI object for Swagger documentation. It sets up API
     * information and security schemes.
     *
     * @return The configured {@link OpenAPI} object.
     */
    @Bean
    public OpenAPI openAPI() {

        final Info apiInformation = getApiInformation();
        final Components components = new Components();

        final String schemeName = "bearerAuth";
        components.addSecuritySchemes(
                schemeName,
                new SecurityScheme()
                        .name(schemeName)
                        .type(HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT"));

        final OpenAPI openAPI = new OpenAPI();
        openAPI.setInfo(apiInformation);
        openAPI.setComponents(components);
        //   openAPI.addSecurityItem(new SecurityRequirement().addList(schemeName));

        return openAPI;
    }

    private Info getApiInformation() {

        final License license = new License();
        license.setName(appLicense);
        license.setUrl(appLicenseUrl);

        final Contact contact = new Contact();
        contact.setName(contactName);
        contact.setUrl(contactUrl);
        contact.setEmail(contactMail);

        final Info info = new Info();
        info.setTitle(appName);
        info.setVersion(appVersion);
        info.setDescription(appDescription);
        info.setLicense(license);
        info.setContact(contact);

        return info;
    }
}
