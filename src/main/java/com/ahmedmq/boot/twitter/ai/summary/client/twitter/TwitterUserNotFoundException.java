package com.ahmedmq.boot.twitter.ai.summary.client.twitter;

public class TwitterUserNotFoundException extends RuntimeException{
    public TwitterUserNotFoundException(String message) {
        super(message);
    }
}
