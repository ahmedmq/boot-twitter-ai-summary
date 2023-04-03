package com.ahmedmq.boot.twitter.ai.summary.client.openai;

import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionRequest;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface OpenAIClient {

    @PostExchange("/v1/chat/completions")
    ChatCompletionResponse createCompletion(@RequestBody ChatCompletionRequest request);

}
