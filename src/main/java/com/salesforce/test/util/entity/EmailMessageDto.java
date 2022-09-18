package com.salesforce.test.util.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
public class EmailMessageDto {
    private final String subject;
    private final String body;
    private final Instant sentDate;
}
