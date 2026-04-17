package com.slop.gpt.dto;

import java.util.ArrayList;
import java.util.List;

public class ErrorResponseDto {
    private String message;
    private String timestamp;
    private String path;
    private List<String> details;

    public ErrorResponseDto() {
        this.details = new ArrayList<>();
    }

    public ErrorResponseDto(String message, String timestamp, String path, List<String> details) {
        this.message = message;
        this.timestamp = timestamp;
        this.path = path;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<String> getDetails() {
        return details;
    }

    public void setDetails(List<String> details) {
        this.details = details;
    }
}
