package namviet.rfid_api.config.data;

import lombok.Builder;
import lombok.Getter;
import namviet.rfid_api.entity.AccountE;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


@Getter
@Builder
public class CustomUserDetails implements UserDetails {
    private final AccountE userE;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getPassword() {
        return this.userE.getPassword();
    }

    @Override
    public String getUsername() {
        return this.userE.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return this.userE.isActive();
    }


}

