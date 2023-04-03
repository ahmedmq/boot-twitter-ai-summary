package com.ahmedmq.boot.twitter.ai.summary;

import com.ahmedmq.boot.twitter.ai.summary.client.openai.OpenAIClient;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionRequest;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatCompletionResponse;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatMessage;
import com.ahmedmq.boot.twitter.ai.summary.client.openai.model.ChatRole;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.TwitterClient;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline.Data;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.timeline.UserTimeline;
import com.ahmedmq.boot.twitter.ai.summary.client.twitter.model.userlookup.UserLookup;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequiredArgsConstructor
public class SummaryController {

    private final TwitterClient twitterClient;

    private final OpenAIClient openAIClient;


    @GetMapping("/summary")
    public SummaryResponse getSummary(@RequestParam("username") String username) {
        UserLookup user = twitterClient.getUserByUsername(username);
        UserTimeline userTimeline = twitterClient.getUserTimeline(user.data().id());
        String promptText = """
                You will be given a twitter stream belonging to a specific profile. Answer with a summary of what they've lately been tweeting about and in what languages.
                                                                                    You may go into some detail about what topics they tend to like tweeting about. Please also mention their overall tone, for example: positive,
                                                                                    negative, political, sarcastic or something else.
                                                                                    Examples:\s
                                                                                   \s
                                                                                    Twitter stream: RT @deepset_ai: Come join our Haystack server for our first Discord event tomorrow, a deepset AMA session with @rusic_milos @malte_pietsch…
                                                                                    RT @deepset_ai: Join us for a chat! On Thursday 25th we are hosting a 'deepset - Ask Me Anything' session on our brand new Discord. Come…
                                                                                    RT @deepset_ai: Curious about how you can use @OpenAI GPT3 in a Haystack pipeline? This week we released Haystack 1.7 with which we introdu…
                                                                                    RT @tuanacelik: So many updates from @deepset_ai today!\s
                                                                                   \s
                                                                                    Summary: This user has lately been retweeting tweets fomr @deepset_ai. The topics of the tweets have been around the Haystack community, NLP and GPT. They've
                                                                                    been posting in English, and have had a positive, informative tone.
                                                                                   \s
                                                                                    Twitter stream: I've directed my team to set sharper rules on how we deal with unidentified objects.\\n\\nWe will inventory, improve ca…\s
                                                                                    the incursion by China’s high-altitude balloon, we enhanced radar to pick up slower objects.\\n \\nBy doing so, w…
                                                                                    I gave an update on the United States’ response to recent aerial objects.\s
                                                                                    Summary: This user has lately been tweeting about having sharper rules to deal with unidentified objects and an incursuin by China's high-altitude
                                                                                    baloon. Their tweets have mostly been neutral but determined in tone. They mostly post in English.
                                                                                    Twitter stream: %s\s
                                                                                   \s
                                                                                    Summary:\s
                """.formatted(userTimeline.data().stream().map(Data::text).collect(java.util.stream.Collectors.joining("\n")));
        ChatCompletionRequest request = new ChatCompletionRequest("gpt-3.5-turbo", Collections.singletonList(new ChatMessage(ChatRole.user, promptText)));
        ChatCompletionResponse completionResponse = openAIClient.createCompletion(request);
        return new SummaryResponse(completionResponse.choices().get(0).message().content());
    }

    record SummaryResponse(String text) {
    }

}
