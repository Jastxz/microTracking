package es.jastxz.micro_tracking.model;

import lombok.Data;

@Data
public class VisitorData {
    private String page;
    private String path;
    private String referrer;
    private String userAgent;
    private String timestamp;
    private String sessionId;
    private String screenResolution;
    private String language;
}
