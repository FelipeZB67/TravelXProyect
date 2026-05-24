package co.edu.unbosque.travelx.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

/**
 * Servicio para el envío de correos electrónicos en TravelX,
 * utilizando {@link JavaMailSender} para la comunicación con el servidor SMTP.
 */
@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envía un correo HTML con el código de verificación al destinatario indicado.
     *
     * @param destino dirección de correo electrónico del destinatario
     * @param codigo  código de verificación a incluir en el mensaje
     */
    public void enviarCodigoVerificacion(String destino, String codigo) throws MessagingException {
        MimeMessage mensaje = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mensaje, true, "UTF-8");

        helper.setFrom(fromEmail);
        helper.setTo(destino);
        helper.setSubject("Código de verificación — TravelX");
        helper.setText(construirHtml(codigo), true);

        mailSender.send(mensaje);
    }

    private String construirHtml(String codigo) {
        return """
            <!DOCTYPE html>
            <html lang="es">
            <head>
              <meta charset="UTF-8"/>
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>Verificación TravelX</title>
            </head>
            <body style="margin:0;padding:0;background-color:#f0f4f8;font-family:'Segoe UI',Arial,sans-serif;">

              <table width="100%" cellpadding="0" cellspacing="0" style="background-color:#f0f4f8;padding:40px 0;">
                <tr>
                  <td align="center">
                    <table width="600" cellpadding="0" cellspacing="0" style="max-width:600px;width:100%;">

                      <!-- HEADER -->
                      <tr>
                        <td style="background:linear-gradient(135deg,#3aa0e6 0%,#0055bb 100%);border-radius:16px 16px 0 0;padding:40px 48px;text-align:center;">
                          <p style="margin:0 0 8px;font-size:13px;font-weight:700;color:rgba(255,255,255,0.75);text-transform:uppercase;letter-spacing:2px;">
                            ✈ TravelX
                          </p>
                          <h1 style="margin:0;font-size:32px;font-weight:800;color:#ffffff;line-height:1.2;">
                            Verifica tu cuenta
                          </h1>
                          <p style="margin:12px 0 0;font-size:15px;color:rgba(255,255,255,0.82);font-weight:300;">
                            Estás a un paso de comenzar tu próxima aventura
                          </p>
                        </td>
                      </tr>

                      <!-- BODY -->
                      <tr>
                        <td style="background:#ffffff;padding:48px;text-align:center;">

                          <p style="margin:0 0 8px;font-size:15px;color:#6b7c93;">
                            Tu código de verificación es:
                          </p>

                          <!-- CODIGO -->
                          <div style="display:inline-block;margin:24px 0;padding:20px 48px;background:#f0f4f8;border-radius:14px;border:2px dashed #3aa0e6;">
                            <span style="font-size:42px;font-weight:800;letter-spacing:10px;color:#0055bb;">
                              """ + codigo + """
                            </span>
                          </div>

                          <p style="margin:0 0 32px;font-size:14px;color:#9aa8b8;line-height:1.6;">
                            Este código expira en <strong style="color:#3aa0e6;">10 minutos</strong>.<br/>
                            Si no solicitaste esto, ignora este correo.
                          </p>

                          <div style="border-top:1px solid #e8edf3;padding-top:32px;">
                            <p style="margin:0;font-size:13px;color:#9aa8b8;">
                              ¿Necesitas ayuda?
                              <a href="mailto:soporte@travelx.com" style="color:#3aa0e6;text-decoration:none;font-weight:600;">
                                soporte@travelx.com
                              </a>
                            </p>
                          </div>

                        </td>
                      </tr>

                      <!-- FOOTER -->
                      <tr>
                        <td style="background:#f8fafd;border-radius:0 0 16px 16px;padding:24px 48px;text-align:center;border-top:1px solid #e8edf3;">
                          <p style="margin:0 0 4px;font-size:13px;color:#9aa8b8;">
                            © 2026 TravelX — El mundo es tuyo
                          </p>
                          <p style="margin:0;font-size:12px;color:#b0bec5;">
                            Este es un correo automático, por favor no respondas a este mensaje.
                          </p>
                        </td>
                      </tr>

                    </table>
                  </td>
                </tr>
              </table>

            </body>
            </html>
            """;
    }
}