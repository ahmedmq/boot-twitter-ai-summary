package com.ahmedmq.boot.twitter.ai.summary.client.openai;

import com.ahmedmq.boot.twitter.ai.summary.client.twitter.TwitterClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class OpenAIClientConfiguration {

    @Bean(name="openAiWebClient")
    WebClient openAIWebClient(@Value("${openai.api.base-url}") String openAIUrl, @Value("${openai.api.key}") String apiKey) {
        return WebClient.builder()
                .baseUrl(openAIUrl)
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(apiKey))
                .build();
    }

    @Bean
    OpenAIClient openAIClient(@Qualifier("openAiWebClient") WebClient openAIWebClient) {
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builder(
                                WebClientAdapter.forClient(openAIWebClient))
                        .blockTimeout(java.time.Duration.ofSeconds(10))
                        .build();
        return factory.createClient(OpenAIClient.class);
    }
}
