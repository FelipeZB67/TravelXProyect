package co.edu.unbosque.travelx.security;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.http.HttpMethod;

/**
 * Clase de configuración de seguridad de la aplicación. Define la cadena de
 * filtros, reglas de autorización por endpoint, política de sesiones sin
 * estado, configuración CORS y los beans necesarios para la autenticación
 * mediante JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthFilter;
	private final UserDetailsService userDetailsService;

	public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService) {
		this.jwtAuthFilter = jwtAuthFilter;
		this.userDetailsService = userDetailsService;
	}

	/**
	 * Configura la cadena de filtros de seguridad, deshabilitando CSRF, aplicando
	 * la configuración CORS, definiendo los endpoints públicos y protegidos,
	 * estableciendo sesiones sin estado y registrando el filtro JWT.
	 *
	 * @param http objeto de configuración de seguridad HTTP
	 * @return {@link SecurityFilterChain} con la configuración aplicada
	 * @throws Exception si ocurre un error durante la configuración
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
						.requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/register").permitAll()
						.requestMatchers(HttpMethod.POST, "/auth/verify").permitAll()
						.requestMatchers("/persona/mi-cuenta").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/persona/**").hasRole("ADMINISTRADOR").requestMatchers("/reservas/admin/**")
						.hasRole("ADMINISTRADOR").requestMatchers(HttpMethod.GET, "/reservas/todas")
						.hasRole("ADMINISTRADOR").requestMatchers("/reservas/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/travel-search/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/visa/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/terrestrial/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/nominatim/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/airbnb/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/hotel/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/google-flights/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.requestMatchers("/google-flights-airport/**").hasAnyRole("USUARIO", "ADMINISTRADOR")
						.anyRequest().authenticated())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authenticationProvider(authenticationProvider())
				.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Configura y retorna la fuente de configuración CORS, permitiendo cualquier
	 * origen, los métodos HTTP estándar y todas las cabeceras.
	 *
	 * @return {@link CorsConfigurationSource} con la política CORS definida
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();

		config.setAllowedOrigins(List.of("http://localhost:4200", "http://localhost:4201", "https://gpcueb.org",
				"https://travelxoficial.netlify.app"));

		config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);

		return source;
	}

	/**
	 * Crea y configura el proveedor de autenticación basado en
	 * {@link UserDetailsService} y el codificador de contraseñas BCrypt.
	 *
	 * @return {@link AuthenticationProvider} configurado para autenticación por
	 *         base de datos
	 */
	@Bean
	public AuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());
		return authProvider;
	}

	/**
	 * Expone el {@link AuthenticationManager} como bean de Spring para ser
	 * inyectado en los controladores de autenticación.
	 *
	 * @param config configuración de autenticación de Spring Security
	 * @return {@link AuthenticationManager} obtenido desde la configuración
	 * @throws Exception si ocurre un error al obtener el administrador de
	 *                   autenticación
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
		return config.getAuthenticationManager();
	}

	/**
	 * Crea y expone el codificador de contraseñas BCrypt como bean de Spring.
	 *
	 * @return {@link PasswordEncoder} basado en el algoritmo BCrypt
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}