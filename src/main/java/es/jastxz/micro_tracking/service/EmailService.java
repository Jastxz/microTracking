package es.jastxz.micro_tracking.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

@Service
public class EmailService {

    private final Resend resend;
    private final String notificationEmail;
    private final String fromEmail;
    private final boolean isEnabled;

    public EmailService(
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
            this.resend = new Resend(apiKey);
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

            this.resend = null;
            this.isEnabled = false;
        }
    }

    public void sendEmail(String subject, String body) {
        if (!isEnabled || resend == null) {
            System.out.println("⚠️ Email omitido (servicio deshabilitado): " + subject);
            return;
        }

        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(notificationEmail)
                    .subject(subject)
                    .html(body)  // o .text(body) si prefieres texto plano
                    .build();

            resend.emails().send(options);
            
            System.out.println("✅ Email enviado correctamente a " + notificationEmail);
        } catch (ResendException e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}