package com.ahmedmq.boot.twitter.ai.summary;

import com.ahmedmq.boot.twitter.ai.summary.client.openai.OpenAIClient;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionChoice;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionRequest;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionResponse;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatMessage;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatRole;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.TwitterClient;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline.UserTimeline;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.userlookup.Data;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.userlookup.UserLookup;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SummaryController.class)
class SummaryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TwitterClient twitterClient;

    @MockBean
    OpenAIClient openAIClient;


    @Test
    void shouldReturnSummaryWhenEndpointInvoked() throws Exception {
        Mockito.when(twitterClient.getUserByUsername("test")).thenReturn(new UserLookup(new Data("1", "Test", "test")));
        Mockito.when(twitterClient.getUserTimeline("1"))
                .thenReturn(new UserTimeline(Collections.singletonList(new com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline.Data(null, "1", "hello"))));

        Mockito.when(openAIClient
                .createCompletion(
                        Mockito.any(ChatCompletionRequest.class)
                )
        ).thenReturn(
                new ChatCompletionResponse("1",
                        "test",
                        "2022-10-10",
                        Collections.singletonList(
                                new ChatCompletionChoice(
                                        new ChatMessage(ChatRole.assistant, "Hey There from ChatGPT!!"))
                        )
                )
        );

        ResultActions resultActions = mockMvc.perform(get("/summary")
                .queryParam("username", "test"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.text", Matchers.is("Hey There from ChatGPT!!")));

    }

    @Test
    public void shouldReturnNotFoundIfUsernameNotExists() throws Exception {

        Mockito.when(twitterClient.getUserByUsername("xxdd")).thenReturn(null);

        mockMvc.perform(get("/summary?username=xxdd"))
                .andExpect(status().isNotFound());

    }
}