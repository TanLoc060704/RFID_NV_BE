package namviet.rfid_api.config.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.config.jwt.JwtUtil;
import namviet.rfid_api.config.service.AuthService;
import namviet.rfid_api.constant.ResponseObject;
import namviet.rfid_api.dto.request.LoginRequestDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final HttpServletRequest httpServletRequest;

    @PostMapping("/login")
    public ResponseObject<?> doLogin(@RequestBody @Valid LoginRequestDTO dto) {
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .data(authService.authenticate(dto))
                .message("Đăng nhập thành công")
                .build();
    }

    @GetMapping("/is-login")
    public ResponseObject<?> isLogin(){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        return ResponseObject.builder()
                .status(HttpStatus.OK)
                .message("Hello" + jwtUtil.extractUsername(token) + "!")
                .build();
    }

}
