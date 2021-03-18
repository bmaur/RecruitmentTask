package com.one2tribe.recruitment.model;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class Message {
    UUID id;
    String content;
}
