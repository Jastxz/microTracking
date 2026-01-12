package es.jastxz.micro_tracking.service;

import es.jastxz.micro_tracking.model.IpInfoResponse;
import es.jastxz.micro_tracking.model.TimeData;
import es.jastxz.micro_tracking.model.VisitorData;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TrackingService {

    private final EmailService emailService;
    private final RestTemplate restTemplate;
    private static final String IP_API_URL = "https://ipapi.co/{ip}/json/";

    public TrackingService(EmailService emailService) {
        this.emailService = emailService;
        this.restTemplate = new RestTemplate();
    }

    @Async
    public void processVisit(VisitorData data, String clientIp) {
        // Obtener info de la IP
        IpInfoResponse ipInfo = getIpInfo(clientIp);

        // Construir email
        String subject = "🔔 Nuevo visitante en tu CV";
        String body = buildEmailBody(data, clientIp, ipInfo);

        // Enviar email
        emailService.sendEmail(subject, body);
    }

    @Async
    public void processTimeSpent(TimeData data) {
        // Solo log por ahora, puedes enviar email si quieres
        System.out.println("Usuario pasó " + data.getTimeSpent() + "s en " + data.getPage());
    }

    private IpInfoResponse getIpInfo(String ip) {
        try {
            // Evitar localhost en desarrollo
            if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
                IpInfoResponse local = new IpInfoResponse();
                local.setIp(ip);
                local.setOrg("Localhost (Desarrollo)");
                local.setCity("Local");
                return local;
            }

            return restTemplate.getForObject(
                    IP_API_URL.replace("{ip}", ip),
                    IpInfoResponse.class);
        } catch (Exception e) {
            System.err.println("Error obteniendo info de IP: " + e.getMessage());
            IpInfoResponse fallback = new IpInfoResponse();
            fallback.setIp(ip);
            fallback.setOrg("Desconocido");
            return fallback;
        }
    }

    private String buildEmailBody(VisitorData data, String clientIp, IpInfoResponse ipInfo) {
        StringBuilder body = new StringBuilder();
        body.append("🔔 Nuevo visitante en tu CV\n\n");
        body.append("👤 Información del visitante:\n");
        body.append("━━━━━━━━━━━━━━━━━━━━━━━━━━\n\n");

        body.append("📍 Ubicación:\n");
        body.append("   • IP: ").append(clientIp).append("\n");
        if (ipInfo.getCity() != null) {
            body.append("   • Ciudad: ").append(ipInfo.getCity()).append("\n");
        }
        if (ipInfo.getRegion() != null) {
            body.append("   • Región: ").append(ipInfo.getRegion()).append("\n");
        }
        if (ipInfo.getCountryName() != null) {
            body.append("   • País: ").append(ipInfo.getCountryName()).append("\n");
        }

        body.append("\n🏢 Empresa/ISP:\n");
        body.append("   • Organización: ").append(ipInfo.getOrg() != null ? ipInfo.getOrg() : "Desconocido")
                .append("\n");

        body.append("\n💻 Dispositivo:\n");
        body.append("   • Navegador: ").append(parseUserAgent(data.getUserAgent())).append("\n");
        body.append("   • Resolución: ").append(data.getScreenResolution()).append("\n");
        body.append("   • Idioma: ").append(data.getLanguage()).append("\n");

        body.append("\n📄 Navegación:\n");
        body.append("   • Página: ").append(data.getPage()).append("\n");
        body.append("   • Ruta: ").append(data.getPath()).append("\n");
        if (data.getReferrer() != null && !data.getReferrer().isEmpty()) {
            body.append("   • Origen: ").append(data.getReferrer()).append("\n");
        }

        body.append("\n🕐 Tiempo:\n");
        body.append("   • Hora: ").append(data.getTimestamp()).append("\n");
        body.append("   • Session ID: ").append(data.getSessionId()).append("\n");

        return body.toString();
    }

    private String parseUserAgent(String ua) {
        if (ua == null)
            return "Desconocido";

        String browser = "Desconocido";
        String os = "Desconocido";

        // Detectar navegador
        if (ua.contains("Chrome") && !ua.contains("Edg"))
            browser = "Chrome";
        else if (ua.contains("Firefox"))
            browser = "Firefox";
        else if (ua.contains("Safari") && !ua.contains("Chrome"))
            browser = "Safari";
        else if (ua.contains("Edg"))
            browser = "Edge";

        // Detectar OS
        if (ua.contains("Windows"))
            os = "Windows";
        else if (ua.contains("Mac OS"))
            os = "macOS";
        else if (ua.contains("Linux"))
            os = "Linux";
        else if (ua.contains("Android"))
            os = "Android";
        else if (ua.contains("iOS"))
            os = "iOS";

        return browser + " en " + os;
    }
}