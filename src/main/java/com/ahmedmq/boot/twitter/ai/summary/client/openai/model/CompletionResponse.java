package com.ahmedmq.boot.twitter.ai.summary.client.openai.model;

import java.util.List;

public record CompletionResponse(String id, String object, String created, String model, List<CompletionChoice> choices) {
}
