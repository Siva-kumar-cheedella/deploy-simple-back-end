package com.cleanlearn.dto; 

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class GroqDto {

    // Request DTO
    public record GroqRequest(
            String model, 
            List<Message> messages,
            @JsonProperty("response_format") ResponseFormat responseFormat
    ) {}

    public record Message(String role, String content) {}

    public record ResponseFormat(String type) {}

    // Response DTO
    public record GroqResponse(String id, List<Choice> choices) {}

    public record Choice(Message message) {}
}