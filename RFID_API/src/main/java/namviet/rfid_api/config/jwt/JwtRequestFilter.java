package namviet.rfid_api.config.jwt;

import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.config.SecurityConfig;
import namviet.rfid_api.config.authentication.CustomUserDetailsAuthenticationToken;
import namviet.rfid_api.config.data.CustomUserDetails;
import namviet.rfid_api.config.security.CustomUserDetailsService;
import namviet.rfid_api.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final List<String> nonAuthenticatedUrls = List.of(SecurityConfig.nonAuthenticatedUrls);
    private final CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain Chain)
            throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        if(isNonAuthenticatedUrl(requestURI)){
            Chain.doFilter(request,response);
            return;
        }

        String token = getJwtToken(request).orElseThrow(() -> new CustomException("Authorization token không hơp lệ", HttpStatus.FORBIDDEN));
        DecodedJWT decodedJWT = jwtUtil.verifyToken(token);
        if(decodedJWT == null){
            throw new CustomException("Token không xác thực", HttpStatus.FORBIDDEN);
        }
        CustomUserDetails customUserDetails = service.jwtToCustomUserDetails(decodedJWT);
        if(customUserDetails.getUserE() == null){
            Chain.doFilter(request, response);
            return;
        }

        var authenticationToken = new CustomUserDetailsAuthenticationToken(customUserDetails);
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        Chain.doFilter(request,response);

    }

    private boolean isNonAuthenticatedUrl(String requestURI) {
        return nonAuthenticatedUrls.stream()
                .anyMatch(urlPattern -> requestURI.matches(convertToRegex(urlPattern)));
    }

    private String convertToRegex(String urlPattern) {
        return urlPattern.replace("**", ".*").replace("*", "[^/]+");
    }

    private Optional<String> getJwtToken(HttpServletRequest request){
        var token = request.getHeader("Authorization");
        if(token != null && token.startsWith("Bearer ")){
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }

}
