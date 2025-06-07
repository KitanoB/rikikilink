package fr.rikiki.rlk.link_service.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LinkCreateRequest {

    @NotBlank(message = "targetUrl must not be blank")
    @Size(max = 2048, message = "targetUrl must be at most 2048 characters")
    private String targetUrl;

    public LinkCreateRequest() {
    }

    public LinkCreateRequest(String targetUrl) {
        this.targetUrl = targetUrl;
    }

    public String getTargetUrl() {
        return targetUrl;
    }

    public void setTargetUrl(String targetUrl) {
        this.targetUrl = targetUrl;
    }
}