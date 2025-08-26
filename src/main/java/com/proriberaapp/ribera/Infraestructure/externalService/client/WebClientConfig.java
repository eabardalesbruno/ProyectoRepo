package com.proriberaapp.ribera.Infraestructure.externalService.client;

import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfig {

    @Value("${inclub.admin.base-url}")
    private String adminPanelBaseUrl;

    @Value("${inclub.gateway.base-url}")
    private String inClubGatewayBaseUrl;

    @Value("${inclub.account.base-url}")
    private String inClubAccountBaseUrl;

    @Value("${ribera.wallet.base-url}")
    private String walletBaseUrl;

    @Bean
    public ReactorClientHttpConnector insecureConnector() {
        HttpClient client = HttpClient.create().secure(t ->
                t.sslContext(SslContextBuilder.forClient()
                        .trustManager(InsecureTrustManagerFactory.INSTANCE)));
        return new ReactorClientHttpConnector(client);
    }

    private WebClient build(WebClient.Builder b, String baseUrl, ReactorClientHttpConnector c) {
        return b.clientConnector(c).baseUrl(baseUrl).build();
    }

    // ---- WebClients
    @Bean(name = "adminPanelWebClientV2")
    @Primary
    public WebClient adminPanelWebClientV2(WebClient.Builder b, ReactorClientHttpConnector c) {
        return build(b, adminPanelBaseUrl, c);
    }

     @Bean(name = "inClubGatewayWebClientV2")
     @Lazy
     public WebClient inClubGatewayWebClientV2(WebClient.Builder b, ReactorClientHttpConnector c) {
         return build(b, inClubGatewayBaseUrl, c);
     }

    @Bean(name = "inClubAccountWebClientV2")
    @Lazy
    public WebClient inClubAccountWebClientV2(WebClient.Builder b, ReactorClientHttpConnector c) {
        return build(b, inClubAccountBaseUrl, c);
    }
    // @Bean(name = "walletWebClient")
    // public WebClient walletWebClient(WebClient.Builder b, ReactorClientHttpConnector c) {
    //     return build(b, walletBaseUrl, c);
    // }

    // ---- ExternalApiClients
    @Bean(name = "adminPanelClientV2")
    public ExternalApiClient adminPanelClientV2(@Qualifier("adminPanelWebClientV2") WebClient wc) {
        return new ExternalApiClient(wc);
    }

     @Bean(name = "inClubGatewayClientV2")
     public ExternalApiClient inClubGatewayClient(@Qualifier("inClubGatewayWebClientV2") WebClient wc) {
         return new ExternalApiClient(wc);
     }

    @Bean(name = "inClubAccountClientV2")
    public ExternalApiClient inClubAccountClient(@Qualifier("inClubAccountWebClientV2") WebClient wc) {
        return new ExternalApiClient(wc);
    }
    // @Bean(name = "walletClient")
    // public ExternalApiClient walletClient(@Qualifier("walletWebClient") WebClient wc) {
    //     return new ExternalApiClient(wc);
    // }



}
