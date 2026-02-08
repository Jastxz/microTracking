package es.jastxz.micro_tracking;

import es.jastxz.micro_tracking.service.EmailService;

/**
 * Test local para enviar un email simple usando Resend.
 * 
 * Este NO es un test unitario de JUnit, es una clase ejecutable
 * para probar el envío de emails de forma local.
 * 
 * Para ejecutar:
 * 1. Asegúrate de tener las credenciales correctas en el código
 * 2. Ejecuta esta clase como una aplicación Java normal (main method)
 */
public class EmailServiceLocalTest {

    public static void main(String[] args) {
        System.out.println("=== Test Local de EmailService ===");
        System.out.println("Iniciando envío de email de prueba...\n");

        // Configuración estática para el test local
        String apiKey = "";// Añadir Api Key para hacer pruebas
        String toEmail = "javgilbla@proton.me";
        String fromEmail = "Portafolio <portafolio@javig.org>";

        // Crear instancia del servicio con las credenciales
        EmailService emailService = new EmailService(apiKey, toEmail, fromEmail);

        // Enviar email de prueba
        String subject = "Hello World - Test Email";
        String body = "<h1>Hello World!</h1>" +
                      "<p>Este es un email de prueba enviado desde el microservicio de tracking.</p>" +
                      "<p>Si recibes este mensaje, el servicio de email está funcionando correctamente.</p>" +
                      "<hr>" +
                      "<p><small>Test enviado desde EmailServiceLocalTest</small></p>";

        try {
            emailService.sendEmail(subject, body);
            System.out.println("\n✅ Test completado. Revisa tu bandeja de entrada en: " + toEmail);
        } catch (Exception e) {
            System.err.println("\n❌ Error durante el test:");
            e.printStackTrace();
        }

        System.out.println("\n=== Fin del Test ===");
    }
}
