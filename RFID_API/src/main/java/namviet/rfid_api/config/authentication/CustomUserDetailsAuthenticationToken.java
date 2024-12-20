package namviet.rfid_api.config.authentication;

import namviet.rfid_api.config.data.CustomUserDetails;
import org.springframework.security.authentication.AbstractAuthenticationToken;

public class CustomUserDetailsAuthenticationToken extends AbstractAuthenticationToken {
    private final CustomUserDetails userDetails;
    public CustomUserDetailsAuthenticationToken(CustomUserDetails userDetails) {
        super(userDetails.getAuthorities());
        this.userDetails = userDetails;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }
}
