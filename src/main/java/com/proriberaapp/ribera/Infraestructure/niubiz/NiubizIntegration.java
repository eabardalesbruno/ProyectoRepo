package com.proriberaapp.ribera.Infraestructure.niubiz;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class NiubizIntegration {

    @Value("${niubiz.api.url}")
    private String baseUrl;

    @Value("${niubiz.api.maxmemorysize}")
    private int maxMemorySize;

    @Bean(name = "nibuizClient")
    public WebClient nibuizClient() {
        final int size = maxMemorySize * 1024 * 1024;
        final ExchangeStrategies strategies = ExchangeStrategies.builder()
                .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size)).build();
        return WebClient.builder()
                .exchangeStrategies(strategies)
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                // .defaultHeader(HttpHeaders.AUTHORIZATION, token)
                .codecs(configurer -> {
                    ObjectMapper objectMapper = configurer.getReaders().stream()
                            .filter(reader -> reader instanceof Jackson2JsonDecoder)
                            .map(reader -> (Jackson2JsonDecoder) reader)
                            .map(reader -> reader.getObjectMapper())
                            .findFirst()
                            .orElseGet(() -> Jackson2ObjectMapperBuilder.json().build());
                    Jackson2JsonDecoder decoder = new Jackson2JsonDecoder(objectMapper, MediaType.TEXT_PLAIN);
                    configurer.customCodecs().registerWithDefaultConfig(decoder);
                })
                .build();
    }

}
