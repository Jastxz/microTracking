package es.jastxz.micro_tracking.model;

import lombok.Data;

@Data
public class TimeData {
    private String page;
    private String path;
    private Integer timeSpent;
    private String sessionId;
    private String timestamp;
}
