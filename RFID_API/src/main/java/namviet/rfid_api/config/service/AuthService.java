package namviet.rfid_api.config.service;

import lombok.RequiredArgsConstructor;
import namviet.rfid_api.config.data.CustomUserDetails;
import namviet.rfid_api.config.jwt.JwtUtil;
import namviet.rfid_api.dto.request.LoginRequestDTO;
import namviet.rfid_api.dto.response.LoginResponseDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public LoginResponseDTO authenticate(LoginRequestDTO dto){
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            var userDetails = (CustomUserDetails)authentication.getPrincipal();
            return LoginResponseDTO.builder()
                    .token(jwtUtil.generateToken(userDetails))
                    .build();
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Sai tên đăng nhập hoặc mật khẩu");
        }
    }
}
