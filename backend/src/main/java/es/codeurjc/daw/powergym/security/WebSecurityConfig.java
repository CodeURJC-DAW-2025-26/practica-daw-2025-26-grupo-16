package es.codeurjc.daw.powergym.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import es.codeurjc.daw.powergym.security.jwt.JwtTokenProvider;
import es.codeurjc.daw.powergym.security.jwt.UnauthorizedHandlerJwt;
import es.codeurjc.daw.powergym.security.jwt.ForbiddenHandlerJwt;
import es.codeurjc.daw.powergym.security.jwt.JwtRequestFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    public RepositoryUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UnauthorizedHandlerJwt unauthorizedHandlerJwt;

    @Autowired
    private ForbiddenHandlerJwt forbiddenHandlerJwt;
    

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
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http
        .securityMatcher("/api/**", "/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html")
        .exceptionHandling(handling -> handling.authenticationEntryPoint(unauthorizedHandlerJwt).accessDeniedHandler(forbiddenHandlerJwt));
        
        http.authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/v3/api-docs/**", "/v3/api-docs.yaml", "/swagger-ui/**", "/swagger-ui.html").permitAll()

            .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/users/").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/users/me").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/users/me").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/users/me/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/users/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/users/**").hasRole("ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/v1/nutritions/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/nutritions/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/nutritions/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/nutritions/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/nutritions/*/pdf").hasAnyRole("USER","ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/v1/trainings/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/api/v1/trainings/**").hasAnyRole("USER", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/v1/trainings/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/v1/trainings/**").hasAnyRole("USER","ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/trainings/*/pdf").hasAnyRole("USER","ADMIN")

            .requestMatchers(HttpMethod.GET, "/api/v1/images/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/api/v1/progress/**").hasAnyRole("USER","ADMIN")

            .anyRequest().authenticated()
        );

        http.formLogin(form -> form.disable());
        http.httpBasic(httpBasic -> httpBasic.disable());

        http.csrf(csrf -> csrf.disable());

        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(new JwtRequestFilter(userDetailsService, jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain webFilterChain(HttpSecurity http) throws Exception {

        http.authenticationProvider(authenticationProvider());

        http.authorizeHttpRequests(authorize -> authorize
            // PUBLIC PAGES
            .requestMatchers("/", "/login", "/loginerror", "/register", "/error").permitAll()
            .requestMatchers("/trainings", "/trainings/**", "/nutritions", "/nutritions/**").permitAll()
            .requestMatchers("/images/**", "/assets/**", "/css/**", "/js/**", "/favicon.ico").permitAll()
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
            .anyRequest().authenticated()
        )
        .formLogin(form -> form
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