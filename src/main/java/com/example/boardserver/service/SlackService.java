package com.example.boardserver.service;

import com.slack.api.Slack;
import org.springframework.beans.factory.annotation.Value;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class SlackService {

    @Value("${slack.token}")
    String slackToken;

    public void sendSlackMessage(String message, String channel) {
        String channelAddress = "";
        log.info("Channel: " + channel);
        if (channel.equals("error")) {
            channelAddress = "#모니터링";
        }

        try {
            MethodsClient methods = Slack.getInstance().methods(slackToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelAddress)
                    .text(message)
                    .build();

            methods.chatPostMessage(request);
            log.info("requeset :{}", request);

            log.info("Slack " + channel + " 에 메시지 보냄");
        } catch (SlackApiException | IOException e) {
            log.error("Slack API 호출 중 오류 발생: ", e);
            if (e instanceof SlackApiException) {
                SlackApiException slackApiException = (SlackApiException) e;
                log.error("Slack API 응답 상태: {}", slackApiException.getResponse());
            }
        }
    }
}