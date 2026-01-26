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

        // Logging detallado de configuración
        System.out.println("=== EmailService Configuration ===");
        System.out.println("API Key presente: " + (apiKey != null && !apiKey.isBlank() ? "SÍ" : "NO"));
        System.out.println("Notification Email: " + (notificationEmail != null && !notificationEmail.isBlank()
                ? notificationEmail
                : "NO CONFIGURADO"));
        System.out.println("From Email: " + (fromEmail != null && !fromEmail.isBlank()
                ? fromEmail
                : "NO CONFIGURADO"));
        System.out.println("==================================");

        this.notificationEmail = notificationEmail;
        this.fromEmail = fromEmail;

        // Validar configuración completa
        boolean hasApiKey = apiKey != null && !apiKey.isBlank();
        boolean hasNotificationEmail = notificationEmail != null && !notificationEmail.isBlank();
        boolean hasFromEmail = fromEmail != null && !fromEmail.isBlank();

        if (hasApiKey && hasNotificationEmail && hasFromEmail) {
            this.webClient = webClientBuilder.baseUrl(RESEND_API_URL)
                    .defaultHeader("Authorization", "Bearer " + apiKey)
                    .defaultHeader("Content-Type", "application/json")
                    .build();
            this.isEnabled = true;
            System.out.println("✅ EmailService habilitado correctamente");
        } else {
            // Mensajes específicos sobre qué falta
            System.err.println("⚠️ EmailService deshabilitado. Faltan las siguientes configuraciones:");
            if (!hasApiKey) {
                System.err.println("   - RESEND_API_KEY: Variable de entorno no configurada");
            }
            if (!hasNotificationEmail) {
                System.err.println("   - TRACKING_NOTIFICATION_EMAIL: Variable de entorno no configurada");
            }
            if (!hasFromEmail) {
                System.err.println("   - resend.from: No configurado en application.yml");
            }

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