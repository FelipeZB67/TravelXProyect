package co.edu.unbosque.travelx.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import co.edu.unbosque.travelx.entity.Persona;

/**
 * Clase utilitaria para la generación, validación y extracción de información
 * de tokens JWT. Utiliza el algoritmo HMAC SHA-256 para firmar los tokens.
 */
@Component
public class JwtUtil {

	private static final long JWT_TOKEN_VALIDITY = 24 * 60 * 60 * 1000;

	@Value("${jwt.secret:defaultSecretKeyWhichShouldBeAtLeast32CharactersLong}")
	private String secret;

	/**
	 * Genera la clave de firma HMAC a partir del secreto configurado.
	 *
	 * @return clave de firma para los tokens JWT
	 */
	private Key getSigningKey() {
		byte[] keyBytes = secret.getBytes();
		return Keys.hmacShaKeyFor(keyBytes);
	}

	/**
	 * Extrae el nombre de usuario contenido en el token JWT.
	 *
	 * @param token token JWT del que se extrae el usuario
	 * @return nombre de usuario almacenado en el subject del token
	 */
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}

	/**
	 * Extrae la fecha de expiración del token JWT.
	 *
	 * @param token token JWT del que se extrae la expiración
	 * @return fecha de expiración del token
	 */
	public Date extractExpiration(String token) {
		return extractClaim(token, Claims::getExpiration);
	}

	/**
	 * Extrae el rol del usuario contenido en el token JWT.
	 *
	 * @param token token JWT del que se extrae el rol
	 * @return nombre del rol almacenado en el token
	 */
	public String extractRole(String token) {
		return extractClaim(token, claims -> claims.get("role", String.class));
	}

	/**
	 * Extrae un claim específico del token JWT aplicando una función resolutora.
	 *
	 * @param token          token JWT del que se extrae el claim
	 * @param claimsResolver función que define qué claim extraer
	 * @return valor del claim extraído
	 */
	public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}

	/**
	 * Extrae todos los claims contenidos en el token JWT.
	 *
	 * @param token token JWT a procesar
	 * @return objeto Claims con toda la información del token
	 */
	private Claims extractAllClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody();
	}

	/**
	 * Verifica si el token JWT ha expirado.
	 *
	 * @param token token JWT a verificar
	 * @return true si el token ha expirado, false si aún es válido
	 */
	private Boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}

	/**
	 * Genera un token JWT para el usuario autenticado.
	 * Incluye las autoridades y el rol del usuario como claims adicionales.
	 *
	 * @param userDetails detalles del usuario autenticado
	 * @return token JWT generado
	 */
	public String generateToken(UserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put("authorities", userDetails.getAuthorities());
		if (userDetails instanceof Persona) {
			Persona user = (Persona) userDetails;
			claims.put("role", user.getTipoUsuario().name());
		}
		return createToken(claims, userDetails.getUsername());
	}

	/**
	 * Construye y firma el token JWT con los claims, subject y tiempos de emisión
	 * y expiración especificados.
	 *
	 * @param claims  mapa de claims adicionales a incluir en el token
	 * @param subject nombre de usuario que se establece como subject del token
	 * @return token JWT firmado en formato compacto
	 */
	private String createToken(Map<String, Object> claims, String subject) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	/**
	 * Valida el token JWT verificando que el usuario coincida y que el token no haya expirado.
	 *
	 * @param token       token JWT a validar
	 * @param userDetails detalles del usuario contra el que se valida el token
	 * @return true si el token es válido, false en caso contrario
	 */
	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}
}