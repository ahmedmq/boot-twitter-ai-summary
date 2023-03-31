package com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline;

import java.util.List;

public record Data(List<String> editHistoryTweetIds, String id, String text) {
}
