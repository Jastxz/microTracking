package es.jastxz.micro_tracking.service;

import org.springframework.stereotype.Service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class EmailService {

    private final Resend resend;
    private final String notificationEmail;
    private final String fromEmail;
    private final boolean isEnabled;

    public EmailService() {
        Map<String, String> emailData = readEmailData();
        
        String apiKey = emailData.get("RESEND_API_KEY");
        this.notificationEmail = emailData.get("TRACKING_NOTIFICATION_EMAIL");
        this.fromEmail = emailData.get("FROM");

        boolean hasApiKey = apiKey != null && !apiKey.isBlank();
        boolean hasNotificationEmail = notificationEmail != null && !notificationEmail.isBlank();
        boolean hasFromEmail = fromEmail != null && !fromEmail.isBlank();

        if (hasApiKey && hasNotificationEmail && hasFromEmail) {
            this.resend = new Resend(apiKey);
            this.isEnabled = true;
            System.out.println("✅ EmailService habilitado correctamente");
        } else {
            System.err.println("⚠️ EmailService deshabilitado - configuración incompleta");
            this.resend = null;
            this.isEnabled = false;
        }
    }

    private Map<String, String> readEmailData() {
        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/.emailData"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                
                String[] parts = line.split(":", 2);
                if (parts.length == 2) {
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            System.err.println("⚠️ No se pudo leer .emailData: " + e.getMessage());
        }
        return data;
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
                    .html(body)
                    .build();

            resend.emails().send(options);
            
            System.out.println("✅ Email enviado correctamente a " + notificationEmail);
        } catch (ResendException e) {
            System.err.println("❌ Error enviando email: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
