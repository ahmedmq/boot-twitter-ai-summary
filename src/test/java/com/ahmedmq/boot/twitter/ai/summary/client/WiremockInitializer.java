package com.ahmedmq.boot.twitter.ai.summary.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;

public class WiremockInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        WireMockServer twitterMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        WireMockServer openAIMockServer = new WireMockServer(new WireMockConfiguration().dynamicPort());
        twitterMockServer.start();
        openAIMockServer.start();

        twitterMockServer.stubFor(
                WireMock.post("/oauth2/token")
                        .withBasicAuth("test-consumer-key","test-consumer-secret")
                        .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded;charset=UTF-8"))
                        .withRequestBody(containing("grant_type=client_credentials"))
                        .willReturn(
                                aResponse()
                                        .withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                                        .withBody("""
                                                {
                                                  "token_type":"bearer",
                                                  "access_token":"AAAAAAAAAAAAAAAAAAAAAEgkiwEAAAAAam%2Bb49N5bkbiQIx6Nzr6tvOR5kk%3DRQngUp3Lu11HZttp1TkGbBuvTa4F66HuqgkZgfYTmGi299gxDY"
                                                }
                                                """)
                        )
        );

        TestPropertyValues
                .of(
                        "spring.security.oauth2.client.provider.twitter.token-uri=http://localhost:" + twitterMockServer.port() + "/oauth2/token",
                        "twitter.api.base-url=http://localhost:" + twitterMockServer.port(),
                        "openai.api.base-url=http://localhost:" + openAIMockServer.port()
                ).applyTo(applicationContext);

        applicationContext.getBeanFactory().registerSingleton("twitterMockServer", twitterMockServer);
        applicationContext.getBeanFactory().registerSingleton("openAIMockServer", openAIMockServer);

        applicationContext.addApplicationListener(applicationEvent -> {
            if (applicationEvent instanceof ContextClosedEvent) {
                twitterMockServer.stop();
                openAIMockServer.stop();
            }
        });

    }



}
