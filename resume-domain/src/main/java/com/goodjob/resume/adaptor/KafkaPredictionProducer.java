package com.goodjob.resume.adaptor;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaPredictionProducer {
    private static final String TOPIC_QUESTION = "question-request-prod";
    private static final String TOPIC_ADVICE = "advice-request-prod";
    private final KafkaTemplate<String, String> kafkaTemplate;
    public void sendQuestionRequest(String message) {
        this.kafkaTemplate.send(TOPIC_QUESTION, message);
    }

    public void sendAdviceRequest(String message) {
        this.kafkaTemplate.send(TOPIC_ADVICE, message);
    }
}
