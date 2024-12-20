package namviet.rfid_api.config.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import namviet.rfid_api.config.data.CustomUserDetails;
import namviet.rfid_api.exception.CustomException;
import namviet.rfid_api.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return CustomUserDetails.builder()
                .userE(userRepository.findByUserName(username)
                        .orElseThrow(() -> new CustomException("Account not found", HttpStatus.NOT_FOUND)))
                .build();
    }

    public CustomUserDetails jwtToCustomUserDetails(DecodedJWT decodedJWT){
        return CustomUserDetails.builder()
                .userE(userRepository.findByUserNameAndIsActiveTrue(decodedJWT.getSubject() )
                .orElse(null))
                .build();
    }
}
