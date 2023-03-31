package com.ahmedmq.boot.twitter.ai.summary.client.twitter;

import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline.UserTimeline;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.userlookup.UserLookup;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface TwitterClient {

    @GetExchange("/users/by/username/{username}")
    UserLookup getUserByUsername(@PathVariable String username);

    @GetExchange("/users/{id}/tweets")
    UserTimeline getUserTimeline(@PathVariable String id);

}
