package org.freedomfinancestack.razorpay.cas.acs.module.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * The {@code DataSourceConfig} class is a Spring configuration class responsible for configuring
 * the data source used by the ACS (Access Control Server) module. It sets up the data source with
 * the necessary properties such as URL, username, password, and driver class name for database
 * connectivity.
 *
 * @version 1.0.0
 * @since 1.0.0
 * @author jaydeepRadadiya
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@Slf4j
@Getter
@Setter
public class DataSourceConfig {

    private String url;
    private String username;
    private String password;
    private String driverClassName;
    private int maximumPoolSize;

    /**
     * Creates and configures the DataSource bean using the specified properties for database
     * connectivity.
     *
     * @return The configured {@link DataSource} object.
     */
    @Bean
    public DataSource getDataSource() {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName(driverClassName);
        ds.setJdbcUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setMaximumPoolSize(maximumPoolSize);
        return ds;
    }
}
