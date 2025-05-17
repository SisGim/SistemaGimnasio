package util;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;

import java.io.File;

public class CorreoUtil {

    public static void enviarConfirmacion(String destinatario) {
        String remitente = "sistemagimnasio84@gmail.com";
        String clave = "gdpuwpcrmafahiqj"; // Reemplaza por tu contraseña de aplicación

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(remitente));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject("Confirmación de Registro - Titan Forge");

            String htmlMsg = """
                <div style='font-family:Arial,sans-serif; padding:20px; text-align:center;'>
                    <h2 style='color:#1DB954;'>¡Bienvenido a Titan Forge!</h2>
                    <p>Hola,</p>
                    <p>Tu cuenta ha sido registrada correctamente en nuestro sistema.<br>
                    Gracias por unirte a <strong>Titan Forge</strong>.</p>
                    <br>
                    <img src="cid:logoTitan" alt="Logo Titan Forge" style="width:300px; margin-top:20px;">
                </div>
                """;

            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlMsg, "text/html");

            InputStream is = CorreoUtil.class.getResourceAsStream("/resources/logo.png");
            if (is == null) {
                System.err.println("❌ No se encontró el recurso 'logo.png'");
                return;
            }

            File tempFile = File.createTempFile("logo", ".png");
            Files.copy(is, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            is.close();

            MimeBodyPart imagePart = new MimeBodyPart();
            imagePart.setDataHandler(new DataHandler(new FileDataSource(tempFile)));
            imagePart.setHeader("Content-ID", "<logoTitan>");
            imagePart.setFileName("logo.png");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);
            multipart.addBodyPart(imagePart);

            mensaje.setContent(multipart);
            Transport.send(mensaje);

            System.out.println("✅ Correo enviado a " + destinatario);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo:");
            e.printStackTrace();
        }
    }
}
