package co.edu.unbosque.travelx.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * Filtro de autenticación JWT que se ejecuta una vez por cada solicitud HTTP.
 * Intercepta las peticiones, extrae y valida el token JWT del encabezado
 * de autorización, y establece la autenticación en el contexto de seguridad.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;
	private final UserDetailsService userDetailsService;

	/**
	 * Constructor que inyecta las dependencias necesarias para la validación del token.
	 *
	 * @param jwtUtil            utilidad para operaciones sobre tokens JWT
	 * @param userDetailsService servicio para cargar los detalles del usuario autenticado
	 */
	public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Intercepta cada solicitud HTTP para extraer y validar el token JWT.
	 * Si el token es válido, establece la autenticación en el contexto de seguridad
	 * y permite que la solicitud continúe por la cadena de filtros.
	 *
	 * @param request     solicitud HTTP entrante
	 * @param response    respuesta HTTP saliente
	 * @param filterChain cadena de filtros de seguridad
	 * @throws ServletException si ocurre un error en el procesamiento del servlet
	 * @throws IOException      si ocurre un error de entrada o salida
	 */
	@Override
	protected void doFilterInternal(
			HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization");
		String username = null;
		String jwt = null;

		if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
			jwt = authorizationHeader.substring(7);
			try {
				username = jwtUtil.extractUsername(jwt);
			} catch (Exception e) {
				logger.error("Error extracting username from token", e);
			}
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			if (jwtUtil.validateToken(jwt, userDetails)) {
				UsernamePasswordAuthenticationToken authenticationToken =
						new UsernamePasswordAuthenticationToken(
								userDetails, null, userDetails.getAuthorities());
				authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			}
		}

		filterChain.doFilter(request, response);
	}
}