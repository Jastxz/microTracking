package es.jastxz.micro_tracking.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.jastxz.micro_tracking.model.VisitorData;
import es.jastxz.micro_tracking.model.TimeData;
import es.jastxz.micro_tracking.service.TrackingService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/")
public class TrackingController {

    private final TrackingService trackingService;

    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @PostMapping("/visit")
    public ResponseEntity<Void> trackVisit(
            @RequestBody VisitorData data,
            HttpServletRequest request) {

        System.out.println("Tracking visit: " + data);
        String clientIp = getClientIp(request);
        trackingService.processVisit(data, clientIp);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/time")
    public ResponseEntity<Void> trackTime(@RequestBody TimeData data) {
        trackingService.processTimeSpent(data);
        return ResponseEntity.ok().build();
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        // Si hay múltiples IPs (proxy chain), toma la primera
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
