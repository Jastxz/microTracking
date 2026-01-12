package es.jastxz.micro_tracking.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {

    private final WebClient webClient;
    private final String notificationEmail;
    private final String fromEmail;
    private final boolean isEnabled;

    private static final String RESEND_API_URL = "https://api.resend.com/emails";

    public EmailService(WebClient.Builder webClientBuilder,
            @Value("${resend.api-key:}") String apiKey,
            @Value("${tracking.notification.email:}") String notificationEmail,
            @Value("${resend.from:}") String fromEmail) {

        this.notificationEmail = notificationEmail;
        this.fromEmail = fromEmail;

        if (apiKey != null && !apiKey.isBlank()) {
            this.webClient = webClientBuilder.baseUrl(RESEND_API_URL)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .build();
            this.isEnabled = true;
        } else {
            System.err.println(
                    "⚠️ RESEND_API_KEY no configurada apropiadamente. El envío de emails estará deshabilitado.");
            this.webClient = null;
            this.isEnabled = false;
        }
    }

    public void sendEmail(String subject, String body) {
        if (!isEnabled || webClient == null) {
            System.out.println("⚠️ Email omitido (servicio deshabilitado): " + subject);
            return;
        }

        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("from", fromEmail);
            payload.put("to", notificationEmail);
            payload.put("subject", subject);
            payload.put("content", body);

            webClient.post().uri("/emails").bodyValue(payload).retrieve().onStatus(
                    status -> status.isError(),
                    response -> response.bodyToMono(String.class).map(error -> {
                        System.err.println("Error enviando email: " + error);
                        throw new RuntimeException("Error enviando email" + error);
                    })).toBodilessEntity().block();

            System.out.println("Email enviado correctamente a " + notificationEmail);
        } catch (Exception e) {
            System.err.println("Error enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}