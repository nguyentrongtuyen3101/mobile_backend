/*package mobile.com.api.config;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.lang.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.JwtParserBuilder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Component
public class JwtFilter implements Filter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @PostConstruct
    public void init() {
        System.out.println("Initializing JwtFilter, jwtSecret: " + jwtSecret);
        if (jwtSecret == null || jwtSecret.isEmpty()) {
            System.out.println("Warning: jwtSecret is not configured properly in application.properties");
            // Không throw exception để ứng dụng chạy, nhưng log cảnh báo
        } else {
            System.out.println("JwtFilter initialized successfully with jwtSecret length: " + jwtSecret.length());
        }
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, javax.servlet.ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        
        System.out.println("Request URI: " + httpRequest.getRequestURI());
        String authHeader = httpRequest.getHeader("Authorization");
        System.out.println("Authorization Header: " + authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        	System.out.println("Invalid or missing Authorization header");
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ hoặc không tồn tại");
            return;
        }

        String token = authHeader.substring(7);
        System.out.println("Token: " + token);

        try {
            // Giải mã jwtSecret từ Base64
        	System.out.println("jwtSecret: " + jwtSecret);
            byte[] secretKeyBytes = Base64.getDecoder().decode(jwtSecret);
            System.out.println("Decoded secretKeyBytes length: " + secretKeyBytes.length);
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKeyBytes)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            System.out.println("Claims: " + claims);
            String role = (String) claims.get("role");
            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
         // Tạo Authentication object và đặt vào SecurityContextHolder
            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                authorities
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
            request.setAttribute("claims", claims);
            chain.doFilter(request, response);
        } catch (Exception e) {
        	System.out.println("Error validating token: " + e.getMessage());
        	e.printStackTrace();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token không hợp lệ: " + e.getMessage());
        }
    }
}*/