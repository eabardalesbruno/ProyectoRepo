package com.proriberaapp.ribera.Crosscutting.config;

import com.proriberaapp.ribera.Domain.invoice.SunatInvoice;
import com.proriberaapp.ribera.Infraestructure.invoice.VisualContIntegration;
import com.proriberaapp.ribera.services.client.S3Uploader;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BeansConfig {
    @Bean
    public WebProperties.Resources resources() {
        return new WebProperties.Resources();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public S3Uploader s3Uploader(RestTemplate restTemplate) {
        return new S3Uploader(restTemplate);
    }

    @Bean
    public SunatInvoice sunatInvoice() {
        return new VisualContIntegration();
    }
}
