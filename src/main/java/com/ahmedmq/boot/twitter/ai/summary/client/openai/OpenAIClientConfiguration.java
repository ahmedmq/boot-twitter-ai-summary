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

    private static final String OPENAI_API =
            "https://api.openai.com";


    @Bean(name="openAiWebClient")
    WebClient openAIWebClient(@Value("${openai.api.key}") String apiKey) {
        return WebClient.builder()
                .baseUrl(OPENAI_API)
                .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth(apiKey))
                .build();
    }

    @Bean
    OpenAIClient openAIClient(@Qualifier("openAiWebClient") WebClient openAIWebClient) {
        HttpServiceProxyFactory factory =
                HttpServiceProxyFactory.builder(
                                WebClientAdapter.forClient(openAIWebClient))
                        .build();
        return factory.createClient(OpenAIClient.class);
    }
}
