package com.ahmedmq.boot.twitter.ai.summary.client.openai;

import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.CompletionRequest;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.CompletionResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface OpenAIClient {

    @PostExchange("/v1/completions")
    CompletionResponse createCompletion(@RequestBody CompletionRequest request);

}
