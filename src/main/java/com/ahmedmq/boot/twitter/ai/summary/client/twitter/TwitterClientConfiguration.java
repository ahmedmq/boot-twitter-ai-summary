package com.ahmedmq.boot.twitter.ai.summary.client.twitter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class TwitterClientConfiguration {

    @Bean(name = "twitterWebClient")
    WebClient webClient(OAuth2AuthorizedClientManager
                                authorizedClientManager, @Value("${twitter.api.base-url}") String twitterApi) {

        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                        authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("twitter");

        return WebClient.builder()
                .baseUrl(twitterApi)
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    TwitterClient twitterClient(@Qualifier("twitterWebClient") WebClient oauth2WebClient) {
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builder(
                                 WebClientAdapter.forClient(oauth2WebClient))
                        .build();
        return factory.createClient(TwitterClient.class);
    }
}
