package com.goodjob.core.domain.job.consume;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goodjob.core.domain.job.dto.JobResponseDto;
import com.goodjob.core.domain.job.service.JobStatisticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchConsumer {
    private final JobStatisticService service;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "job-prod", groupId = "job-group")
    public void batchConsumer(String message) {
        try {
            JobResponseDto jobResponseDto = objectMapper.readValue(message, JobResponseDto.class);
            log.debug("consume message : {}", jobResponseDto.getUrl());
            service.upsert(jobResponseDto);
        } catch (Exception e) {
            log.error("batch consumer error : {}", e.getMessage());
        }
    }
}
