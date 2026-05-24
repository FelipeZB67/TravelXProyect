package co.edu.unbosque.travelx.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
	 * Envía un correo electrónico con el código de verificación al destinatario indicado.
	 *
	 * @param destino dirección de correo electrónico del destinatario
	 * @param codigo  código de verificación a incluir en el mensaje
	 */
	public void enviarCodigoVerificacion(String destino, String codigo) {
		SimpleMailMessage mensaje = new SimpleMailMessage();
		mensaje.setFrom(fromEmail);
		mensaje.setTo(destino);
		mensaje.setSubject("Código de verificación - TravelX");
		mensaje.setText("Tu código de verificación es: " + codigo);

		mailSender.send(mensaje);
	}
}