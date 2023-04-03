package com.ahmedmq.boot.twitter.ai.summary.client.openai.model;

import java.util.List;

public record ChatCompletionRequest(String model, List<ChatMessage> messages) {
}
