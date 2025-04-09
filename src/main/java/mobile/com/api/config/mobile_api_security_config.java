package mobile.com.api.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import mobile.com.api.entity.Account;
import mobile.com.api.service.account_service;

@Configuration
@EnableWebSecurity
public class mobile_api_security_config {

    private final account_service accountService;

    public mobile_api_security_config(account_service accountService) {
        this.accountService = accountService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Thêm CORS vào đây
            .authorizeHttpRequests(auth -> auth
                .antMatchers("/API_for_mobile/api/checkmobile/login").permitAll() // Đường dẫn API của bạn
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://10.0.2.2:19006", "http://localhost:19006")); // Origin từ React Native
        configuration.setAllowedMethods(Arrays.asList("GET", "POST")); // Cho phép GET và POST
        configuration.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả header
        configuration.setAllowCredentials(true); // Nếu cần gửi cookie hoặc credentials
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cho mọi endpoint
        return source;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Account account = accountService.findByGmail(username);
            if (account == null) {
                throw new UsernameNotFoundException("User not found");
            }
            return new org.springframework.security.core.userdetails.User(
                account.getGmail(),
                account.getMatKhau(),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
            );
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}