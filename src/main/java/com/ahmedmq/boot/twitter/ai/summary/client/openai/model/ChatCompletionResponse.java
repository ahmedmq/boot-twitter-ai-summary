package com.ahmedmq.boot.twitter.ai.summary.client.openai.model;

import java.util.List;

public record ChatCompletionResponse(String id, String object, String created, List<ChatCompletionChoice> choices) {
}
