package es.codeurjc.daw.powergym.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

	@Autowired
	RepositoryUserDetailsService userDetailsService;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authenticationProvider());

		http
				.authorizeHttpRequests(authorize -> authorize
						// PUBLIC PAGES
						.requestMatchers("/").permitAll()
						.requestMatchers("/login").permitAll()
						.requestMatchers("/loginerror").permitAll()
						.requestMatchers("/error").permitAll()
						.requestMatchers("/images/**").permitAll()
						.requestMatchers("/books/**").permitAll()
						.requestMatchers("/trainings").permitAll()
						.requestMatchers("/trainings/**").permitAll()
						.requestMatchers("/nutritions").permitAll()
						.requestMatchers("/nutritions/**").permitAll()
						.requestMatchers("/assets/**").permitAll() // Allow access to static resources
						.requestMatchers("/css/**").permitAll()
						.requestMatchers("/favicon.ico").permitAll()
						.requestMatchers("/register").permitAll()
						// PRIVATE PAGES
						.requestMatchers("/createTraining/**").hasAnyRole("USER")
						.requestMatchers("/editTraining").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/editTraining/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/deleteTraining/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/subscribeTraining/**").hasAnyRole("USER")
						.requestMatchers("/unsubscribeTraining/**").hasAnyRole("USER")
						.requestMatchers("/createNutrition/**").hasAnyRole("USER")
						.requestMatchers("/editNutrition").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/editNutrition/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/deleteNutrition/**").hasAnyRole("ADMIN", "USER")
						.requestMatchers("/subscribeNutrition/**").hasAnyRole("USER")
						.requestMatchers("/unsubscribeNutrition/**").hasAnyRole("USER")
						.requestMatchers("/progress").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/profileUser").hasAnyRole("USER")
                        .requestMatchers("/profile").hasAnyRole("USER","ADMIN")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
						.requestMatchers("/admin/users/**").hasAnyRole("ADMIN")
						.requestMatchers("/newbook").hasAnyRole("USER")
						.requestMatchers("/editbook").hasAnyRole("USER")
						.requestMatchers("/editbook/**").hasAnyRole("USER")
						.requestMatchers("/removebook/**").hasAnyRole("ADMIN"))
				.formLogin(formLogin -> formLogin
						.loginPage("/login")
						.failureUrl("/loginerror")
						.defaultSuccessUrl("/")
						.permitAll())
				.logout(logout -> logout
						.logoutUrl("/logout")
						.logoutSuccessUrl("/")
						.permitAll());

		return http.build();
	}
}
