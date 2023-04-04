package com.ahmedmq.boot.twitter.ai.summary.client.openai;

import com.ahmedmq.boot.twitter.ai.summary.client.WiremockInitializer;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionRequest;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionResponse;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatMessage;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatRole;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

@SpringBootTest
@ContextConfiguration(initializers = WiremockInitializer.class)
@ActiveProfiles("test")
class OpenAIClientTest {

    @Autowired
    OpenAIClient openAIClient;

    @Autowired
    WireMockServer openAIMockServer;

    @Test
    void shouldReturnChatCompletion() {

        openAIMockServer.stubFor(post("/v1/chat/completions")
                .withHeader("Authorization", equalTo("Bearer abc"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(
                        aResponse()
                                .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                .withBodyFile("openai_chat_completion_response.json")
                )
        );

        ChatCompletionResponse completionResponse = openAIClient.createCompletion(
                new ChatCompletionRequest(
                        "gpt-3.5-turbo",
                        Collections.singletonList(
                                new ChatMessage(ChatRole.user, "Hello!"))
                )
        );

        Assertions.assertThat(completionResponse).satisfies(
                a -> {
                    a.choices().get(0).message().role().equals(ChatRole.assistant);
                    a.choices().get(0).message().content().equals("\n\nHello there, how may I assist you today?");

                }
        );
    }
}